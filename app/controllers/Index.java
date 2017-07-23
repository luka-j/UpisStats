package controllers;

import akka.stream.ConnectionException;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.*;
import org.intellij.lang.annotations.MagicConstant;
import play.http.HttpEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import upismpn.obrada.OsnovneBase;
import upismpn.obrada.SmeroviBase;
import upismpn.obrada.UceniciGroup;
import upismpn.obrada.UceniciGroupBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * Created by luka on 5.5.16..
 */
public class Index extends Controller {
    private static final boolean INIT_PHASE = true;
    private static final boolean DEBUG = false;
    private static final boolean LOG_QUERY_ERRORS = true;
    public static final int CURRENT_YEAR = 15;

    static Index instance; //well, shit
    //(need this for proper db injection)
    //todo fix mess

    private static String errors = "";

    @Inject
    play.db.Database db;

    public static void errors(int sifra) {
        errors += sifra + ", ";
    }

    public Result populateDb() {
        long start = System.currentTimeMillis();
        if (!INIT_PHASE) return forbidden("Init phase over");
        SmeroviBase.load();
        UceniciGroup all = new UceniciGroupBuilder(null).getGroup();
        System.out.println("Loaded everything; populating");
        Ebean.execute(() -> all.forEach(Ucenik2015::create));
        Ebean.execute(() -> all.forEach(Ucenik2015::populateZelje));
        populateSchoolAverages();
        System.out.println("Done");
        long end = System.currentTimeMillis();
        System.out.println("Time: " + ((end - start) / 1000) / 60.0 + "min");
        return ok("Errors:\n" + errors);
    }

    private void populateAveragesInner(Object s, String columnName, long id) {
        List<Ucenik2015> group = Ucenik2015.finder.where().eq(columnName, id).findList();
        for (Method m : s.getClass().getMethods()) {
            if (m.getName().startsWith("set") && m.getParameterTypes()[0].equals(double.class)) {
                try {
                    m.invoke(s, 1.0); //invoking setter in order to force populating fields from db
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Field f : s.getClass().getFields()) {
            if (f.getType().equals(double.class) && !f.getName().toLowerCase().endsWith("real") &&
                    !f.getName().startsWith("procenat")) {
                try {
                    if (Ucenik2015.class.getField(f.getName()).getType().equals(int.class)) {
                        f.setDouble(s, group.stream().mapToInt((Ucenik2015 uc) -> {
                            try {
                                return Ucenik2015.class.getField(f.getName()).getInt(uc);
                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                throw new RuntimeException(e);
                            }
                        }).average().getAsDouble());
                    } else {
                        f.setDouble(s, group.stream().mapToDouble((Ucenik2015 uc) -> {
                            try {
                                return Ucenik2015.class.getField(f.getName()).getDouble(uc);
                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                throw new RuntimeException(e);
                            }
                        }).average().getAsDouble());
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    System.err.println("Invalid field " + f.getName());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Result populateSchoolAverages() {
        if (!INIT_PHASE) return forbidden("Init phase over");
        Ebean.execute(() -> {
            Smer2015.finder.findEach((Smer2015 s) -> {
                populateAveragesInner(s, "upisana_id", s.id);
                s.save();
            });
            OsnovnaSkola2015.finder.findEach((OsnovnaSkola2015 s) -> {
                populateAveragesInner(s, "osnovna_id", s.id);
                s.save();
            });
        });
        return ok("Done");
    }

    public Result fillInNeupisani() {
        if (!INIT_PHASE) return forbidden("Init phase over");
        OsnovneBase.load();
        System.out.println("Filling in missing data...");
        Ebean.execute(() -> OsnovnaSkola2016.finder.findEach(OsnovnaSkola2016::fillInNeupisani));
        return ok("Hopefully this worked");
    }

    public Result getData(String query) {
        if (INIT_PHASE) return new Result(503, HttpEntity.fromString("Ažuriranje podataka je u toku...\n" +
                "Probajte ponovo za par minuta", "UTF-8"));

        try {
            ParseResult res = parseQuery(query);
            if (res != null) {
                if(res.status == ParseResult.SUCCESS)
                    return ok(res.result);
                else if(res.status == ParseResult.PARTIAL_FAIL)
                    return status(201, res.result);
                else if(res.status == ParseResult.TOTAL_FAIL)
                    return badRequest(res.result);
                throw new RuntimeException("invalid ParseResult status... screwed up pretty bad");
            }
            else return internalServerError("db unavailable");
        } catch (ConnectionException ex) {
            return internalServerError(ex.getMessage());
        }
    }

    static class ParseResult {
        static final int SUCCESS=0;
        static final int PARTIAL_FAIL=1;
        static final int TOTAL_FAIL=2;
        public final ArrayNode result;
        public final @MagicConstant int status;

        private ParseResult(ArrayNode result,
                            @MagicConstant(intValues = {SUCCESS, PARTIAL_FAIL, TOTAL_FAIL}) int status) {
            this.result = result;
            this.status = status;
        }
    }
    public ParseResult parseQuery(String query) {
        if (db == null) return null;
        Parser p = new Parser(query);
        ArrayNode ret = Json.newArray();
        boolean partialFail=false, totalFail=false;
        //if (DEBUG) System.out.println("got query: " + query);

        Connection conn = null;
        try {
            int errors = 0;
            List<Parser.Action> acs = p.parse();

            conn = db.getConnection();
            if (conn == null) {
                throw new ConnectionException("db connection unavailable");
            }
            for (Parser.Action ac : acs) {
                ObjectNode jsonAction = Json.newObject();
                try {
                    if (ac.isOk()) {
                        jsonAction.put("type", ac.getAction());
                        if (ac.getAction() == Parser.Action.DUMP) {
                            Ucenik uc = Ucenik2015.finder.where().eq("sifra", Integer.parseInt(ac.getQuery())).findUnique(); //todo year-agnostic
                            jsonAction.put("data", String.valueOf(uc));
                        } else {
                            PreparedStatement st = conn.prepareStatement(ac.getQuery());
                            st.execute();
                            if (ac.getColor() != null)
                                jsonAction.put("color", Integer.toHexString(ac.getColor().getRGB()));
                            CountMatrix results = new CountMatrix();
                            do {
                                ResultSet r = st.getResultSet();
                                if (!r.next()) continue;
                                while (!r.isAfterLast()) {
                                    String[] axesData = new String[ac.getAxesCount()];
                                    for (int i = 0; i < ac.getAxesCount(); i++) {
                                        axesData[i] = (String.valueOf(r.getObject(i + 1)));
                                    }
                                    results.add(new CountMatrix.Datum(axesData));
                                    r.next();
                                }
                            } while (!(!st.getMoreResults() && (st.getUpdateCount() == -1)));

                            ArrayNode res = Json.newArray();
                            ArrayNode countAxis = Json.newArray();
                            ArrayNode[] dataAxes = new ArrayNode[ac.getAxesCount()];
                            for (int i = 0; i < dataAxes.length; i++) dataAxes[i] = Json.newArray();
                            int max = results.maxCount();
                            double factor = (4.2 / max) * Utils.log(1.5, max);
                            if (max == 1) factor = 4;
                            for (Map.Entry<CountMatrix.Datum, Integer> e : results.entrySet()) {
                                String[] vals = e.getKey().getArray();
                                for (int i = 0; i < vals.length; i++) {
                                    if (Utils.isDouble(vals[i])) {
                                        dataAxes[i].add(Double.parseDouble(vals[i]));
                                    } else {
                                        dataAxes[i].add(vals[i]);
                                    }
                                }
                                countAxis.add(e.getValue());
                            }
                            res.add(countAxis);
                            for (ArrayNode dataAxe : dataAxes) res.add(dataAxe);
                            jsonAction.put("factor", factor);
                            jsonAction.put("xname", ac.getXAxisName());
                            jsonAction.put("yname", ac.getYAxisName());
                            if (ac.getZAxisName() != null) jsonAction.put("zname", ac.getZAxisName());
                            jsonAction.set("data", res);
                        }
                    } else {
                        if (DEBUG) System.err.println("minor parseex");
                        jsonAction.put("error", ac.getExceptionDetails());
                        errors++;
                        if(LOG_QUERY_ERRORS) System.err.println("minor parseex: " + query);
                    }
                } catch (SQLException e) {
                    if (DEBUG) System.err.println("sqlex");
                    jsonAction.put("error", "SQLException: " + ac.getQuery());
                    errors++;
                    if (DEBUG) e.printStackTrace();
                    if(LOG_QUERY_ERRORS) System.err.println("sqlex " + query);
                }
                ret.add(jsonAction);
            }
            if(errors>0) partialFail=true;
            if(errors==acs.size()) totalFail=true;
        } catch (Parser.ParseException e) {
            if (DEBUG) System.err.println("major parseex");
            if(LOG_QUERY_ERRORS) System.err.println("major parseex: " + query);
            ObjectNode errorNode = Json.newObject();
            errorNode.put("error", e.getMessage());
            totalFail = true;
            ret.add(errorNode);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("error closing connection, " + e.getMessage());
                System.err.println("errorcode: " + e.getErrorCode() + ", sql state: " + e.getSQLState());
            }
        }
        int status=ParseResult.SUCCESS;
        if(partialFail) status=ParseResult.PARTIAL_FAIL;
        if(totalFail) status=ParseResult.TOTAL_FAIL;
        return new ParseResult(ret, status);
    }

    public Result hw() {
        if (instance == null) instance = this;
        return redirect("/query");
        //return populateSchoolAverages();
        //return ok("Hello world");
    }

    public Result query(String initialQuery) {
        if (instance == null) instance = this;
        if (INIT_PHASE) return new Result(503, HttpEntity.fromString("Ažuriranje podataka je u toku...\n" +
                "Probajte ponovo za par minuta", "UTF-8"));
        return ok(views.html.query.render(initialQuery));
    }
}

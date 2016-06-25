package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.CountMatrix;
import models.OsnovnaSkola;
import models.Smer;
import models.Ucenik;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
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

    @Inject
    play.db.Database db;
    private static String errors = "";

    public static void errors(int sifra) {
        errors += sifra + ", ";
    }

    private static final boolean INIT_PHASE = true;
    private static final boolean DEBUG = true;
    public static final int CURRENT_YEAR = 2015;

    private static final String EXAMPLE_QUERY = "x:zaokruzi#1.prosek.srpski, y:zaokruzi#1.prosek.matematika\n" +
            "crtaj zuto: osnovna prosek.ukupno<3.5 ili bodovi.zavrsni<20\n" +
            "crtaj crveno: ucenik upisao skola.okrug='gradbeograd'\n" +
            "crtaj plavo: ucenik pohadjao skola.ime='matematicka gimnazija-ogled'\n" +
            "crtaj zeleno: smer kvota>=50\n";
    private static final String EXAMPLE_RESULT = "[{\"type\":0,\"color\":\"ffffff00\",\"factor\":0.013699432389843029,\"xname\":\"zaokruzi#1.prosek.srpski\",\"yname\":\"zaokruzi#1.prosek.matematika\",\"data\":[[1972,197,329,815,176,2,28,1,608,74,160,358,580,67,402,575,410,3,30,23,471,348,149,216,7,338,68,279,6,284,250,91,3,133,205,20,281,1,29,6952,317,117,811,454,438,193,219,199,38,812,195,76,385,78,47,215,57,214,2660,124,25,128,465,473,92,4,240,37,46,613,1868,1156,749,262,1033,7,306,757,52,61,326,174,107,356,6,590,365,1,517,19,249,466,7,239,799,324,144,1,2,240,1,406,1,361,87,18,148,347],[2.7,4.7,4.3,2.7,5.0,3.6,2.0,1.7,3.3,3.7,2.0,4.0,3.3,2.0,4.0,3.7,3.7,1.3,3.0,2.3,2.7,4.3,3.0,5.0,2.3,4.3,3.0,5.0,2.3,4.7,4.7,4.3,2.7,4.3,2.3,5.0,2.0,1.3,3.3,2.0,3.7,4.0,2.0,3.7,3.7,3.3,3.0,4.7,5.0,3.0,4.3,2.3,3.0,5.0,2.3,4.7,2.7,4.7,2.3,4.3,2.7,4.0,4.0,3.7,3.3,2.0,4.0,3.3,3.7,3.3,3.0,5.0,2.3,4.7,3.0,2.7,2.3,3.0,4.7,4.7,2.7,2.7,4.7,4.3,2.0,4.0,3.3,4.2,4.0,2.0,3.3,4.0,2.0,3.7,3.3,4.0,3.7,3.5,1.7,4.3,2.3,5.0,2.3,5.0,2.7,3.0,4.3,5.0],[2.0,5.0,3.0,2.3,3.0,3.3,3.7,1.7,2.3,4.7,3.0,2.3,2.7,3.3,2.7,3.0,3.3,2.0,4.7,4.0,2.7,3.3,4.0,3.3,4.3,3.7,4.3,3.7,4.7,4.0,4.3,2.0,5.0,2.3,3.0,2.0,2.7,2.3,5.0,2.0,3.7,5.0,2.3,2.0,2.3,4.0,3.7,4.7,2.3,3.0,2.7,3.3,3.3,2.7,3.7,3.0,4.0,3.3,2.0,5.0,4.3,4.7,4.0,2.7,4.3,1.3,4.3,4.7,5.0,3.0,2.0,5.0,2.3,3.7,2.3,4.7,2.7,2.7,2.0,2.3,3.0,3.3,2.7,4.0,4.7,3.0,3.3,4.1,3.3,4.0,3.7,3.7,4.3,4.0,2.0,2.0,4.3,3.3,1.3,4.3,1.3,4.0,5.0,4.3,3.7,5.0,4.7,4.7]]},{\"type\":0,\"color\":\"ffff0000\",\"factor\":0.030215642846228654,\"xname\":\"zaokruzi#1.prosek.srpski\",\"yname\":\"zaokruzi#1.prosek.matematika\",\"data\":[[399,230,137,193,67,7,148,40,30,116,179,13,154,205,156,10,2,151,124,43,105,2,169,24,178,1,208,196,25,38,31,5,62,14,1251,142,87,145,117,117,64,73,235,9,210,74,22,22,109,17,84,17,119,553,102,5,97,249,152,27,159,22,22,168,2832,442,128,159,267,69,204,16,26,71,43,43,240,1,219,121,219,1,105,206,2,103,187,71,66,141,298,1,337,21,6,135,462],[2.7,4.7,4.3,2.7,5.0,2.0,3.3,3.7,2.0,4.0,3.3,2.0,4.0,3.7,3.7,3.0,2.3,4.3,2.7,3.0,5.0,2.3,4.3,3.0,5.0,2.3,4.7,4.7,4.3,4.3,2.3,5.0,2.0,3.3,2.0,3.7,4.0,2.0,3.7,3.7,3.3,3.0,4.7,5.0,3.0,4.3,2.3,5.0,3.0,2.3,4.7,2.7,4.7,2.3,4.3,2.7,4.0,4.0,3.7,3.3,4.0,3.3,3.7,3.3,5.0,3.0,4.7,2.3,3.0,2.3,3.0,4.7,4.7,2.7,2.7,4.7,4.3,2.0,4.0,3.3,4.0,2.0,3.3,4.0,2.0,3.7,3.3,3.7,4.0,4.3,5.0,2.3,5.0,2.7,3.0,4.3,5.0],[2.0,5.0,3.0,2.3,3.0,3.7,2.3,4.7,3.0,2.3,2.7,3.3,2.7,3.0,3.3,4.7,4.0,3.3,2.7,4.0,3.3,4.3,3.7,4.3,3.7,4.7,4.0,4.3,2.0,2.3,3.0,2.0,2.7,5.0,2.0,3.7,5.0,2.3,2.0,2.3,4.0,3.7,4.7,2.3,3.0,2.7,3.3,2.7,3.3,3.7,3.0,4.0,3.3,2.0,5.0,4.3,4.7,4.0,2.7,4.3,4.3,4.7,5.0,3.0,5.0,2.0,3.7,2.3,2.3,2.7,2.7,2.0,2.3,3.0,3.3,2.7,4.0,4.7,3.0,3.3,3.3,4.0,3.7,3.7,4.3,4.0,2.0,4.3,2.0,4.3,4.0,5.0,4.3,3.7,5.0,4.7,4.7]]},{\"type\":0,\"color\":\"ff0000ff\",\"factor\":1.31410123475245,\"xname\":\"zaokruzi#1.prosek.srpski\",\"yname\":\"zaokruzi#1.prosek.matematika\",\"data\":[[1,2,1,2,27,1,2,4,2,1,6,1],[4.0,4.7,4.7,4.3,5.0,4.3,4.7,5.0,4.3,4.0,5.0,5.0],[4.7,5.0,4.3,4.0,5.0,4.3,4.7,4.3,4.7,4.3,4.7,3.7]]},{\"type\":0,\"color\":\"ff00ff00\",\"factor\":1.9435348483245136,\"xname\":\"zaokruzi#1.prosek.srpski\",\"yname\":\"zaokruzi#1.prosek.matematika\",\"data\":[[1,3,2,4,1,5,1,1,6,1,2,1,2,1,2,2,1,1,3,3,2,1,1,2,6,3,1,1,5,2,3,2,1,2,1,1,1,4,2,1,3,1,1,1,3,2,1,2,3,3,6,1,6,4,2,1,2,6,2,2,4,1,2,1,1,1,5,3,2,2,1,1,3,3,2,2,1,2,1,3,1,1,10,14,15,2,1,1,1,2,1,2,1,1,4,4,1,1,2,1,2,1,3,4,1,2,2,2,2,2,1,1,2,3,1,1,5,1,3,1,5,1,1,1,3,2,2],[3.6,4.5,3.6,4.5,2.7,4.5,3.6,2.7,4.5,2.7,4.4,2.6,4.4,4.2,4.2,3.3,4.4,4.2,3.3,4.2,4.2,3.7,4.6,2.8,4.6,4.6,3.7,2.8,4.6,2.8,4.6,4.3,2.7,4.3,3.4,3.4,3.4,4.3,3.4,4.3,4.3,4.7,3.8,2.9,4.7,3.8,2.9,2.9,4.7,3.8,4.7,2.9,4.7,4.6,4.6,4.6,3.5,4.4,4.4,3.9,4.8,3.9,4.8,3.9,4.8,2.2,4.7,4.7,4.5,3.6,4.5,4.9,4.9,2.2,4.8,4.8,4.6,4.8,2.4,4.2,3.3,3.3,4.9,5.0,4.9,3.8,4.1,2.3,3.0,3.0,3.2,3.0,3.4,4.3,2.5,4.3,4.0,4.2,4.2,4.0,3.1,4.0,4.0,4.0,3.1,3.1,4.4,2.6,2.6,3.5,4.4,2.6,4.4,5.0,5.0,4.1,4.3,2.5,4.1,3.4,4.1,3.2,4.1,4.1,3.2,3.2,4.1],[3.0,4.1,3.1,4.0,2.3,4.3,3.3,2.4,4.2,2.5,4.4,2.5,4.3,3.5,3.4,2.6,4.5,3.7,2.8,3.9,3.8,3.0,4.0,2.2,4.2,4.1,3.3,2.4,4.4,2.6,4.3,3.4,2.6,3.6,2.6,2.7,2.8,3.8,2.9,3.7,3.9,4.0,3.2,2.3,4.3,3.3,2.4,2.5,4.2,3.4,4.5,2.6,4.4,4.6,4.5,4.7,2.8,3.9,3.8,3.2,4.4,3.4,4.6,3.7,4.5,2.0,4.7,4.6,3.8,2.9,3.9,4.5,4.7,2.1,4.8,4.7,3.9,4.9,2.1,4.0,3.0,3.1,4.9,5.0,4.8,2.9,4.0,2.3,2.5,2.6,3.7,2.7,3.0,4.1,2.2,4.0,3.9,4.2,4.1,3.2,2.5,3.5,3.4,3.7,2.7,2.8,4.0,2.2,2.3,3.2,4.2,2.4,4.1,4.9,4.8,3.9,4.3,2.4,3.4,3.5,3.6,2.6,3.5,3.8,2.8,2.9,3.7]]}]";

    public Result populateDb() {
        long start = System.currentTimeMillis();
        if (!INIT_PHASE) return forbidden("Init phase over");
        SmeroviBase.load();
        UceniciGroup all = new UceniciGroupBuilder(null).getGroup();
        System.out.println("Loaded everything; populating");
        Ebean.execute(() -> all.forEach(Ucenik::create));
        Ebean.execute(() -> all.forEach(Ucenik::populateZelje));
        populateSchoolAverages();
        System.out.println("Done");
        long end = System.currentTimeMillis();
        System.out.println("Time: " + ((end - start) / 1000) / 60.0 + "min");
        return ok("Errors:\n" + errors);
    }

    private void populateAveragesInner(Object s, String columnName, long id) {
        List<Ucenik> group = Ucenik.finder.where().eq(columnName, id).findList(); //change to upisana_id
        for (Method m : s.getClass().getDeclaredMethods()) {
            if (m.getName().startsWith("set") && m.getParameterTypes()[0].equals(double.class)) {
                try {
                    m.invoke(s, 1.0);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Field f : s.getClass().getFields()) {
            if (f.getType().equals(double.class)) {
                try {
                    if (Ucenik.class.getField(f.getName()).getType().equals(int.class)) {
                        f.setDouble(s, group.stream().mapToInt((Ucenik uc) -> {
                            try {
                                return Ucenik.class.getField(f.getName()).getInt(uc);
                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                throw new RuntimeException(e);
                            }
                        }).average().getAsDouble());
                    } else {
                        f.setDouble(s, group.stream().mapToDouble((Ucenik uc) -> {
                            try {
                                return Ucenik.class.getField(f.getName()).getDouble(uc);
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
        //System.out.println(s.prosekUkupno);
    }

    public Result populateSchoolAverages() {
        if (!INIT_PHASE) return forbidden("Init phase over");
        Ebean.execute(() -> {
            Smer.finder.findEach((Smer s) -> {
                populateAveragesInner(s, "upisana_id", s.id);
                s.save();
            });
            OsnovnaSkola.finder.findEach((OsnovnaSkola s) -> {
                populateAveragesInner(s, "osnovna_id", s.id);
                s.save();
            });
        });
        return ok("Hopefully this worked");
    }

    public Result getData(String query) {
        if (EXAMPLE_QUERY.equals(query)) return ok(EXAMPLE_RESULT);
        if (db == null) return internalServerError("db unavailable");
        Parser p = new Parser(query);
        ArrayNode ret = Json.newArray();
        if (DEBUG) System.out.println("got query: " + query);

        Connection conn = null;
        try {
            List<Parser.Action> acs = p.parse();

            conn = db.getConnection();
            if (conn == null) {
                return internalServerError("db connection unavailable");
            }
            for (Parser.Action ac : acs) {
                ObjectNode jsonAction = Json.newObject();
                try {
                    if (ac.isOk()) {
                        jsonAction.put("type", ac.getAction());
                        if (ac.getAction() == Parser.Action.DUMP) {
                            Ucenik uc = Ucenik.finder.where().eq("sifra", Integer.parseInt(ac.getQuery())).eq("godina", 2015).findUnique(); //todo year-agnostic
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
                            //alt formulas: (80/max)*log(100_000, size)
                            //              (1/max)*log(1.15, size)
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
                    }
                } catch (SQLException e) {
                    if (DEBUG) System.err.println("sqlex");
                    jsonAction.put("error", "SQLException: " + ac.getQuery());
                    if (DEBUG) e.printStackTrace();
                }
                ret.add(jsonAction);
            }
        } catch (Parser.ParseException e) {
            if (DEBUG) System.err.println("major parseex");
            ObjectNode errorNode = Json.newObject();
            errorNode.put("error", e.getMessage());
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
        return ok(ret);
    }

    public Result hw() {
        //return populateSchoolAverages();
        return ok("Hello world");
    }

    public Result query() {
        return ok(views.html.query.render());
    }
}

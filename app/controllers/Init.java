package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.google.inject.Inject;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import upismpn.obrada.SmeroviBase;
import upismpn.obrada.UceniciGroup;
import upismpn.obrada.UceniciGroupBuilder;
import upismpn.obrada2017.OsnovneBase;
import upismpn.obrada2017.UceniciBase;
import upismpn.obrada2017.UcenikW;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Init extends Controller {
    public static final boolean INIT_PHASE = true;


    @Inject play.db.Database db;

    private static final Class<? extends Ucenik> currentUcenik = Ucenik2017.class;
    private static final Field[] ucenikFields = currentUcenik.getFields();
    private static final Model.Finder<Long, ? extends Smer> smerFinder = Smer2017.finder;
    private static final Model.Finder<Long, ? extends OsnovnaSkola> osnovnaFinder = OsnovnaSkola2017.finder;
    private static final Model.Finder<Long, ? extends Ucenik> ucenikFinder = Ucenik2017.finder;

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
        return ok("Errors:\n" + Index.errors);
    }

    public Result populateDb2017() {
        long start = System.currentTimeMillis();
        if(!INIT_PHASE) return forbidden("Init phase over");
        System.out.println("Starting population");
        UceniciBase.load();
        System.out.println("Loaded data");
        Ebean.execute(() -> upismpn.obrada2017.OsnovneBase.getAll().forEach(OsnovnaSkola2017::create));
        System.out.println("Loaded osnovne");
        Ebean.execute(() -> upismpn.obrada2017.SmeroviBase.getAll().forEach(Smer2017::create));
        System.out.println("Loaded smerovi");
        Stream<UcenikW> svi = UceniciBase.svi();
        System.out.println("Loading ucenici...");
        Ebean.execute(() -> svi.forEach(Ucenik2017::create));
        System.out.println("Loaded ucenici. Populating averages.");

        Ebean.execute(this::populateSchoolAverages);
        System.out.println("Done");
        long end = System.currentTimeMillis();
        System.out.println("Time: " + ((end - start) / 1000) / 60.0 + "min");
        return ok("Errors:\n" + Index.errors);
    }

    //for some reason Ucenik2017 refuses to load school ids correctly, so this method is assigning them manually
    public Result setSkole() {
        System.out.println("Setting skole...");
        UceniciBase.load();
        List<UcenikW> ucenici = UceniciBase.svi().collect(Collectors.toList());

        Connection conn = db.getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("update ucenici2017 set osnovna_id=?, upisana_id=? where sifra=?");
            conn.setAutoCommit(false);
            for(UcenikW uc : ucenici) {
                stmt.setLong(1, uc.osnovna.id);
                stmt.setLong(2, Smer2017.find(CharUtils.stripAll(uc.smer.sifra)).id);
                stmt.setLong(3, uc.sifra);
                stmt.execute();
            }
            conn.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ok();
    }

    private void populateAveragesInner(Object skola, String columnName, long id) {
        List<? extends Ucenik> group = ucenikFinder.where().eq(columnName, id).findList();
        if(group.isEmpty()) return;

        for (Method m : skola.getClass().getMethods()) {
            if (m.getName().startsWith("set") && m.getParameterTypes()[0].equals(double.class)) {
                try {
                    m.invoke(skola, 1.0); //invoking setter in order to force populating fields from db
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Field f : skola.getClass().getFields()) {
            if (f.getType().equals(double.class)
                    && Utils.containsNumericField(ucenikFields, f.getName())) {
                try {
                    if (currentUcenik.getField(f.getName()).getType().equals(int.class)) {
                        f.setDouble(skola, group.stream().mapToInt(uc -> {
                            try {
                                return currentUcenik.getField(f.getName()).getInt(uc);
                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                throw new RuntimeException(e);
                            }
                        }).average().getAsDouble());
                    } else {
                        f.setDouble(skola, group.stream().mapToDouble(uc -> {
                            try {
                                return currentUcenik.getField(f.getName()).getDouble(uc);
                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                throw new RuntimeException(e);
                            }
                        }).average().getAsDouble());
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    System.err.println("Invalid field " + f.getName());
                    throw new RuntimeException(e);
                } catch (NoSuchElementException e) {
                    System.err.println("Empty field: " + f.getName());
                    throw e;
                }
            }
        }
    }

    public Result populateSchoolAverages() {
        if (!INIT_PHASE) return forbidden("Init phase over");
        Ebean.execute(() -> {
            smerFinder.findEach(s -> {
                populateAveragesInner(s, "upisana_id", s.id);
                s.save();
            });
            osnovnaFinder.findEach(s -> {
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
}

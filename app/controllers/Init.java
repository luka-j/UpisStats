package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import upismpn.obrada.OsnovneBase;
import upismpn.obrada.SmeroviBase;
import upismpn.obrada.UceniciGroup;
import upismpn.obrada.UceniciGroupBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Init extends Controller {
    public static final boolean INIT_PHASE = true;

    private static final Class<? extends Ucenik> currentUcenik = Ucenik2015.class;
    private static final Field[] ucenikFields = currentUcenik.getFields();
    private static final Model.Finder<Long, ? extends Smer> smerFinder = Smer2015.finder;
    private static final Model.Finder<Long, ? extends OsnovnaSkola> osnovnaFinder = OsnovnaSkola2015.finder;
    private static final Model.Finder<Long, ? extends Ucenik> ucenikFinder = Ucenik2015.finder;

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

    private void populateAveragesInner(Object s, String columnName, long id) {
        List<? extends Ucenik> group = ucenikFinder.where().eq(columnName, id).findList();
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
            if (f.getType().equals(double.class)
                    && Utils.containsNumericField(ucenikFields, f.getName())) {
                try {
                    if (currentUcenik.getField(f.getName()).getType().equals(int.class)) {
                        f.setDouble(s, group.stream().mapToInt(uc -> {
                            try {
                                return currentUcenik.getField(f.getName()).getInt(uc);
                            } catch (IllegalAccessException | NoSuchFieldException e) {
                                throw new RuntimeException(e);
                            }
                        }).average().getAsDouble());
                    } else {
                        f.setDouble(s, group.stream().mapToDouble(uc -> {
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

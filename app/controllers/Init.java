package controllers;

import com.google.inject.Inject;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Transaction;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import rs.lukaj.upisstats.scraper.download.DownloadController;
import rs.lukaj.upisstats.scraper.obrada.SmeroviBase;
import rs.lukaj.upisstats.scraper.obrada.UceniciGroup;
import rs.lukaj.upisstats.scraper.obrada.UceniciGroupBuilder;
import rs.lukaj.upisstats.scraper.obrada2017.OsnovneBase;
import rs.lukaj.upisstats.scraper.obrada2017.UceniciBase;
import rs.lukaj.upisstats.scraper.obrada2017.UcenikW;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Init extends Controller {
    public static final boolean INIT_PHASE = true;


    @Inject
    private play.db.Database db;

    private static final Class<? extends Ucenik> currentUcenik = Ucenik2018.class;
    private static final Class<? extends Smer> currentSmer = Smer2018.class;
    private static final Class<? extends OsnovnaSkola> currentOsnovna = OsnovnaSkola2018.class;

    private static final Field[] ucenikFields = currentUcenik.getFields();

    public Result populateDb() {
        if (!INIT_PHASE) return forbidden("Init phase over");
        long start = System.currentTimeMillis();
        SmeroviBase.load();
        UceniciGroup all = new UceniciGroupBuilder(null).getGroup();
        System.out.println("Loaded everything; populating");
        Ebean.execute(() -> all.forEach(Ucenik2016::create));
        Ebean.execute(() -> all.forEach(Ucenik2016::populateZelje));
        populateSchoolAverages();
        System.out.println("Done");
        long end = System.currentTimeMillis();
        System.out.println("Time: " + ((end - start) / 1000) / 60.0 + "min");
        return ok("Errors:\n" + Index.errors);
    }

    public Result populateDb2017() {
        if(!INIT_PHASE) return forbidden("Init phase over");
        if(UceniciBase.get(100001) != null) {
            System.out.println("Non-empty UceniciBase at the beginning! This shouldn't happen!");
            try {
                Thread.sleep(1000*60*3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long start = System.currentTimeMillis();
        System.out.println("Starting population");
        UceniciBase.load();
        System.out.println("Loaded data");
        Ebean.execute(() -> OsnovneBase.getAll().forEach(OsnovnaSkola2018::create));
        System.out.println("Loaded osnovne");
        Ebean.execute(() -> rs.lukaj.upisstats.scraper.obrada2017.SmeroviBase.getAll().forEach(Smer2018::create));
        System.out.println("Loaded smerovi");
        Stream<UcenikW> svi = UceniciBase.svi();
        System.out.println("Loading ucenici...");
        Ebean.execute(() -> svi.forEach(Ucenik2018::create));
        System.out.println("Loaded ucenici. Populating averages.");

        populateSchoolAverages();
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
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ok();
    }

    //this may look lengthy, but it's mostly exception-handling boilerplate; it's actually quite short considering what it does
    private void populateAveragesInner(Object skola, String columnName, long id) {
        List<? extends Ucenik> group = Ebean.find(currentUcenik).where().eq(columnName, id).findList();
        if(group.isEmpty()) return;

        /*for (Method m : skola.getClass().getMethods()) {
            if (m.getName().startsWith("set") && m.getParameterTypes()[0].equals(double.class)) {
                try {
                    m.invoke(skola, 1.0); //invoking setter in order to force populating fields from db,
                                                 // because ebean lazy-loads the entities
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }*/
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
        System.out.println("Populating school averages");
        EbeanServer server  = Ebean.getDefaultServer();
        /*try {
            Field config = server.getClass().getDeclaredField("serverConfig");
            config.setAccessible(true);
            ((ServerConfig)config.get(server)).setUpdateChangesOnly(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return internalServerError("Cannot access server config");
        }*/
        try {
            Transaction t = server.beginTransaction();
            t.setUpdateAllLoadedProperties(true);

            Ebean.find(currentSmer).findEach(s -> {
                populateAveragesInner(s, "upisana_id", s.id);
                s.markAsDirty();
                server.update(s, t);
            });
            t.commitAndContinue();
            System.out.println("Finished calculating smer averages");

            Ebean.find(currentOsnovna).findEach(s -> {
                populateAveragesInner(s, "osnovna_id", s.id);
                server.update(s, t);
            });
            t.commit();
            System.out.println("Finished calculating osnovne averages");
            t.close();
        } finally {
            server.endTransaction();
        }
        return ok("Done");
    }


    public Result populateZelje() {
        if (!INIT_PHASE) return forbidden("Init phase over");

        System.out.println("Starting populateZelje...");
        DownloadController.DATA_FOLDER = DownloadController.generateDataFolder("16");
        SmeroviBase.load();
        UceniciGroup all = new UceniciGroupBuilder(null).getGroup(); //todo add methods in scraper to clear
        System.out.println("Loaded 2016, populating...");
        Ebean.execute(() -> all.forEach(Ucenik2016::populateZelje));
        System.out.println("Populated 2016");

        DownloadController.DATA_FOLDER = DownloadController.generateDataFolder("17");
        UceniciBase.clear();
        UceniciBase.load();
        System.out.println("Loaded 2017, populating...");
        Ebean.execute(() -> UceniciBase.svi().forEach(Ucenik2017::populateZelje));

        System.out.println("Done");
        return ok("Done");
    }

    public Result fillInNeupisani() {
        if (!INIT_PHASE) return forbidden("Init phase over");
        OsnovneBase.load();
        System.out.println("Filling in missing data...");
        Ebean.execute(() -> Ebean.find(OsnovnaSkola2016.class).findEach(OsnovnaSkola2016::fillInNeupisani));
        return ok("Hopefully this worked");
    }

    public Result fixTakmicenja2015() {
        if (!INIT_PHASE) return forbidden("Init phase over");
        DownloadController.DATA_FOLDER = DownloadController.generateDataFolder("15");
        Ebean.execute(() -> UceniciGroup.svi().forEach(Ucenik2015::fixTakmicenja));
        return ok("This should work");
    }
}

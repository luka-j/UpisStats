package models.utils;

import controllers.CharUtils;
import rs.lukaj.upisstats.scraper.download.DownloadController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luka on 3.4.17..
 */
public class OsnovneBase {
    private static Map<String, List<Osnovna>> base;

    public static void load() {
        base = new HashMap<>();
        File f = new File(DownloadController.DATA_FOLDER, "osnovne");
        try {
            String text = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
            String[] osnovne = text.split("\\n\\$\\n");
            for(String os : osnovne) {
                Osnovna osnovna = new Osnovna(os);
                String ime = CharUtils.stripAll(osnovna.ime), okrug = CharUtils.stripAll(osnovna.okrug),
                        mesto = CharUtils.stripAll(osnovna.mesto);
                List<Osnovna> mesta = base.computeIfAbsent(ime + "+" + okrug, k -> new ArrayList<>(6));
                osnovna.mesto = mesto;
                mesta.add(osnovna);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Osnovna> getOsnovne(String ime, String okrug) {
        if(base.isEmpty()) load();
        return base.get(ime + "+" + okrug);
    }
}
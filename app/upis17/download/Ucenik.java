package upis17.download;

import upis17.Locations;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Predstavlja Ucenika u obliku pogodnom za preuzimanje s neta i cuvanje u fajl (Stringovi)
 */
public class Ucenik {

    protected static final boolean OVERWRITE_OLD = false;
    private static final boolean PRINT_MISSING = false;
    protected boolean exists = false;

    private static final String UCENICI_URL = "http://195.222.98.40/ucenik_info.php?id_ucenika=";

    public final String id; //filename

    protected String osnovnaSkola; //ime_skole
    protected String mestoOS; //ime_mesta\\
    protected String okrugOS; //ime_okruga\n

    protected Map<String, String> sestiRaz; //predmet:ocena\\predmet:ocena... \n
    protected Map<String, String> sedmiRaz; //predmet:ocena\\predmet:ocena... \n
    protected Map<String, String> osmiRaz; //predmet:ocena\\predmet:ocena... \n

    protected Map<String, String> takmicenja = new HashMap<>(); //predmet:bodova\\predmet:bodova... \n

    protected String matematika; //broj_bodova\\
    protected String srpski; //broj_bodova\\
    protected String kombinovani; //broj_bodova\n

    /**
     * !! Zajedno sa prijemnim.
     */
    protected String ukupnoBodova; //broj_bodova\n

    protected List<Skola> listaZelja; //skola_1\\skola_2\\skola_3... \n
    protected Skola upisanaSkola; //ime_skole\\
    protected String upisanaZelja; //redni_broj_u_listi_zelja
    protected String krug;

    public String getOsnovnaSkola() {
        return osnovnaSkola;
    }

    public String getMestoOS() {
        return mestoOS;
    }

    public String getOkrugOS() {
        return okrugOS;
    }

    public Map<String, String> getSestiRaz() {
        return sestiRaz;
    }

    public Map<String, String> getSedmiRaz() {
        return sedmiRaz;
    }

    public Map<String, String> getOsmiRaz() {
        return osmiRaz;
    }

    public Map<String, String> getTakmicenja() {
        return takmicenja;
    }

    public String getMatematika() {
        return matematika;
    }

    public String getSrpski() {
        return srpski;
    }

    public String getKombinovani() {
        return kombinovani;
    }

    public String getUkupnoBodova() {
        return ukupnoBodova;
    }

    public List<Skola> getListaZelja() {
        return listaZelja;
    }

    public Skola getUpisanaSkola() {
        return upisanaSkola;
    }

    public String getUpisanaZelja() {
        return upisanaZelja;
    }

    public String getKrug() {
        return krug;
    }

    public Ucenik(String id) {
        this.id = id;
        exists = new File(Locations.DATA_FOLDER, id).exists();
    }

    /**
     * Ucitava podatke o Uceniku iz fajla
     * @param folder folder u kome se nalaze podaci o ucenicima
     * @return
     */
    public Ucenik loadFromFile(File folder) {
        File f = new File(folder, id);
        try {
            String text = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
            loadFromString(text);
        } catch (IOException ex) {
            Logger.getLogger(upismpn.download.Ucenik.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    /**
     * Cuva ovog ucenika u fajl
     * @param folder folder u kom treba sacuvati ucenika
     */
    public void saveToFile(File folder) {
        if (exists && !OVERWRITE_OLD) {
            return;
        }
        File f = new File(folder, id);
        try (Writer fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
            fw.write(this.toCompactString());
        } catch (IOException ex) {
            Logger.getLogger(upismpn.download.Ucenik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadFromString(String compactString) {
        throw new UnsupportedOperationException("loading old Ucenik not supported in upis17");
    }

    public String toCompactString() {
        StringBuilder compactString = new StringBuilder();
        compactString.append(osnovnaSkola).append("\\").append(mestoOS).append("\\").append(okrugOS).append("\n");
        compactString.append(UcenikUtils.mapToStringBuilder(UcenikUtils.PredmetiDefault.compress(sestiRaz)));
        compactString.append(UcenikUtils.mapToStringBuilder(UcenikUtils.PredmetiDefault.compress(sedmiRaz)));
        compactString.append(UcenikUtils.mapToStringBuilder(UcenikUtils.PredmetiDefault.compress(osmiRaz)));
        compactString.append(UcenikUtils.mapToStringBuilder(UcenikUtils.PredmetiDefault.compress(takmicenja)));
        compactString.append(matematika).append("\\").append(srpski).append("\\").append(kombinovani).append("\n");
        compactString.append(ukupnoBodova).append("\n");
        compactString.append(UcenikUtils.listToStringBuilder(listaZelja));
        compactString.append(upisanaSkola).append("\\").append(upisanaZelja).append("\\").append(krug);
        return compactString.toString();
    }

    public static class Skola {

        public final String sifra;
        public final String ime;
        public final String mesto;
        public final String smer;

        Skola(String sifra, String ime, String mesto, String smer) {
            this.sifra = sifra;
            this.ime = ime;
            this.mesto = mesto;
            this.smer = smer;
        }

        Skola(String compactString) {
            String[] tokens = compactString.split(",");
            sifra = tokens[0];
            ime = tokens[1];
            mesto = tokens[2];
            smer = tokens[3];
        }

        @Override
        public String toString() {
            return sifra + "," + ime + "," + mesto + "," + smer;
        }
    }
}

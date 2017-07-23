package upis17.download;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by luka on 3.7.17..
 */
public class Ucenik2017 extends Ucenik {
    public Ucenik2017(String id) {
        super(id);
    }

    protected String osId;
    protected String jsonData;
    protected String bodovaAM;
    protected String blizanac, najboljiBlizanacBodovi;
    protected String maternji, prviStrani, drugiStrani;

    //naziv testa -> broj bodova
    protected Map<String, String> prijemni = new HashMap<>();
    protected List<Zelja> listaZelja1 = new ArrayList<>();
    protected List<Zelja> listaZelja2 = new ArrayList<>();

    public String getOsId() {
        return osId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public String getBodovaAM() {
        return bodovaAM;
    }

    public String getBlizanac() {
        return blizanac;
    }

    public String getNajboljiBlizanacBodovi() {
        return najboljiBlizanacBodovi;
    }

    public String getMaternji() {
        return maternji;
    }

    public String getPrviStrani() {
        return prviStrani;
    }

    public String getDrugiStrani() {
        return drugiStrani;
    }

    public Map<String, String> getPrijemni() {
        return prijemni;
    }

    public List<Zelja> getListaZelja1() {
        return listaZelja1;
    }

    public List<Zelja> getListaZelja2() {
        return listaZelja2;
    }

    public String getUpisana() {
        return upisana;
    }

    protected String upisana;

    public Ucenik setDetails(String ukBodova, String krug) {
        ukupnoBodova = ukBodova;
        this.krug = krug;
        return this;
    }

    public static class Zelja {
        private String sifraSmera, uslov;

        public Zelja(String sifraSmera, String uslov) {
            this.sifraSmera = sifraSmera;
            this.uslov = uslov;
        }
        public Zelja(String compactString) {
            String[] tokens = compactString.split(",");
            sifraSmera = tokens[0];
            uslov = tokens[1];
        }
        public String getSifraSmera() {
            return sifraSmera;
        }
        public String getUslov() {
            return uslov;
        }

        @Override
        public String toString() {
            return sifraSmera + "," + uslov;
        }
    }

    @Override
    public String toCompactString() {
        StringBuilder sb = new StringBuilder();
        sb.append(osId).append("\\").append(upisana).append("\\").append(krug).append("\\").append(blizanac).append("\\").append(najboljiBlizanacBodovi).append("\n");
        sb.append(srpski).append("\\").append(matematika).append("\\").append(kombinovani).append("\\").append(bodovaAM).append("\\").append(ukupnoBodova).append("\n");
        sb.append(maternji).append("\\").append(prviStrani).append("\\").append(drugiStrani).append("\n");
        sb.append(UcenikUtils.mapToStringBuilder(UcenikUtils.PredmetiDefault.compress(sestiRaz)));
        sb.append(UcenikUtils.mapToStringBuilder(UcenikUtils.PredmetiDefault.compress(sedmiRaz)));
        sb.append(UcenikUtils.mapToStringBuilder(UcenikUtils.PredmetiDefault.compress(osmiRaz)));
        sb.append(UcenikUtils.mapToStringBuilder(takmicenja));
        sb.append(UcenikUtils.mapToStringBuilder(prijemni));
        sb.append(UcenikUtils.listToStringBuilder(listaZelja1));
        sb.append(UcenikUtils.listToStringBuilder(listaZelja2));
        return sb.toString();
    }

    @Override
    public void loadFromString(String compactString) {
        String[] chunks = compactString.split("\n", -1);

        String[] basics = chunks[0].split("\\\\", -1);
        String[] bodovi = chunks[1].split("\\\\");
        String[] jezici = chunks[2].split("\\\\", -1);
        String[] sesti = chunks[3].split("\\\\", 0);
        String[] sedmi = chunks[4].split("\\\\", 0);
        String[] osmi = chunks[5].split("\\\\", 0);
        String[] takmicenja = chunks[6].split("\\\\", 0);
        String[] prijemni = chunks[7].split("\\\\", 0);
        String[] zelje1 = chunks[8].split("\\\\", 0);
        String[] zelje2 = chunks[9].split("\\\\", 0);

        osId = basics[0];
        upisana = basics[1];
        krug = basics[2];
        blizanac = basics[3];
        najboljiBlizanacBodovi = basics[4];
        srpski = bodovi[0];
        matematika = bodovi[1];
        kombinovani = bodovi[2];
        bodovaAM = bodovi[3];
        ukupnoBodova = bodovi[4];
        maternji = jezici[0];
        prviStrani = jezici[1];
        drugiStrani = jezici[2];

        sestiRaz = UcenikUtils.PredmetiDefault.decompress(UcenikUtils.stringArrayToMap(sesti));
        sedmiRaz = UcenikUtils.PredmetiDefault.decompress(UcenikUtils.stringArrayToMap(sedmi));
        osmiRaz = UcenikUtils.PredmetiDefault.decompress(UcenikUtils.stringArrayToMap(osmi));
        this.takmicenja = UcenikUtils.stringArrayToMap(takmicenja);
        this.prijemni = UcenikUtils.stringArrayToMap(prijemni);
        listaZelja1 = UcenikUtils.stringToListZelja(zelje1);
        listaZelja2 = UcenikUtils.stringToListZelja(zelje2);
    }

    @Override
    public void saveToFile(File folder) {
        super.saveToFile(folder);
        if(exists && !OVERWRITE_OLD) return;
        File f = new File(folder, id + ".json");
        try (Writer fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
            fw.write(jsonData);
        } catch (IOException ex) {
            Logger.getLogger(Ucenik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

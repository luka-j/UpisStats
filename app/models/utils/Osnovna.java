package models.utils;

public class Osnovna {
    final String ime;
    String mesto;
    final String okrug;

    public String getIme() {
        return ime;
    }

    public String getMesto() {
        return mesto;
    }

    public String getOkrug() {
        return okrug;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public double getMat6() {
        return mat6;
    }

    public double getMat7() {
        return mat7;
    }

    public double getMat8() {
        return mat8;
    }

    public double getSrp6() {
        return srp6;
    }

    public double getSrp7() {
        return srp7;
    }

    public double getSrp8() {
        return srp8;
    }

    public int getBrojUcenikaUkupno() {
        return brojUcenikaUkupno;
    }

    public double getMatp() {
        return matp;
    }

    public double getSrpp() {
        return srpp;
    }

    public int getId() {
        return id;
    }

    final int id;
    final String adresa;
    final String telefon;
    final double mat6, mat7, mat8, srp6, srp7, srp8;
    final int brojUcenikaUkupno;
    final double matp, srpp;

    public Osnovna(String compactString) {
        String[] parts = compactString.split("\\n");
        String[] info = parts[0].split("\\\\", -1);
        String[] ocene = parts[1].split("\\\\");
        id = Integer.parseInt(info[0]);
        ime = info[1];
        mesto = info[2];
        okrug = info[3];
        brojUcenikaUkupno = info[4].isEmpty() ? 0 : Integer.parseInt(info[4]);
        adresa = info[5];
        telefon = info[6];
        mat6 = Double.parseDouble(ocene[0]);
        mat7 = Double.parseDouble(ocene[1]);
        mat8 = Double.parseDouble(ocene[2]);
        srp6 = Double.parseDouble(ocene[3]);
        srp7 = Double.parseDouble(ocene[4]);
        srp8 = Double.parseDouble(ocene[5]);
        matp = (mat6+mat7+mat8)/3;
        srpp = (srp6+srp7+srp8)/3;
    }
}

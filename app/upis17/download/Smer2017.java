package upis17.download;

/**
 * Created by luka on 2.7.17.
 */
public class Smer2017 extends Smer {
    protected String ime;
    protected String jezik;
    protected String opstina;
    protected String okrug;
    protected String podrucje;
    protected String trajanje;
    protected String kvotaUmanjenje;

    protected String json;



    public String getIme() {
        return ime;
    }

    public String getJezik() {
        return jezik;
    }

    public String getOpstina() {
        return opstina;
    }

    public String getOkrug() {
        return okrug;
    }

    public String getTrajanje() {
        return trajanje;
    }

    //let's pretend this isn't godawful
    //well this ain't supposed to look pretty, it's supposed to work fast
    //will worry about that in Exec/obrada
    public String getPodrucje2017() {
        return podrucje;
    }

    public String getJson() {
        return json;
    }

    public Smer2017(String sifra, String ime, String smer, String jezik, String kvota) {
        super(sifra, smer, kvota);
        this.ime = ime;
        this.jezik = jezik;
    }

    public Smer2017(String compactString) {
        super(compactString);
        String[] tokens = compactString.split("\\\\");
        ime = tokens[3];
        trajanje = tokens[4];
        kvotaUmanjenje = tokens[5];
        jezik = tokens[6];
        opstina = tokens[7];
        okrug = tokens[8];
        podrucje = tokens[9];
    }

    public String toCompactString() {
        StringBuilder str = new StringBuilder(super.toCompactString());
        str.deleteCharAt(str.length()-1); //removing newline
        if(ime.endsWith(",")) ime = ime.substring(0, ime.length()-1);
        str.append("\\").append(ime).append("\\").append(trajanje).append("\\")
                .append(kvotaUmanjenje).append("\\").append(jezik.trim()).append("\\")
                .append(opstina).append("\\").append(okrug).append("\\").append(podrucje).append("\n");
        return str.toString();
    }

    public String getKvotaUmanjenje() {
        return kvotaUmanjenje;
    }
}

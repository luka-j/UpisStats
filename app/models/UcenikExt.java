package models;

import controllers.CharUtils;
import controllers.Index;
import rs.lukaj.upisstats.scraper.obrada2017.UcenikW;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Map;

@MappedSuperclass
public class UcenikExt extends Ucenik {
    public int drugiMaternji6, drugiMaternji7, drugiMaternji8;  //todo fix: this isn't loaded
    public double drugiMaternjiP;
    public double najboljiBlizanacBodovi;
    public int blizanacSifra;
    @Column(name = "bodova_am")
    public double bodovaAM;
    public String maternjiJezik, prviStraniJezik;
    public boolean vukovaDiploma, prioritet;

    public static <T extends UcenikExt> T fillIn(T uc, UcenikW from) {
        //System.out.println("Loading " + from.sifra);
        if (Ucenik.fillIn(uc, from) == null) return null;
        try {
            populateGrades2017(from.sestiRaz.ocene, uc, 6);
            populateGrades2017(from.sedmiRaz.ocene, uc, 7);
            populateGrades2017(from.osmiRaz.ocene, uc, 8);
            Ucenik.populateAverages(uc);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Exception while populating: invalid field(s) for uc" + uc.sifra + ": " + e.getMessage());
            Index.errors(uc.sifra);
            return null;
        }

        uc.najboljiBlizanacBodovi = from.najboljiBlizanacBodovi;
        uc.blizanacSifra = from.blizanacSifra;
        uc.bodovaAM = from.bodovaAM;
        uc.maternjiJezik = CharUtils.stripAll(from.maternji);
        uc.prviStraniJezik = CharUtils.stripAll(from.prviStrani);
        uc.vukovaDiploma = from.vukovaDiploma;
        uc.prioritet = from.prioritet;

        return uc;
    }



    private static void populateGrades2017(Map<String, Integer> from, UcenikExt to, int raz) throws NoSuchFieldException, IllegalAccessException {
        Class<?> c = to.getClass();
        for (Map.Entry<String, Integer> e : from.entrySet()) {
            switch (e.getKey()) {
                case "izbornisport":
                case "izborniSport":
                    c.getField("sport" + raz).setInt(to, e.getValue());
                    break;
                case "prviStrani":
                    c.getField("engleski" + raz).setInt(to, e.getValue());
                    break;
                case "maternjiJezik":
                    c.getField("srpski" + raz).setInt(to, e.getValue());
                    break;
                case "drugiMaternjiJezik":
                    c.getField("drugiMaternji" + raz).setInt(to, e.getValue());
                    break;
                default:
                    if (e.getKey().isEmpty()) {
                        System.err.println("Empty grade @ uc" + to.sifra);
                        Index.errors(to.sifra);
                    } else {
                        c.getField(e.getKey() + raz).setInt(to, e.getValue());
                    }
                    break;
            }
        }
    }
}

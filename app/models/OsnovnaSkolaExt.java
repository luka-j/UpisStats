package models;

import controllers.CharUtils;
import rs.lukaj.upisstats.scraper.obrada2017.OsnovnaW;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class OsnovnaSkolaExt extends OsnovnaSkola {
    public String opstina;
    public int ukupnoUcenika, svrsenihUcenika, nesvrsenihUcenika, vukovaca, nagradjenih;
    public double sviBodova6, sviBodova7, sviBodova8, sviProsek6, sviProsek7, sviProsek8;
    public double sviBodovaUkupno, sviBodovaZavrsni, sviBodovaOcene, sviProsekUkupno;

    public double drugiMaternji6, drugiMaternji7, drugiMaternji8, drugiMaternjiP;

    public static <T extends OsnovnaSkolaExt> T fillIn(T dest, OsnovnaW src) {
        dest.id = src.id;
        dest.ime = CharUtils.stripAll(src.naziv);
        dest.mesto = CharUtils.stripAll(src.sediste);
        dest.okrug = CharUtils.stripAll(src.okrug);
        dest.brojUcenika = 0;
        dest.opstina = CharUtils.stripAll(src.opstina);
        dest.ukupnoUcenika = src.brojUcenika;
        dest.svrsenihUcenika = src.ucenikaZavrsilo;
        dest.vukovaca = src.vukovaca;
        dest.nagradjenih = src.nagradjenih;
        dest.nesvrsenihUcenika = src.nijeZavrsilo;
        dest.sviBodova6 = src.bodova6;
        dest.sviBodova7 = src.bodova7;
        dest.sviBodova8 = src.bodova8;
        dest.sviProsek6 = src.prosek6;
        dest.sviProsek7 = src.prosek7;
        dest.sviProsek8 = src.prosek8;
        dest.sviBodovaUkupno = src.ukupnoBodova;
        dest.sviBodovaZavrsni = src.bodovaZavrsni;
        dest.sviBodovaOcene = src.bodovaOcene;
        dest.sviProsekUkupno = src.prosecnaOcena;

        dest.save();
        return dest;
    }

    public void addUcenik() {
        brojUcenika++;
        update();
    }
}

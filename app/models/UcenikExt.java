package models;

import controllers.CharUtils;
import rs.lukaj.upisstats.scraper.obrada2017.UcenikW;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class UcenikExt extends Ucenik {
    public int drugiMaternji6, drugiMaternji7, drugiMaternji8;
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

        uc.najboljiBlizanacBodovi = from.najboljiBlizanacBodovi;
        uc.blizanacSifra = from.blizanacSifra;
        uc.bodovaAM = from.bodovaAM;
        uc.maternjiJezik = CharUtils.stripAll(from.maternji);
        uc.prviStraniJezik = CharUtils.stripAll(from.prviStrani);
        uc.vukovaDiploma = from.vukovaDiploma;
        uc.prioritet = from.prioritet;

        return uc;
    }
}

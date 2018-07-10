package models;

import controllers.CharUtils;
import rs.lukaj.upisstats.scraper.obrada2017.SmerW;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class SmerExt extends Smer {

    public String jezik;
    public int trajanje, kvotaUmanjenje;
    @Column(name = "upisano_1k")
    public int upisano1k;
    @Column(name = "upisano_2k")
    public int upisano2k;
    @Column(name = "kvota_2k")
    public int kvota2k;
    @Column(name = "min_bodova_1k")
    public double minBodova1k;
    @Column(name = "min_bodova_2k")
    public double minBodova2k;

    public double drugiMaternji6, drugiMaternji7, drugiMaternji8, drugiMaternjiP;

    public static <T extends SmerExt> T fillIn(T s, SmerW smer) {
        s.sifra = CharUtils.stripAll(smer.sifra);
        s.ime = CharUtils.stripAll(smer.skola);
        s.mesto = CharUtils.stripAll(smer.opstina);
        s.okrug = CharUtils.stripAll(smer.okrug);
        s.smer = CharUtils.stripAll(smer.smer);
        s.podrucje = CharUtils.stripAll(smer.podrucje);
        s.kvota = smer.kvota;
        s.brojUcenika = 0;
        s.jezik = CharUtils.stripAll(smer.jezik);
        s.trajanje = smer.trajanje;
        s.kvotaUmanjenje = smer.kvotaUmanjenje;
        s.upisano1k = smer.upisano1k;
        s.upisano2k = smer.upisano2k;
        s.kvota2k = smer.kvota2k;
        s.minBodova1k = smer.minBodova1k;
        s.minBodova2k = smer.minBodova2k;
        s.save();
        return s;
    }

    public void addUcenik() {
        brojUcenika++;
        update();
    }
}

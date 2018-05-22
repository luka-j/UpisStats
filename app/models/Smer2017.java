package models;

import controllers.CharUtils;
import io.ebean.Ebean;
import rs.lukaj.upisstats.scraper.obrada2017.SmerW;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="smerovi2017")
public class Smer2017 extends Smer {

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

    public static Smer2017 create(SmerW smer) {
        String sifra = CharUtils.stripAll(smer.sifra);
        Smer2017 s = find(sifra);
        if (s == null) {
            s = new Smer2017();
            s.sifra = sifra;
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
        }
        return s;
    }

    public static Smer2017 find(String sifra) {
        return Ebean.find(Smer2017.class).where().eq("sifra", CharUtils.stripAll(sifra)).findOne();
    }

    public void addUcenik() {
        brojUcenika++;
        update();
    }
}

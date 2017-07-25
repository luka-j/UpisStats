package models;

import com.avaje.ebean.Model;
import controllers.CharUtils;
import upis17.data.SmerWrapper;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="smerovi2017")
public class Smer2017 extends Smer {

    public static Finder<Long, Smer2017> finder = new Model.Finder<>(Smer2017.class);
    public String jezik;
    public int trajanje, kvotaUmanjenje;

    public double drugiMaternji6, drugiMaternji7, drugiMaternji8, drugiMaternjiP;

    public static Smer2017 create(SmerWrapper smer) {
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
            s.save();
        }
        return s;
    }

    public static Smer2017 find(String sifra) {
        return finder.where().eq("sifra", sifra).findUnique();
    }

    public void addUcenik() {
        brojUcenika++;
        update();
    }
}

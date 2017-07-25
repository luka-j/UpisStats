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
        Smer2017 res = find(sifra);
        if (res == null) {
            Smer2017 s = new Smer2017();
            Smer.create(s, sifra, smer.skola, smer.opstina, smer.okrug, smer.smer, smer.podrucje, smer.kvota);
            s.jezik = CharUtils.stripAll(smer.jezik);
            s.trajanje = smer.trajanje;
            s.kvotaUmanjenje = smer.kvotaUmanjenje;
            s.update();
            return s;
        }
        res.brojUcenika++;
        res.update();
        return res;
    }

    public static Smer2017 find(String sifra) {
        return finder.where().eq("sifra", sifra).findUnique();
    }
}

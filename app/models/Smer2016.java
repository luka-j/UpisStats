package models;

import controllers.CharUtils;
import io.ebean.Ebean;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by luka on 26.6.16..
 */
@Entity
@Table(name = "smerovi2016")
public class Smer2016 extends Smer {

    public static Smer2016 create(String sifra, String ime, String mesto, String okrug, String smer, String podrucje, int kvota) {
        sifra = CharUtils.stripAll(sifra);
        Smer2016 res = Ebean.find(Smer2016.class).where().eq("sifra", sifra).findOne();
        if (res == null) {
            Smer2016 s = new Smer2016();
            Smer.create(s, sifra, ime, mesto, okrug, smer, podrucje, kvota);
            return s;
        }
        res.brojUcenika++;
        res.update();
        return res;
    }
}

package models;

import com.avaje.ebean.Model;
import controllers.CharUtils;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by luka on 26.6.16..
 */
@Entity
@Table(name = "smerovi2016")
public class Smer2016 extends Smer {

    public static Finder<Long, Smer2016> finder = new Model.Finder<>(Smer2016.class);

    public static Smer2016 create(String sifra, String ime, String mesto, String okrug, String smer, String podrucje, int kvota) {
        sifra = CharUtils.stripAll(sifra);
        Smer2016 res = finder.where().eq("sifra", sifra).findUnique();
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

package models;

import com.avaje.ebean.Model;
import controllers.CharUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by luka on 26.6.16..
 */
@Entity
@Table(name = "smerovi2015")
public class Smer2015 extends Smer {

    public static Finder<Long, Smer2015> finder = new Model.Finder<>(Smer2015.class);

    public static Smer2015 create(String sifra, String ime, String mesto, String okrug, String smer, String podrucje, int kvota) {
        sifra = CharUtils.stripAll(sifra);
        List<Smer2015> res = finder.where().eq("sifra", sifra).findList();
        if (res.size() == 0) {
            Smer2015 s = new Smer2015();
            Smer.create(s, sifra, ime, mesto, okrug, smer, podrucje, kvota);
            return s;
        }
        Smer2015 s = res.get(0);
        s.brojUcenika++;
        s.update();
        return res.get(0);
    }
}

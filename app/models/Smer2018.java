package models;

import controllers.CharUtils;
import io.ebean.Ebean;
import rs.lukaj.upisstats.scraper.obrada2017.SmerW;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="smerovi2018")
public class Smer2018 extends SmerExt {

    public static Smer2018 create(SmerW smer) {
        String sifra = CharUtils.stripAll(smer.sifra);
        Smer2018 s = find(sifra);
        if (s == null) {
            s = new Smer2018();
            SmerExt.fillIn(s, smer);
        }
        return s;
    }

    public static Smer2018 find(String sifra) {
        return Ebean.find(Smer2018.class).where().eq("sifra", CharUtils.stripAll(sifra)).findOne();
    }
}

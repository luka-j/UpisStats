package models;

import io.ebean.Ebean;
import rs.lukaj.upisstats.scraper.obrada2017.OsnovnaW;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="os2018")
public class OsnovnaSkola2018 extends OsnovnaSkolaExt {

    public static OsnovnaSkola2018 create(OsnovnaW skola) {
        OsnovnaSkola2018 os = Ebean.find(OsnovnaSkola2018.class, (long)skola.id);
        if (os == null) {
            os = new OsnovnaSkola2018();
            OsnovnaSkolaExt.fillIn(os, skola);
        }
        return os;
    }
}

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
@Table(name = "os2015")
public class OsnovnaSkola2015 extends OsnovnaSkola {

    public static Finder<Long, OsnovnaSkola2015> finder = new Model.Finder<>(OsnovnaSkola2015.class);

    public static OsnovnaSkola2015 create(String ime, String mesto, String okrug) {
        ime = CharUtils.stripAll(ime);
        mesto = CharUtils.stripAll(mesto);
        okrug = CharUtils.stripAll(okrug);
        List<OsnovnaSkola2015> res = finder.where().eq("ime", ime).eq("mesto", mesto).eq("okrug", okrug).findList();
        if (res.size() == 0) {
            OsnovnaSkola2015 os = new OsnovnaSkola2015();
            OsnovnaSkola.create(os, ime, mesto, okrug);
            return os;
        }
        OsnovnaSkola2015 os = res.get(0);
        os.brojUcenika++;
        os.update();
        return os;
    }
}

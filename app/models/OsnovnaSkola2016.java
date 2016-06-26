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
@Table(name = "os2016")
public class OsnovnaSkola2016 extends OsnovnaSkola {

    public static Model.Finder<Long, OsnovnaSkola2016> finder = new Model.Finder<>(OsnovnaSkola2016.class);

    public static OsnovnaSkola2016 create(String ime, String mesto, String okrug) {
        ime = CharUtils.stripAll(ime);
        mesto = CharUtils.stripAll(mesto);
        okrug = CharUtils.stripAll(okrug);
        List<OsnovnaSkola2016> res = finder.where().eq("ime", ime).eq("mesto", mesto).eq("okrug", okrug).findList();
        if (res.size() == 0) {
            OsnovnaSkola2016 os = new OsnovnaSkola2016();
            OsnovnaSkola.create(os, ime, mesto, okrug);
            return os;
        }
        OsnovnaSkola2016 os = res.get(0);
        os.brojUcenika++;
        os.update();
        return os;
    }
}

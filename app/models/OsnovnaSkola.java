package models;

import com.avaje.ebean.Model;
import controllers.CharUtils;
import controllers.Index;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by luka on 5.5.16..
 */
@Entity
@Table(name = "os")
public class OsnovnaSkola extends Model {
    public int godina;

    @Id
    public long id;

    public String ime;
    public String mesto;
    public String okrug;

    public double likovno6, tehnicko6, geografija6, biologija6, sport6, drugiStrani6, matematika6, istorija6, engleski6, muzicko6, fizicko6, vladanje6, fizika6, srpski6;
    public double likovno7, tehnicko7, geografija7, biologija7, sport7, drugiStrani7, matematika7, istorija7, engleski7, muzicko7, fizicko7, vladanje7, fizika7, srpski7, hemija7;
    public double likovno8, tehnicko8, geografija8, biologija8, sport8, drugiStrani8, matematika8, istorija8, engleski8, muzicko8, fizicko8, vladanje8, fizika8, srpski8, hemija8;
    public double likovnoP, tehnickoP, geografijaP, biologijaP, sportP, drugiStraniP, matematikaP, istorijaP, engleskiP, muzickoP, fizickoP, vladanjeP, fizikaP, srpskiP, hemijaP;
    public double prosekSesti, prosekSedmi, prosekOsmi, prosekUkupno;
    public double matematika, srpski, kombinovani;
    public double bodoviIzSkole, bodoviSaZavrsnog, bodoviUkupno, bodoviSaPrijemnog;

    public static Finder<Long, OsnovnaSkola> finder = new Model.Finder<>(OsnovnaSkola.class);

    public static OsnovnaSkola create(String ime, String mesto, String okrug) {
        List<OsnovnaSkola> res = finder.where().eq("ime", ime).eq("mesto", mesto).eq("okrug", okrug).findList();
        if (res.size() == 0) {
            OsnovnaSkola os = new OsnovnaSkola();
            os.godina = Index.CURRENT_YEAR;
            os.ime = CharUtils.stripAll(ime);
            os.mesto = CharUtils.stripAll(mesto);
            os.okrug = CharUtils.stripAll(okrug);
            os.save();
            return os;
        }
        return res.get(0);
    }
}

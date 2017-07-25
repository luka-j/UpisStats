package models;

import com.avaje.ebean.Model;
import controllers.CharUtils;
import upis17.data.OsnovnaWrapper;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="os2017")
public class OsnovnaSkola2017 extends OsnovnaSkola {
    public static Finder<Long, OsnovnaSkola2017> finder = new Model.Finder<>(OsnovnaSkola2017.class);

    public String opstina;
    public int ukupnoUcenika, svrsenihUcenika, nesvrsenihUcenika, vukovaca, nagradjenih;
    public double sviBodova6, sviBodova7, sviBodova8, sviProsek6, sviProsek7, sviProsek8;
    public double sviBodovaUkupno, sviBodovaZavrsni, sviBodovaOcene, sviProsekUkupno;

    public double drugiMaternji6, drugiMaternji7, drugiMaternji8, drugiMaternjiP;

    public static OsnovnaSkola2017 create(OsnovnaWrapper skola) {
        OsnovnaSkola2017 os = finder.byId((long)skola.id);
        if (os == null) {
            os = new OsnovnaSkola2017();
            OsnovnaSkola.create(os, skola.naziv, skola.sediste, skola.okrug);
            os.opstina = CharUtils.stripAll(skola.opstina);
            os.ukupnoUcenika = skola.brojUcenika;
            os.svrsenihUcenika = skola.ucenikaZavrsilo;
            os.vukovaca = skola.vukovaca;
            os.nagradjenih = skola.nagradjenih;
            os.nesvrsenihUcenika = skola.nijeZavrsilo;
            os.sviBodova6 = skola.bodova6;
            os.sviBodova7 = skola.bodova7;
            os.sviBodova8 = skola.bodova8;
            os.sviProsek6 = skola.prosek6;
            os.sviProsek7 = skola.prosek7;
            os.sviProsek8 = skola.prosek8;
            os.sviBodovaUkupno = skola.ukupnoBodova;
            os.sviBodovaZavrsni = skola.bodovaZavrsni;
            os.sviBodovaOcene = skola.bodovaOcene;
            os.sviProsekUkupno = skola.prosecnaOcena;

            os.update();
            return os;
        }
        os.brojUcenika++;
        os.update();
        return os;
    }
}

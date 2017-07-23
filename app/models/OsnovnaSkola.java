package models;

import com.avaje.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by luka on 5.5.16..
 */
@MappedSuperclass
public class OsnovnaSkola extends Model {
    @Id
    public long id;

    public int brojUcenika;

    public String ime;
    public String mesto;
    public String okrug;
    public String adresa, telefon;

    public double likovno6, tehnicko6, geografija6, biologija6, sport6, drugiStrani6, matematika6, istorija6, engleski6, muzicko6, fizicko6, vladanje6, fizika6, srpski6;
    public double likovno7, tehnicko7, geografija7, biologija7, sport7, drugiStrani7, matematika7, istorija7, engleski7, muzicko7, fizicko7, vladanje7, fizika7, srpski7, hemija7;
    public double likovno8, tehnicko8, geografija8, biologija8, sport8, drugiStrani8, matematika8, istorija8, engleski8, muzicko8, fizicko8, vladanje8, fizika8, srpski8, hemija8;
    public double likovnoP, tehnickoP, geografijaP, biologijaP, sportP, drugiStraniP, matematikaP, istorijaP, engleskiP, muzickoP, fizickoP, vladanjeP, fizikaP, srpskiP, hemijaP;
    public double prosekSesti, prosekSedmi, prosekOsmi, prosekUkupno;
    public double matematika, srpski, kombinovani;
    public double bodoviIzSkole, bodoviSaZavrsnog, bodoviUkupno, bodoviSaPrijemnog;

    protected static OsnovnaSkola create(OsnovnaSkola os, String ime, String mesto, String okrug) {
        os.ime = ime;
        os.mesto = mesto;
        os.okrug = okrug;
        os.brojUcenika = 1;
        os.save();
        return os;
    }
}

package models;

import controllers.CharUtils;
import controllers.Index;
import controllers.Utils;
import io.ebean.Model;
import rs.lukaj.upisstats.scraper.obrada.UcenikWrapper;
import rs.lukaj.upisstats.scraper.obrada2017.UcenikW;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by luka on 5.5.16..
 */
@MappedSuperclass
public class Ucenik extends Model {
    @Id
    public long id;

    @Column(unique = true)
    public int sifra;

    public String drugiStraniJezik;
    public int likovno6, tehnicko6, geografija6, biologija6, sport6, drugiStrani6, matematika6, istorija6, engleski6, muzicko6, fizicko6, vladanje6, fizika6, srpski6;
    public int likovno7, tehnicko7, geografija7, biologija7, sport7, drugiStrani7, matematika7, istorija7, engleski7, muzicko7, fizicko7, vladanje7, fizika7, srpski7, hemija7;
    public int likovno8, tehnicko8, geografija8, biologija8, sport8, drugiStrani8, matematika8, istorija8, engleski8, muzicko8, fizicko8, vladanje8, fizika8, srpski8, hemija8;
    public double likovnoP, tehnickoP, geografijaP, biologijaP, sportP, drugiStraniP, matematikaP, istorijaP, engleskiP, muzickoP, fizickoP, vladanjeP, fizikaP, srpskiP, hemijaP;

    public double prosekSesti, prosekSedmi, prosekOsmi, prosekUkupno;
    public double matematika, srpski, kombinovani;
    public double bodoviIzSkole, bodoviSaZavrsnog, bodoviUkupno, bodoviSaPrijemnog, bodoviSaTakmicenja;
    public int brojZelja, upisanaZelja, krug;

    protected static Ucenik create(Ucenik uc, UcenikWrapper from) {
        uc.sifra = from.id;
        uc.prosekSesti = from.prosekSesti;
        uc.prosekSedmi = from.prosekSedmi;
        uc.prosekOsmi = from.prosekOsmi;
        uc.prosekUkupno = from.prosekUkupno;
        uc.matematika = from.matematika;
        uc.srpski = from.srpski;
        uc.kombinovani = from.kombinovani;
        uc.bodoviIzSkole = from.bodoviIzSkole;
        uc.bodoviSaZavrsnog = from.bodoviSaZavrsnog;
        uc.bodoviUkupno = from.ukupnoBodova;
        uc.bodoviSaPrijemnog = from.bodoviSaPrijemnog;
        uc.bodoviSaTakmicenja = uc.bodoviUkupno - uc.bodoviIzSkole - uc.bodoviSaPrijemnog - uc.bodoviSaZavrsnog;
        uc.brojZelja = from.brojZelja;
        uc.upisanaZelja = from.upisanaZelja;
        uc.krug = from.krug;

        try {
            populateGrades(from.sestiRaz, uc, 6);
            populateGrades(from.sedmiRaz, uc, 7);
            populateGrades(from.osmiRaz, uc, 8);
            populateAverages(uc);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Exception while populating: invalid field(s) for uc" + uc.sifra);
            Index.errors(uc.sifra);
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Nepostojeci drugi strani jezik, uc" + uc.sifra);
            e.printStackTrace();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Index.errors(uc.sifra);
            return null;
        }
        return uc;
    }

    protected static Ucenik create(Ucenik uc, UcenikW from) {
        uc.sifra = from.sifra;
        uc.prosekSesti = from.sestiRaz.prosekOcena;
        uc.prosekSedmi = from.sedmiRaz.prosekOcena;
        uc.prosekOsmi = from.osmiRaz.prosekOcena;
        uc.prosekUkupno = from.prosekUkupno;
        uc.matematika = from.matematika;
        uc.srpski = from.srpski;
        uc.kombinovani = from.kombinovani;
        uc.bodoviIzSkole = from.bodoviOcene;
        uc.bodoviSaZavrsnog = from.bodovaZavrsni;
        uc.bodoviUkupno = from.ukupnoBodova;
        //uc.bodoviSaPrijemnog = from.bodovaPrijemni; this is impossible (or rather quite hard to do reliably). sorry
        uc.bodoviSaTakmicenja = from.bodovaTakmicenja;
        uc.drugiStraniJezik = from.drugiStrani;
        if(from.krug == 1) uc.brojZelja = from.listaZelja1.size();
        else if(from.krug == 2) uc.brojZelja = from.listaZelja2.size();
        else uc.brojZelja = 0;
        uc.upisanaZelja = from.upisanaZelja;
        uc.krug = from.krug;

        try {
            populateGrades2017(from.sestiRaz.ocene, uc, 6);
            populateGrades2017(from.sedmiRaz.ocene, uc, 7);
            populateGrades2017(from.osmiRaz.ocene, uc, 8);
            Ucenik.populateAverages(uc);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Exception while populating: invalid field(s) for uc" + uc.sifra + ": " + e.getMessage());
            Index.errors(uc.sifra);
            return null;
        }

        return uc;
    }


    private static void populateGrades2017(Map<String, Integer> from, Ucenik to, int raz) throws NoSuchFieldException, IllegalAccessException {
        Class<?> c = to.getClass();
        for (Map.Entry<String, Integer> e : from.entrySet()) {
            switch (e.getKey()) {
                case "izbornisport":
                case "izborniSport":
                    c.getField("sport" + raz).setInt(to, e.getValue());
                    break;
                case "prviStrani":
                    c.getField("engleski" + raz).setInt(to, e.getValue());
                    break;
                case "maternjiJezik":
                    c.getField("srpski" + raz).setInt(to, e.getValue());
                    break;
                case "drugiMaternjiJezik":
                    c.getField("drugiMaternji" + raz).setInt(to, e.getValue());
                    break;
                default:
                    if (e.getKey().isEmpty()) {
                        System.err.println("Empty grade @ uc" + to.sifra);
                        Index.errors(to.sifra);
                    } else {
                        c.getField(e.getKey() + raz).setInt(to, e.getValue());
                    }
                    break;
            }
        }
    }


    public static void populateAverages(Ucenik of) throws NoSuchFieldException, IllegalAccessException {
        for (Field f : Ucenik.class.getDeclaredFields()) {
            if (f.getName().endsWith("P")) {
                String subject = f.getName().substring(0, f.getName().length() - 1);
                int r6=0, r7, r8;
                r7 = Ucenik.class.getField(subject + "7").getInt(of);
                r8 = Ucenik.class.getField(subject + "8").getInt(of);
                if (!subject.equals("hemija"))
                    r6 = Ucenik.class.getField(subject + "6").getInt(of);

                int nonzero = 3 - Utils.count(0, r6, r7, r8);
                double avg = (double)(r6+r7+r8)/nonzero;
                f.setDouble(of, avg);
            }
        }
    }

    private static void populateGrades(Map<String, Integer> from, Ucenik to, int raz) throws NoSuchFieldException, IllegalAccessException {
        Class<?> c = to.getClass();
        for (Map.Entry<String, Integer> e : from.entrySet()) {
            switch (e.getKey()) {
                case "Likovno vaspitanje":
                    c.getDeclaredField("likovno" + raz).setInt(to, e.getValue());
                    break;
                case "Tehničko obrazovanje":
                    c.getDeclaredField("tehnicko" + raz).setInt(to, e.getValue());
                    break;
                case "Geografija":
                    c.getDeclaredField("geografija" + raz).setInt(to, e.getValue());
                    break;
                case "Biologija":
                    c.getDeclaredField("biologija" + raz).setInt(to, e.getValue());
                    break;
                case "Hemija":
                    c.getDeclaredField("hemija" + raz).setInt(to, e.getValue());
                    break;
                case "Izabrani sport":
                    c.getDeclaredField("sport" + raz).setInt(to, e.getValue());
                    break;
                case "Matematika":
                    c.getDeclaredField("matematika" + raz).setInt(to, e.getValue());
                    break;
                case "Istorija":
                    c.getDeclaredField("istorija" + raz).setInt(to, e.getValue());
                    break;
                case "Engleski jezik":
                    c.getDeclaredField("engleski" + raz).setInt(to, e.getValue());
                    break;
                case "Muzičko vaspitanje":
                    c.getDeclaredField("muzicko" + raz).setInt(to, e.getValue());
                    break;
                case "Fizičko vaspitanje":
                    c.getDeclaredField("fizicko" + raz).setInt(to, e.getValue());
                    break;
                case "Vladanje":
                    c.getDeclaredField("vladanje" + raz).setInt(to, e.getValue());
                    break;
                case "Fizika":
                    c.getDeclaredField("fizika" + raz).setInt(to, e.getValue());
                    break;
                case "Srpski jezik":
                    c.getDeclaredField("srpski" + raz).setInt(to, e.getValue());
                    break;
                default:
                    if (e.getKey().isEmpty()) {
                        System.err.println("Empty grade @ uc" + to.sifra);
                        Index.errors(to.sifra);
                        to.drugiStraniJezik = null;
                        c.getDeclaredField("drugiStrani" + raz).setInt(to, e.getValue());
                    } else if (e.getKey().endsWith("jezik")) {
                        to.drugiStraniJezik = CharUtils.stripAll(e.getKey());
                        c.getDeclaredField("drugiStrani" + raz).setInt(to, e.getValue());
                    } else throw new IllegalArgumentException("Invalid subject: " + e.getKey());
                    break;
            }
        }
    }

}

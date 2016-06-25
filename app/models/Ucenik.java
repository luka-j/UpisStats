package models;

import com.avaje.ebean.Model;
import controllers.CharUtils;
import controllers.Index;
import upismpn.obrada.UcenikWrapper;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luka on 5.5.16..
 */
@Entity
@Table(name = "ucenici")
public class Ucenik extends Model {
    @Id
    public long id;

    public int godina;

    @Column(unique = true)
    public int sifra;
    @ManyToOne(cascade = CascadeType.ALL)
    public models.OsnovnaSkola osnovna;
    @ManyToOne(cascade = CascadeType.ALL)
    public Smer upisana;

    public String drugiStraniJezik;
    public int likovno6, tehnicko6, geografija6, biologija6, sport6, drugiStrani6, matematika6, istorija6, engleski6, muzicko6, fizicko6, vladanje6, fizika6, srpski6;
    public int likovno7, tehnicko7, geografija7, biologija7, sport7, drugiStrani7, matematika7, istorija7, engleski7, muzicko7, fizicko7, vladanje7, fizika7, srpski7, hemija7;
    public int likovno8, tehnicko8, geografija8, biologija8, sport8, drugiStrani8, matematika8, istorija8, engleski8, muzicko8, fizicko8, vladanje8, fizika8, srpski8, hemija8;
    public double likovnoP, tehnickoP, geografijaP, biologijaP, sportP, drugiStraniP, matematikaP, istorijaP, engleskiP, muzickoP, fizickoP, vladanjeP, fizikaP, srpskiP, hemijaP;

    @OneToMany(mappedBy = "ucenik")
    public List<Takmicenje> takmicenja = new ArrayList<>();

    @OneToMany(mappedBy = "ucenik")
    public List<Zelja> listaZelja = new ArrayList<>();

    public double prosekSesti, prosekSedmi, prosekOsmi, prosekUkupno;
    public double matematika, srpski, kombinovani;
    public double bodoviIzSkole, bodoviSaZavrsnog, bodoviUkupno, bodoviSaPrijemnog, bodoviSaTakmicenja;
    public int brojZelja, upisanaZelja, krug;

    public static Ucenik create(UcenikWrapper from) {
        Ucenik uc = new Ucenik();
        uc.godina = Index.CURRENT_YEAR;
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

        UcenikWrapper.SrednjaSkola ss = from.upisanaSkola;
        uc.upisana = Smer.create(ss.sifra, ss.ime, ss.mesto, ss.okrug, ss.smer, ss.podrucje, ss.kvota);
        UcenikWrapper.OsnovnaSkola os = from.osInfo;
        uc.osnovna = OsnovnaSkola.create(os.ime, os.mesto, os.okrug);
        uc.save();
        for (Map.Entry<UcenikWrapper.Takmicenje, Integer> tak : from.takmicenja.entrySet()) {
            uc.takmicenja.add(Takmicenje.create(uc, tak.getKey().predmet, tak.getValue(), tak.getKey().mesto, tak.getKey().rang));
        }
        /*for(UcenikWrapper.SrednjaSkola z : from.listaZelja) {
            uc.listaZelja.add(Zelja.create(uc, z.sifra));
        }*/
        return uc;
    }

    public static void populateZelje(UcenikWrapper from) {
        Ucenik uc = finder.where().eq("sifra", from.id).findUnique();
        if (uc == null) {
            System.err.println("Non-existant uc" + from.id);
            return;
        }
        for (UcenikWrapper.SrednjaSkola z : from.listaZelja) {
            uc.listaZelja.add(Zelja.create(uc, z.sifra));
        }
        uc.update();
    }

    public static void populateAverages(Ucenik of) throws NoSuchFieldException, IllegalAccessException {
        for (Field f : Ucenik.class.getDeclaredFields()) {
            if (f.getName().endsWith("P")) {
                String subject = f.getName().substring(0, f.getName().length() - 1);
                double avg = Ucenik.class.getField(subject + "7").getInt(of) + Ucenik.class.getField(subject + "8").getInt(of);
                if (!subject.equals("hemija")) {
                    avg += Ucenik.class.getField(subject + "6").getInt(of);
                    avg /= 3;
                } else
                    avg /= 2;
                f.setDouble(of, avg);
            }
        }
    }

    private static void populateGrades(Map<String, Integer> from, Ucenik to, int raz) throws NoSuchFieldException, IllegalAccessException {
        Class<Ucenik> c = Ucenik.class;
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

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Godina upisa: ").append(godina).append(", sifra: ").append(sifra).append("\n")
                .append("osnovna: ").append(osnovna.ime).append(" (").append(osnovna.mesto)
                .append("), upisana: ").append(upisana.ime).append(" (").append(upisana.mesto).append(")\n")
                .append("Upisan u ").append(krug).append(" krugu\n");
        return str.toString();
    }

    public static Finder<Long, Ucenik> finder = new Model.Finder<>(Ucenik.class);
}

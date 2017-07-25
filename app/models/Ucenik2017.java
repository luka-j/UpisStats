package models;

import com.avaje.ebean.Model;
import controllers.Index;
import upis17.data.UcenikWrapper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
public class Ucenik2017 extends Ucenik {

    public static Finder<Long, Ucenik2017> finder = new Model.Finder<>(Ucenik2017.class);

    @ManyToOne(cascade = CascadeType.ALL)
    public models.OsnovnaSkola2017 osnovna;
    @ManyToOne(cascade = CascadeType.ALL)
    public Smer2017 upisana;
    @OneToMany(mappedBy = "ucenik")
    public List<Zelja2017> listaZelja1 = new ArrayList<>();
    @OneToMany(mappedBy = "ucenik")
    public List<Zelja2017> listaZelja2 = new ArrayList<>();
    @OneToOne(mappedBy = "ucenik")
    public Takmicenje2017 takmcenje;


    public double drugiMaternji6, drugiMaternji7, drugiMaternji8, drugiMaternjiP;

    public static Ucenik2017 create(UcenikWrapper from) {
        Ucenik2017 uc = new Ucenik2017();
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
            populateGrades(from.sestiRaz.ocene, uc, 6);
            populateGrades(from.sedmiRaz.ocene, uc, 7);
            populateGrades(from.osmiRaz.ocene, uc, 8);
            Ucenik.populateAverages(uc);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Exception while populating: invalid field(s) for uc" + uc.sifra);
            Index.errors(uc.sifra);
            return null;
        }

        uc.osnovna = OsnovnaSkola2017.finder.byId((long) from.osnovna.id);
        uc.upisana = Smer2017.find(from.smer.sifra);
        uc.listaZelja1.addAll(from.listaZelja1.stream().map(z -> Zelja2017.create(uc, z.smer.sifra, z.uslov)).collect(Collectors.toList()));
        uc.listaZelja2.addAll(from.listaZelja2.stream().map(z -> Zelja2017.create(uc, z.smer.sifra, z.uslov)).collect(Collectors.toList()));
        if(from.takmicenje != null) uc.takmcenje = Takmicenje2017.create(uc, from.takmicenje);

        return uc;
    }


    private static void populateGrades(Map<String, Integer> from, Ucenik to, int raz) throws NoSuchFieldException, IllegalAccessException {
        Class<Ucenik2017> c = Ucenik2017.class;
        for (Map.Entry<String, Integer> e : from.entrySet()) {
            switch (e.getKey()) {
                case "izbornisport":
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
}

package models;

import io.ebean.Ebean;
import rs.lukaj.upisstats.scraper.obrada.UcenikWrapper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luka on 26.6.16..
 */
@Entity
@Table(name = "ucenici2015")
public class Ucenik2015 extends Ucenik {

    @ManyToOne(cascade = CascadeType.ALL)
    public models.OsnovnaSkola2015 osnovna;
    @ManyToOne(cascade = CascadeType.ALL)
    public Smer2015 upisana;
    @OneToMany(mappedBy = "ucenik")
    public List<Zelja2015> listaZelja = new ArrayList<>();
    @OneToMany(mappedBy = "ucenik")
    public List<Takmicenje2015> takmicenja = new ArrayList<>();

    public static void populateZelje(UcenikWrapper from) {
        Ucenik2015 uc = Ebean.find(Ucenik2015.class).where().eq("sifra", from.id).findOne();
        if (uc == null) {
            System.err.println("Non-existant uc" + from.id);
            return;
        }
        for(short i=0; i<from.listaZelja.size(); i++)
            uc.listaZelja.add(Zelja2015.create(uc, from.listaZelja.get(i).sifra, i));
        uc.update();
    }

    //ne postoje bodoviSaTakmicenja u Smerovi i OsnovnaSkola
    //takodje, 2016. se takmicenja nisu racunala, a 2017 imam uradjenu kako treba
    //tako da bi ovaj quick&dirty fix trebalo da prodje okej (i brzo)
    public static void fixTakmicenja(UcenikWrapper from) {
        Ucenik2015 uc = Ebean.find(Ucenik2015.class).where().eq("sifra", from.id).findOne();
        if (uc == null) {
            System.err.println("Non-existant uc" + from.id);
            return;
        }
        uc.bodoviSaTakmicenja = from.bodoviSaTakmicenja;
        uc.update();
    }

    public static Ucenik2015 create(UcenikWrapper from) {
        Ucenik2015 uc = new Ucenik2015();
        Ucenik.create(uc, from);
        UcenikWrapper.SrednjaSkola ss = from.upisanaSkola;
        uc.upisana = Smer2015.create(ss.sifra, ss.ime, ss.mesto, ss.okrug, ss.smer, ss.podrucje, ss.kvota);
        UcenikWrapper.OsnovnaSkola os = from.osInfo;
        uc.osnovna = OsnovnaSkola2015.create(os.ime, os.mesto, os.okrug);
        uc.save();


        for (Map.Entry<UcenikWrapper.Takmicenje, Integer> tak : from.takmicenja.entrySet()) {
            uc.takmicenja.add(Takmicenje2015.create(uc, tak.getKey().predmet, tak.getValue(), tak.getKey().mesto, tak.getKey().rang));
        }
        return uc;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Sifra: ").append(sifra).append("\n")
                .append("osnovna: ").append(osnovna.ime).append(" (").append(osnovna.mesto)
                .append("), upisana: ").append(upisana.ime).append(" (").append(upisana.mesto).append(")\n")
                .append("Upisan u ").append(krug).append(" krugu\n")
                .append("Prosek: ").append(prosekUkupno).append(", zavrsni: ").append(bodoviSaZavrsnog).append("\n");
        return str.toString();
    }
}

package models;

import com.avaje.ebean.Model;
import rs.lukaj.upisstats.scraper.obrada.UcenikWrapper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luka on 26.6.16..
 */
@Entity
@Table(name = "ucenici2016")
public class Ucenik2016 extends Ucenik {

    public static Finder<Long, Ucenik2016> finder = new Model.Finder<>(Ucenik2016.class);

    @ManyToOne(cascade = CascadeType.ALL)
    public models.OsnovnaSkola2016 osnovna;
    @ManyToOne(cascade = CascadeType.ALL)
    public Smer2016 upisana;
    @OneToMany(mappedBy = "ucenik")
    public List<Zelja2016> listaZelja = new ArrayList<>();
    @OneToMany(mappedBy = "ucenik")
    public List<Takmicenje2016> takmicenja = new ArrayList<>();

    public static void populateZelje(UcenikWrapper from) {
        Ucenik2016 uc = finder.where().eq("sifra", from.id).findUnique();
        if (uc == null) {
            System.err.println("Non-existant uc" + from.id);
            return;
        }
        for(short i=0; i<from.listaZelja.size(); i++)
            uc.listaZelja.add(Zelja2016.create(uc, from.listaZelja.get(i).sifra, i));
        uc.update();
    }

    public static Ucenik2016 create(UcenikWrapper from) {
        Ucenik2016 uc = new Ucenik2016();
        Ucenik.create(uc, from);
        UcenikWrapper.SrednjaSkola ss = from.upisanaSkola;
        uc.upisana = Smer2016.create(ss.sifra, ss.ime, ss.mesto, ss.okrug, ss.smer, ss.podrucje, ss.kvota);
        UcenikWrapper.OsnovnaSkola os = from.osInfo;
        uc.osnovna = OsnovnaSkola2016.create(os.ime, os.mesto, os.okrug);
        uc.save();

        for (Map.Entry<UcenikWrapper.Takmicenje, Integer> tak : from.takmicenja.entrySet()) {
            uc.takmicenja.add(Takmicenje2016.create(uc, tak.getKey().predmet, tak.getValue(), tak.getKey().mesto, tak.getKey().rang));
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

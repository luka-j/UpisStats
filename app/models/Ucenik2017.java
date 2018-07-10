package models;

import controllers.CharUtils;
import io.ebean.Ebean;
import rs.lukaj.upisstats.scraper.obrada2017.UcenikW;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "ucenici2017")
public class Ucenik2017 extends UcenikExt {


    @ManyToOne(cascade = CascadeType.ALL)
    public OsnovnaSkola2017 osnovna;
    @ManyToOne(cascade = CascadeType.ALL)
    public Smer2017 upisana;
    @OneToMany(mappedBy = "ucenik")
    public List<Zelja2017> listaZelja = new ArrayList<>();
    @OneToMany(mappedBy = "ucenik")
    public List<Prijemni2017> prijemni = new ArrayList<>();
    @OneToOne(mappedBy = "ucenik")
    public Takmicenje2017 takmicenje;


    public static void populateZelje(UcenikW from) {
        Ucenik2017 uc = Ebean.find(Ucenik2017.class).where().eq("sifra", from.sifra).findOne();
        if(uc == null) {
            System.err.println("Non-existant sifra " + from.sifra);
            return;
        }
        from.listaZelja1.forEach(z -> uc.listaZelja.add(Zelja2017.create(uc, z)));
        from.listaZelja2.forEach(z -> uc.listaZelja.add(Zelja2017.create(uc, z)));
        uc.update();
    }

    public static Ucenik2017 create(UcenikW from) {
        //System.out.println("Loading " + from.sifra);
        Ucenik2017 uc = new Ucenik2017();
        if(UcenikExt.fillIn(uc, from) == null) return null;
        uc.save(); //fun fact: this assigns null to lists

        uc.osnovna = Ebean.find(OsnovnaSkola2017.class, (long) from.osnovna.id);
        uc.upisana = Smer2017.find(CharUtils.stripAll(from.smer.sifra));
        uc.osnovna.addUcenik();
        uc.upisana.addUcenik();
        from.listaZelja1.forEach(z -> uc.listaZelja.add(Zelja2017.create(uc, z)));
        from.listaZelja2.forEach(z -> uc.listaZelja.add(Zelja2017.create(uc, z)));
        uc.prijemni=(from.prijemni.entrySet().stream().map(e -> Prijemni2017.create(uc, e.getKey(), e.getValue())).collect(Collectors.toList()));
        if(from.takmicenje != null) uc.takmicenje = Takmicenje2017.create(uc, from.takmicenje);

        uc.update();
        return uc;
    }
}

package models;

import controllers.CharUtils;
import io.ebean.Ebean;
import rs.lukaj.upisstats.scraper.obrada2017.UcenikW;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "ucenici2018")
public class Ucenik2018 extends UcenikExt {

    @ManyToOne(cascade = CascadeType.ALL)
    public OsnovnaSkola2018 osnovna;
    @ManyToOne(cascade = CascadeType.ALL)
    public Smer2018 upisana;
    @OneToMany(mappedBy = "ucenik")
    public List<Zelja2018> listaZelja = new ArrayList<>();
    @OneToMany(mappedBy = "ucenik")
    public List<Prijemni2018> prijemni = new ArrayList<>();
    @OneToOne(mappedBy = "ucenik")
    public Takmicenje2018 takmicenje;

    public static Ucenik2018 create(UcenikW from) {
        //System.out.println("Loading " + from.sifra);
        Ucenik2018 uc = new Ucenik2018();
        if(UcenikExt.fillIn(uc, from) == null) return null;
        uc.save(); //fun fact: this assigns null to lists

        uc.osnovna = Ebean.find(OsnovnaSkola2018.class, (long) from.osnovna.id);
        uc.upisana = Smer2018.find(CharUtils.stripAll(from.smer.sifra));
        uc.osnovna.addUcenik();
        uc.upisana.addUcenik();
        from.listaZelja1.forEach(z -> uc.listaZelja.add(Zelja2018.create(uc, z)));
        from.listaZelja2.forEach(z -> uc.listaZelja.add(Zelja2018.create(uc, z)));
        uc.prijemni=(from.prijemni.entrySet().stream().map(e -> Prijemni2018.create(uc, e.getKey(), e.getValue())).collect(Collectors.toList()));
        if(from.takmicenje != null) uc.takmicenje = Takmicenje2018.create(uc, from.takmicenje);

        uc.update();
        return uc;
    }
}

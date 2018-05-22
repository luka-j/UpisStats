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
public class Ucenik2017 extends Ucenik {


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


    public int drugiMaternji6, drugiMaternji7, drugiMaternji8;
    public double drugiMaternjiP;
    public double najboljiBlizanacBodovi;
    public int blizanacSifra;
    @Column(name = "bodova_am")
    public double bodovaAM;
    public String maternjiJezik, prviStraniJezik;
    public boolean vukovaDiploma, prioritet;

    public static void populateZelje(UcenikW from) {
        Ucenik2017 uc = Ebean.find(Ucenik2017.class).where().eq("sifra", from.sifra).findOne();
        if(uc == null) {
            System.err.println("Non-existant sifra " + from.sifra);
            return;
        }
        for(short i=0; i<from.listaZelja1.size(); i++) {
            UcenikW.Zelja z = from.listaZelja1.get(i);
            uc.listaZelja.add(Zelja2017.create(uc, z.smer.sifra, z.uslov, z.bodovaZaUpis, (short)1, i));
        }
        for(short i=0; i<from.listaZelja2.size(); i++) {
            UcenikW.Zelja z = from.listaZelja2.get(i);
            uc.listaZelja.add(Zelja2017.create(uc, z.smer.sifra, z.uslov, z.bodovaZaUpis, (short)2, i));
        }
        uc.update();
    }

    public static Ucenik2017 create(UcenikW from) {
        //System.out.println("Loading " + from.sifra);
        Ucenik2017 uc = new Ucenik2017();
        if(Ucenik.create(uc, from) == null) return null;

        uc.najboljiBlizanacBodovi = from.najboljiBlizanacBodovi;
        uc.blizanacSifra = from.blizanacSifra;
        uc.bodovaAM = from.bodovaAM;
        uc.maternjiJezik = CharUtils.stripAll(from.maternji);
        uc.prviStraniJezik = CharUtils.stripAll(from.prviStrani);
        uc.vukovaDiploma = from.vukovaDiploma;
        uc.prioritet = from.prioritet;

        uc.save(); //fun fact: this assigns null to lists

        uc.osnovna = Ebean.find(OsnovnaSkola2017.class, (long) from.osnovna.id);
        uc.upisana = Smer2017.find(CharUtils.stripAll(from.smer.sifra));
        uc.osnovna.addUcenik();
        uc.upisana.addUcenik();
        for(short i=0; i<from.listaZelja1.size(); i++) {
            UcenikW.Zelja z = from.listaZelja1.get(i);
            uc.listaZelja.add(Zelja2017.create(uc, z.smer.sifra, z.uslov, z.bodovaZaUpis, (short)1, i));
        }
        for(short i=0; i<from.listaZelja2.size(); i++) {
            UcenikW.Zelja z = from.listaZelja2.get(i);
            uc.listaZelja.add(Zelja2017.create(uc, z.smer.sifra, z.uslov, z.bodovaZaUpis, (short)2, i));
        }
        uc.prijemni=(from.prijemni.entrySet().stream().map(e -> Prijemni2017.create(uc, e.getKey(), e.getValue())).collect(Collectors.toList()));
        if(from.takmicenje != null) uc.takmicenje = Takmicenje2017.create(uc, from.takmicenje);

        uc.update();
        return uc;
    }
}

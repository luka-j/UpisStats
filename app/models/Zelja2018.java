package models;

import rs.lukaj.upisstats.scraper.obrada2017.UcenikW;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lista_zelja2018")
public class Zelja2018 extends ZeljaExt {
    @ManyToOne
    public Smer2018 smer;

    @ManyToOne
    public Ucenik2018 ucenik;

    public static Zelja2018 create(Ucenik2018 uc, UcenikW.Zelja zelja) {
        Zelja2018 z = new Zelja2018();
        z.smer = Smer2018.find(zelja.smer.sifra);
        z.ucenik = uc;
        ZeljaExt.fillIn(z, zelja);
        return z;
    }
}
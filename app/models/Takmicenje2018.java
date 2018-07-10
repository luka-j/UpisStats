package models;

import rs.lukaj.upisstats.scraper.obrada2017.UcenikW;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="takmicenja2018")
public class Takmicenje2018 extends Takmicenje {
    @OneToOne
    public Ucenik2018 ucenik;

    public static Takmicenje2018 create(Ucenik2018 uc, UcenikW.Takmicenje takmicenje) {
        if(takmicenje == null) return null;
        Takmicenje2018 tak = new Takmicenje2018();
        tak.ucenik = uc;
        Takmicenje.fillIn(tak, takmicenje.predmet, takmicenje.bodova, takmicenje.mesto, takmicenje.nivo);
        return tak;
    }
}

package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by luka on 26.6.16..
 */
@Entity
@Table(name = "takmicenja2015")
public class Takmicenje2015 extends Takmicenje {

    @ManyToOne
    public Ucenik2015 ucenik;

    public static Takmicenje2015 create(Ucenik2015 uc, String predmet, int bodovi, int mesto, int rang) {
        Takmicenje2015 tak = new Takmicenje2015();
        tak.ucenik = uc;
        Takmicenje.create(tak, predmet, bodovi, mesto, rang);
        return tak;
    }
}

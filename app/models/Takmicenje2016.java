package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by luka on 26.6.16..
 */
@Entity
@Table(name = "takmicenja2016")
public class Takmicenje2016 extends Takmicenje {

    @ManyToOne
    public Ucenik2016 ucenik;

    public static Takmicenje2016 create(Ucenik2016 uc, String predmet, int bodovi, int mesto, int rang) {
        Takmicenje2016 tak = new Takmicenje2016();
        tak.ucenik = uc;
        Takmicenje.create(tak, predmet, bodovi, mesto, rang);
        return tak;
    }
}

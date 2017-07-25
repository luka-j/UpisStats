package models;

import org.jetbrains.annotations.Contract;
import upis17.data.UcenikWrapper;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Takmicenje2017 extends Takmicenje {
    @OneToOne
    public Ucenik2017 ucenik;

    @Contract("_, null -> null")
    public static Takmicenje2017 create(Ucenik2017 uc, UcenikWrapper.Takmicenje takmicenje) {
        if(takmicenje == null) return null;
        Takmicenje2017 tak = new Takmicenje2017();
        tak.ucenik = uc;
        Takmicenje.create(tak, takmicenje.predmet, takmicenje.bodova, takmicenje.mesto, takmicenje.nivo);
        return tak;
    }
}

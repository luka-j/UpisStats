package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Prijemni2018 extends Prijemni {
    @ManyToOne
    public Ucenik2018 ucenik;


    public static Prijemni2018 create(Ucenik2018 uc, String ispit, double bodovi) {
        Prijemni2018 prijemni = new Prijemni2018();
        prijemni.ucenik = uc;
        Prijemni.fillIn(prijemni, ispit, bodovi);
        prijemni.save();
        return prijemni;
    }
}

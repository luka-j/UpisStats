package models;

import controllers.CharUtils;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Prijemni2017 extends Model {
    @Id
    public long id;

    @ManyToOne
    public Ucenik2017 ucenik;

    @Lob
    public String nazivIspita;
    public double bodova;

    public static Prijemni2017 create(Ucenik2017 uc, String ispit, double bodovi) {
        Prijemni2017 prijemni = new Prijemni2017();
        prijemni.ucenik = uc;
        prijemni.nazivIspita = CharUtils.stripAll(ispit);
        prijemni.bodova = bodovi;
        prijemni.save();
        return prijemni;
    }
}

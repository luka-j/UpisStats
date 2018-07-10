package models;

import controllers.CharUtils;
import io.ebean.Model;

import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Prijemni extends Model {
    @Id
    public long id;

    @Lob
    public String nazivIspita;
    public double bodova;

    public static <T extends Prijemni> T fillIn(T dest, String ispit, double bodovi) {
        dest.nazivIspita = CharUtils.stripAll(ispit);
        dest.bodova = bodovi;
        //dest.save();
        return dest;
    }
}

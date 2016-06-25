package models;

import com.avaje.ebean.Model;
import controllers.CharUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by luka on 5.5.16..
 */
@Entity
@Table(name = "takmicenja")
public class Takmicenje extends Model {

    @Id
    public long id;

    @ManyToOne
    public Ucenik ucenik;

    public String predmet;
    public int bodova, mesto, rang;

    public static Takmicenje create(Ucenik uc, String predmet, int bodovi, int mesto, int rang) {
        Takmicenje tak = new Takmicenje();
        tak.predmet = CharUtils.stripAll(predmet);
        tak.bodova = bodovi;
        tak.mesto = mesto;
        tak.rang = rang;
        tak.ucenik = uc;
        tak.save();
        return tak;
    }
}

package models;

import com.avaje.ebean.Model;
import controllers.CharUtils;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by luka on 5.5.16..
 */
@MappedSuperclass
public class Takmicenje extends Model {

    @Id
    public long id;


    public String predmet;
    public int bodova, mesto, rang;

    protected static Takmicenje create(Takmicenje tak, String predmet, int bodovi, int mesto, int rang) {
        tak.predmet = CharUtils.stripAll(predmet);
        tak.bodova = bodovi;
        tak.mesto = mesto;
        tak.rang = rang;
        tak.save();
        return tak;
    }
}

package models;

import io.ebean.Ebean;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by luka on 26.6.16..
 */
@Entity
@Table(name = "lista_zelja2015")
public class Zelja2015 extends Model {
    @Id
    public long id;

    @ManyToOne
    public Smer2015 smer;

    @ManyToOne
    public Ucenik2015 ucenik;

    public short redniBroj;

    public static models.Zelja2015 create(Ucenik2015 uc, String sifraSmera, short redniBroj) {
        models.Zelja2015 z = new models.Zelja2015();
        z.smer = Ebean.find(Smer2015.class).where().eq("sifra", sifraSmera).findOne();
        z.ucenik = uc;
        z.redniBroj = redniBroj;
        z.save();
        return z;
    }
}

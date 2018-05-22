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
@Table(name = "lista_zelja2016")
public class Zelja2016 extends Model {
    @Id
    public long id;

    @ManyToOne
    public Smer2016 smer;

    @ManyToOne
    public Ucenik2016 ucenik;

    public short redniBroj;

    public static models.Zelja2016 create(Ucenik2016 uc, String sifraSmera, short redniBroj) {
        models.Zelja2016 z = new models.Zelja2016();
        z.smer = Ebean.find(Smer2016.class).where().eq("sifra", sifraSmera).findOne();
        z.ucenik = uc;
        z.redniBroj = redniBroj;
        z.save();
        return z;
    }
}
package models;

import com.avaje.ebean.Model;

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

    public static models.Zelja2016 create(Ucenik2016 uc, String sifraSmera) {
        models.Zelja2016 z = new models.Zelja2016();
        z.smer = Smer2016.finder.where().eq("sifra", sifraSmera).findUnique();
        z.ucenik = uc;
        z.save();
        return z;
    }
}
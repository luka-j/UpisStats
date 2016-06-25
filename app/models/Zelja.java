package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by luka on 5.5.16..
 */
@Entity
@Table(name = "lista_zelja")
public class Zelja extends Model {
    @Id
    public long id;

    @ManyToOne
    public Smer smer;

    @ManyToOne
    public Ucenik ucenik;

    public static Zelja create(Ucenik uc, String sifraSmera) {
        Zelja z = new Zelja();
        z.smer = Smer.finder.where().eq("sifra", sifraSmera).findUnique();
        z.ucenik = uc;
        z.save();
        return z;
    }
}

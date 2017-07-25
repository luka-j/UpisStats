package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lista_zelja2017")
public class Zelja2017 extends Model {
    @Id
    public long id;

    @ManyToOne
    public Smer2017 smer;

    @ManyToOne
    public Ucenik2017 ucenik;

    public boolean ispunioUslov;

    public static Zelja2017 create(Ucenik2017 uc, String sifraSmera, boolean uslov) {
        Zelja2017 z = new Zelja2017();
        z.smer = Smer2017.find(sifraSmera);
        z.ucenik = uc;
        z.ispunioUslov = uslov;
        z.save();
        return z;
    }
}

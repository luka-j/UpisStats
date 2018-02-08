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
    public double bodovaZaUpis;
    public short krug, redniBroj;

    public static Zelja2017 create(Ucenik2017 uc, String sifraSmera, boolean uslov, double bodovaZaUpis,
                                   short krug, short redniBroj) {
        Zelja2017 z = new Zelja2017();
        z.smer = Smer2017.find(sifraSmera);
        z.ucenik = uc;
        z.ispunioUslov = uslov;
        z.bodovaZaUpis = bodovaZaUpis;
        z.krug = krug;
        z.redniBroj = redniBroj;
        z.save();
        return z;
    }
}

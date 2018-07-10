package models;

import io.ebean.Model;
import rs.lukaj.upisstats.scraper.obrada2017.UcenikW;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ZeljaExt extends Model {
    @Id
    public long id;

    public boolean ispunioUslov;
    public double bodovaZaUpis;
    public short krug, redniBroj;

    public static <T extends ZeljaExt> T fillIn(T z, UcenikW.Zelja zelja) {
        z.ispunioUslov = zelja.uslov;
        z.bodovaZaUpis = zelja.bodovaZaUpis;
        z.krug = (short)zelja.krug;
        z.redniBroj = (short)zelja.redniBroj;
        z.save();
        return z;
    }
}

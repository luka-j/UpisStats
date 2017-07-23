package models;

import com.avaje.ebean.Model;
import controllers.CharUtils;
import org.jetbrains.annotations.NotNull;
import upismpn.obrada.Osnovna;
import upismpn.obrada.OsnovneBase;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luka on 26.6.16..
 */
@Entity
@Table(name = "os2016")
public class OsnovnaSkola2016 extends OsnovnaSkola {

    public int sajtId;
    public int brojUcenikaUkupno; //ukljucujuci i one koji nisu upisali srednju
    public double matematika6real, matematika7real, matematika8real, srpski6real, srpski7real, srpski8real;
    public double srpskiPReal, matematikaPReal;
    public double procenatUpisanih;

    public static Model.Finder<Long, OsnovnaSkola2016> finder = new Model.Finder<>(OsnovnaSkola2016.class);

    public static OsnovnaSkola2016 create(String ime, String mesto, String okrug) {
        ime = CharUtils.stripAll(ime);
        mesto = CharUtils.stripAll(mesto);
        okrug = CharUtils.stripAll(okrug);
        OsnovnaSkola2016 res = finder.where().eq("ime", ime).eq("mesto", mesto).eq("okrug", okrug).findUnique();
        if (res == null) {
            OsnovnaSkola2016 os = new OsnovnaSkola2016();
            OsnovnaSkola.create(os, ime, mesto, okrug);
            return os;
        }
        res.brojUcenika++;
        res.update();
        return res;
    }

    public void fillInNeupisani() {
        List<Osnovna> osnovne = OsnovneBase.getOsnovne(ime, okrug);
        Osnovna osnovna;
        if(osnovne == null) {
            System.err.println("Ne postoji osnovna " + ime + "+" + okrug);
            return;
        }
        if(osnovne.size() == 0) {
            System.err.println("ne postoji osnovna " + ime + "+" + okrug);
            return;
        }
        if(osnovne.size() == 1) osnovna = osnovne.get(0);
        else osnovna = tryGetOsnovna(osnovne);
        if(osnovna == null)
            osnovna = pickOsnovna(osnovne);
        sajtId = osnovna.getId();
        adresa = osnovna.getAdresa();
        telefon = osnovna.getTelefon();
        brojUcenikaUkupno = osnovna.getBrojUcenikaUkupno();
        matematika6real = osnovna.getMat6();
        matematika7real = osnovna.getMat7();
        matematika8real = osnovna.getMat8();
        srpski6real = osnovna.getSrp6();
        srpski7real = osnovna.getSrp7();
        srpski8real = osnovna.getSrp8();
        srpskiPReal = osnovna.getSrpp();
        matematikaPReal = osnovna.getMatp();
        procenatUpisanih = ((double)brojUcenika/brojUcenikaUkupno)*100;
        update();
    }

    private Osnovna tryGetOsnovna(List<Osnovna> osnovne) {
        for(Osnovna os : osnovne)
            if(os.getMesto().equals(mesto) && os.getBrojUcenikaUkupno() >= brojUcenika)
                return os;
        return null;
    }

    private static class Pair implements Comparable<Pair> {
        public Pair(double a, double b) {
            this.a = a;
            this.b = b;
        }

        double a, b;

        @Override
        public int compareTo(@NotNull Pair o) {
            return Double.compare(b, o.b);
        }
    }

    private Osnovna pickOsnovna(List<Osnovna> osnovne) {
        Pair[] ucDiff = new Pair[osnovne.size()];
        for(int i=0; i<osnovne.size(); i++) {
            ucDiff[i] = new Pair(i, osnovne.get(i).getBrojUcenikaUkupno() - brojUcenika);
            if(ucDiff[i].b < 0) ucDiff[i].b = Double.POSITIVE_INFINITY;
        }
        Arrays.sort(ucDiff);
        if(ucDiff[1].b - ucDiff[0].b > 3) return osnovne.get((int)ucDiff[0].a);
        int mini = 0;
        double min=Math.abs(osnovne.get(0).getMatp() - matematikaP), curr;
        for(int i=1; i<osnovne.size(); i++)
            if ((curr=Math.abs(osnovne.get(i).getMatp() - matematikaP)) < min) {
                min = curr;
                mini=i;
            }
        return osnovne.get(mini);
    }
}

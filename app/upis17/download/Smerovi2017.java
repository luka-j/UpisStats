package upis17.download;


/**
 * Created by luka on 2.7.17..
 */
public class Smerovi2017 extends Smerovi {
    protected Smerovi2017() {
    }

    private static Smerovi2017 instance;

    public static Smerovi2017 getInstance() {
        if(instance == null) instance = new Smerovi2017();
        return instance;
    }

    //sifra, ime, mesto, jezik
    private static String[] parseSmer(String smer) {
        StringBuilder ime= new StringBuilder();
        String sifra, podrucje, jezik;
        int spaces = 0, i=0;
        while(spaces < 3)
            if(Character.isWhitespace(smer.charAt(i++)))
                spaces++;
        sifra = smer.substring(0, i);
        String[] data = smer.substring(i).split(",");
        int l = data.length;
        jezik = data[l-1];
        podrucje = data[l-2];
        for(int j=0; j<l-2; j++)
            ime.append(data[j]).append(",");
        return new String[]{sifra, ime.toString(), podrucje, jezik};
    }

    @Override
    public Smer2017 get(String sifra) {
        return (Smer2017) super.get(sifra);
    }

    @Override
    protected Smer createSmer(String compactString) {
        return new Smer2017(compactString);
    }
}

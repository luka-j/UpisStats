package upis17.download;

import upis17.Locations;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luka
 */
public class Smerovi {
    static final String SAVEFILE_NAME = "smerovi";
    static final File SMEROVI_FOLDER = new File(Locations.DATA_FOLDER, "smeroviData");
    static {
        if(!SMEROVI_FOLDER.isDirectory()) SMEROVI_FOLDER.mkdirs();
    }

    private static Smerovi instance;
    public static Smerovi getInstance() {
        if(instance == null) instance = new Smerovi();
        return instance;
    }

    private final LinkedHashMap<String, Smer> base = new LinkedHashMap<>(2_280);

    public void load() {
        loadFromFile();
    }


    protected void addToBase(Smer s) {
        base.put(s.getSifra(), s);
    }
    protected Smer createSmer(String compactString) {
        return new Smer(compactString);
    }
    
    public void loadFromFile() {
        File f = new File(Locations.DATA_FOLDER, SAVEFILE_NAME);
        try {
            System.out.println(f.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String text = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
            String[] smerovi = text.split("\\n");
            for(String smer : smerovi) {
                addToBase(createSmer(smer));
            }
        } catch (IOException ex) {
            Logger.getLogger(Ucenik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Smer get(String sifra) {
        return base.get(sifra);
    }

    /**
     * Cuva podatke o smerovima u fajl
     */
    public void save() {
        StringBuilder out = new StringBuilder();
        base.values().forEach((Smer s) -> out.append(s.toCompactString()));
        File f = new File(Locations.DATA_FOLDER, SAVEFILE_NAME);
        try (Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
            bw.write(out.toString());
        } catch (IOException ex) {
            Logger.getLogger(Smerovi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //todo make this implement Iterable

    private Iterator<Map.Entry<String, Smer>> baseIterator;
    private int count=0;
    public void iterate(int pos) {
        baseIterator = base.entrySet().iterator();
        for(int i=0; i<pos; i++) baseIterator.next();
        count = pos;
    }

    public boolean hasNext() {
        return baseIterator.hasNext();
    }

    public String getNextSifra() {
        count++;
        return baseIterator.next().getKey();
    }
    
    public Smer getNext() {
        count++;
        return baseIterator.next().getValue();
    }
    
    public int getCurrentIndex() {return count-1;}
    
    public double getPercentageIterated() {
        return ((double)(count+1)/(base.size()+1)) * 100;
    }

    protected Smerovi() {
    }
}

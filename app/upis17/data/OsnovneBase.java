package upis17.data;

import upis17.Locations;
import upis17.download.Osnovna2017;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OsnovneBase {

    public static final File DATAFILE = new File(Locations.DATA_FOLDER, "osnovne");

    private static Map<Integer, OsnovnaWrapper> base = new HashMap<>();

    public static boolean isLoaded() {
        return !base.isEmpty();
    }

    public static void load() {
        List<String> osnovne = FileMerger.readFromOne(DATAFILE);
        base = osnovne.stream().map(Osnovna2017::new)
                .map(OsnovnaWrapper::new)
                .collect(Collectors.toMap(os -> os.id, os -> os));
    }

    public static OsnovnaWrapper get(int id) {
        if(!base.containsKey(id)) throw new IllegalArgumentException(String.valueOf(id));
        return base.get(id);
    }

    public static Collection<OsnovnaWrapper> getAll() {
        return base.values();
    }
}

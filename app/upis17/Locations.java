package upis17;

import controllers.Index;

import java.io.File;

public class Locations {
    public static final int YEAR = Index.CURRENT_YEAR;

    public static final File DATA_FOLDER = System.getProperty("os.name").toLowerCase().contains("nix")
            || System.getProperty("os.name").toLowerCase().contains("nux") ?
            new File("/data/Shared/mined/UpisData/" + YEAR)
            : new File("E:\\Shared\\mined\\UpisData\\" + YEAR);
}

package models;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by luka on 1.6.16..
 */

public class CountMatrix extends HashMap<CountMatrix.Datum, Integer> {

    public static class Datum {
        private final String[] datum;

        public Datum(String... datum) {
            this.datum = datum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(datum);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Datum)) return false;
            Datum d = (Datum) o;
            if (d.datum.length != datum.length) return false;
            for (int i = 0; i < datum.length; i++) {
                if (!datum[i].equals(d.datum[i])) return false;
            }
            return true;
        }

        public String[] getArray() {
            return datum;
        }
    }

    private int maxCount = 1;

    public boolean add(Datum element) {
        if (this.containsKey(element)) {
            int currCount = this.get(element);
            if (currCount == maxCount) maxCount++;
            return this.put(element, currCount + 1) != null;
        } else {
            return this.put(element, 1) != null;
        }
    }

    public int maxCount() {
        return maxCount;
    }
}

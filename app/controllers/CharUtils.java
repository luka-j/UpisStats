package controllers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luka on 25.6.16..
 */
public class CharUtils {


    private static final Map<Character, String> toLatin = new HashMap<>();
    private static final Map<Character, String> stripGuillemets = new HashMap<>();

    static {
        toLatin.put('љ', "lj");
        toLatin.put('њ', "nj");
        toLatin.put('е', "e");
        toLatin.put('р', "r");
        toLatin.put('т', "t");
        toLatin.put('з', "z");
        toLatin.put('у', "u");
        toLatin.put('и', "i");
        toLatin.put('о', "o");
        toLatin.put('п', "p");
        toLatin.put('ш', "š");
        toLatin.put('ђ', "đ");
        toLatin.put('а', "a");
        toLatin.put('с', "s");
        toLatin.put('д', "d");
        toLatin.put('ф', "f");
        toLatin.put('г', "g");
        toLatin.put('х', "h");
        toLatin.put('ј', "j");
        toLatin.put('к', "k");
        toLatin.put('л', "l");
        toLatin.put('ч', "č");
        toLatin.put('ћ', "ć");
        toLatin.put('ж', "ž");
        toLatin.put('џ', "dž");
        toLatin.put('ц', "c");
        toLatin.put('в', "v");
        toLatin.put('б', "b");
        toLatin.put('н', "n");
        toLatin.put('м', "m");

        stripGuillemets.put('š', "s");
        stripGuillemets.put('đ', "dj");
        stripGuillemets.put('č', "c");
        stripGuillemets.put('ć', "c");
        stripGuillemets.put('ž', "z");
    }

    private static String toLatin(String word) {
        StringBuilder latin = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            latin.append(toLatin.getOrDefault(word.charAt(i), String.valueOf(word.charAt(i))));
        }
        return latin.toString();
    }

    private static String stripGuillemets(String word) {
        StringBuilder noGuillemets = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            noGuillemets.append(stripGuillemets.getOrDefault(word.charAt(i), String.valueOf(word.charAt(i))));
        }
        return noGuillemets.toString();
    }

    public static String stripAll(String str) {
        return toLatin(stripGuillemets(str.replaceAll(" |'|\"|-|\\.", "").toLowerCase()));
    }

    public static String latinize(String str) {
        return toLatin(str.replace(" ", "")).toLowerCase();
    }
}

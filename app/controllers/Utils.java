package controllers;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Created by luka on 28.5.16.
 */
public class Utils {
    private static final Pattern DOUBLE_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    public static boolean isDouble(String s) {
        return DOUBLE_PATTERN.matcher(s).matches();
    }

    public static double log(double base, double num) {
        return Math.log(num) / Math.log(base);
    }

    public static boolean endsWith(StringBuilder what, String with) {
        return what.substring(what.length() - with.length(), what.length()).equals(with);
    }

    public static int randomInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min) + min);
    }

    public static boolean containsNumericField(Field[] fields, String fieldName) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName)
                    && (field.getType().equals(int.class) || field.getType().equals(double.class))) {
                return true;
            }
        }
        return false;
    }
}

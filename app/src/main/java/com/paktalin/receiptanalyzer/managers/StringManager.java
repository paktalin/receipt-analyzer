package com.paktalin.receiptanalyzer.managers;

import com.paktalin.receiptanalyzer.recognition.similarity.JaroWinkler;

/**
 * Created by Paktalin on 19-Mar-18.
 */

public class StringManager {
    private static final String TAG = StringManager.class.getSimpleName();

    public static final int NO_CUT = 0;
    public static final int CUT_FIRST = 1;
    public static final int MAKE_EQUAL = 2;

    public static String clean(String string) {
        string = string.replaceAll(" ", "");
        string = string.replaceAll("\\.", "");
        return string;
    }

    public static String removeNumbers(String string){
        return string.replaceAll("\\d", "");
    }

    public static String removeLetters(String string) {
        return string.replaceAll("[a-zA-Z]", "");
    }

    public static double similarity(String input, String expectedString) {
        JaroWinkler jaro = new JaroWinkler();
        double distance = jaro.distance(input, expectedString);
        return distance;
    }

    private static String cutFirst(String cutThis, String toLengthOf) {
        if (cutThis.length() > toLengthOf.length())
            return cutThis.substring(0, toLengthOf.length());
        else return cutThis;
    }

    private static String[] makeEqualLength(String string1, String string2) {
        if (string1.length() > string2.length())
            return new String[]{string1.substring(0, string2.length()), string2};
        if (string2.length() > string1.length())
            return new String[]{string1, string2.substring(0, string1.length())};
        else return new String[]{string1, string2};
    }

    private static String[] cut(String string1, String string2, int cutHowMuch) {
        switch (cutHowMuch) {
            case CUT_FIRST:
                return new String[]{cutFirst(string1, string2), string2};
            case MAKE_EQUAL:
                return makeEqualLength(string1, string2);
            default:
                return new String[]{string1, string2};
        }
    }

    public static boolean identical(String input, String expectedString, int cutHowMuch) {
        String[] strings = cut(input, expectedString, cutHowMuch);
        return similarity(strings[0], strings[1]) < 0.07;
    }

    public static boolean similar(String input, String expectedString, int cutHowMuch) {
        String[] strings = cut(input, expectedString, cutHowMuch);
        return similarity(strings[0], strings[1]) < 0.17;
    }

    public static float extractFinalPriceFloat(String string, int flagLength) {
        float f = -1;
        try {
            f = Float.parseFloat(string);
        } catch (NumberFormatException e) {
            if (string.length() > flagLength - 2)
                string = string.substring(flagLength - 2);
            string = string.replaceAll("o", "0");
            string = string.replaceAll(" ", "");
            string = removeLetters(string);
            try {
                f = Float.parseFloat(string);
            } catch (NumberFormatException ignored){}
        }
        return f;
    }
}

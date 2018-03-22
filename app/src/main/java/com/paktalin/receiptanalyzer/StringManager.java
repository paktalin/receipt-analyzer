package com.paktalin.receiptanalyzer;

import android.util.Log;

import com.paktalin.receiptanalyzer.similarity.JaroWinkler;

/**
 * Created by Paktalin on 19-Mar-18.
 */

public class StringManager {
    private static final String TAG = StringManager.class.getSimpleName();


    /*static String getFirstLine(String string) {
        int index = string.indexOf('\n');
        return string.substring(0, index);
    }

    static String getRegCode(String string) {
        //check if contains numbers
        if (string.matches(".*\\d+.*")) {
            int index = string.length() - 8 - 1;
            return string.substring(index, string.length()-1);
        }
        Log.d(TAG, "Couldn't extract register code");
        return null;
    }*/

    public static String clean(String string) {
        string = string.replaceAll(" ", "");
        string = string.replaceAll("\\.", "");
        return string;
    }

    public static String getFirstLine(String string) {
        int index = string.indexOf('\n');
        return string.substring(0, index);
    }

    /**
     * @param input the whole string
     * @param line the line we look for
     * @param number the number of line where the line is supposed to be
     * @return actual number of the line we're looking for
     */
    public static int findLine(String input, String line, int number) {
        String[] lines = input.split("\n");
        if (similar(lines[number - 1], line))
            return number;
        return -1;
    }

    public static double similarity(String input, String string) {
        JaroWinkler jaro = new JaroWinkler();
        double distance = jaro.distance(input, string);
        Log.d(TAG, input + " " + string + " " + distance);
        return distance;
    }

    public static boolean similar(String input, String string) {
        return similarity(input, string) < 0.08;
    }
}

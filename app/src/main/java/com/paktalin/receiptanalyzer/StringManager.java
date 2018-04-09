package com.paktalin.receiptanalyzer;

import android.util.Log;

import com.paktalin.receiptanalyzer.similarity.JaroWinkler;

/**
 * Created by Paktalin on 19-Mar-18.
 */

public class StringManager {
    private static final String TAG = StringManager.class.getSimpleName();

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

    public static double similarity(String input, String string) {
        JaroWinkler jaro = new JaroWinkler();
        double distance = jaro.distance(input, string);
        //Log.d(TAG, input + " " + string + " " + distance);
        return distance;
    }

    public static boolean identical(String input, String string) {
        return similarity(input, string) < 0.07;
    }

    public static boolean similar(String input, String string) {
        Log.d(TAG, input + " " + string + " = " + similarity(input, string));
        return similarity(input, string) < 0.17;
    }

    public static float extractFloat(String string) {
        float f = -1;
        try {
            f = Float.parseFloat(string);
        } catch (NumberFormatException e) {
            string = string.substring(7);
            string = string.replaceAll("o", "0");
            string = string.replaceAll(" ", "");
            string = removeLetters(string);
            try {
                f = Float.parseFloat(string);
            } catch (NumberFormatException ignored){

            }
        }
        return f;
    }
}

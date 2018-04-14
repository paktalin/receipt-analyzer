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

    public static double similarity(String input, String expectedString) {
        JaroWinkler jaro = new JaroWinkler();
        double distance = jaro.distance(input, expectedString);
        //Log.d(TAG, input + " " + string + " " + distance);
        return distance;
    }

    public static boolean identical(String input, String expectedString) {
        return similarity(input, expectedString) < 0.07;
    }

    public static boolean similar(String input, String expectedString) {
        Log.d(TAG, input + " " + expectedString + " = " + similarity(input, expectedString));
        return similarity(input, expectedString) < 0.17;
    }

    public static float extractFloat(String string, int flagLength) {
        float f = -1;
        try {
            f = Float.parseFloat(string);
        } catch (NumberFormatException e) {
            Log.d(TAG, "we got here! " + string);
            string = string.substring(flagLength - 2);
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

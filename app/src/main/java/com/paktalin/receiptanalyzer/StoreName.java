package com.paktalin.receiptanalyzer;

import android.util.Log;

import com.paktalin.receiptanalyzer.similarity.JaroWinkler;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class StoreName {
    private static final String TAG = StoreName.class.getSimpleName();

    private static final double RATHER_SIMILAR = 0.16;
    private static final double VERY_SIMILAR = 0.08;

    static private String name = null;
    private static String input;

    static String getStoreName(String input) {
        StoreName.input = input;
        input = StringManager.clean(input);
        getFirstGroupName();
        if(name == null)
            getSecondGroupName();
        return name;
    }

    private static void getFirstGroupName() {
        String[] firstStrings = StringArrays.getFirstGroupFirstLines();
        for (int i = 0; i < firstStrings.length; i++) {
            if (calculateSimilarity(firstStrings[i]) < VERY_SIMILAR) {
                name = StringArrays.getFirstGroupNameByIndex(i);
                return;
            }
        }
    }

    private static void getSecondGroupName() {
        String[] firstStrings = StringArrays.getSecondGroupFirstLines();
        for (int i = 0; i < firstStrings.length; i++) {
            if (calculateSimilarity(firstStrings[i]) < RATHER_SIMILAR)
                name = StringArrays.getSecondGroupNameByIndex(i);
        }
    }

    private static double calculateSimilarity(String string) {
        JaroWinkler jaro = new JaroWinkler();
        double distance = jaro.distance(input, string);
        Log.d(TAG, string + " " + distance);
        return distance;
    }
}
package com.paktalin.receiptanalyzer;

import android.util.Log;

/**
 * Created by Paktalin on 19-Mar-18.
 */

class StringManager {
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

    static String clean(String string) {
        string = string.replaceAll(" ", "");
        string = string.replaceAll("\\.", "");
        return string;
    }

    static String getFirstLine(String string) {
        int index = string.indexOf('\n');
        return string.substring(0, index);
    }
}

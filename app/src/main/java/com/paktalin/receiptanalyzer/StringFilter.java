package com.paktalin.receiptanalyzer;

import com.paktalin.receiptanalyzer.similarity.JaroWinkler;
import java.util.ArrayList;

/**
 * Created by Paktalin on 20-Mar-18.
 */

public class StringFilter {
    private static final String TAG = StringFilter.class.getSimpleName();

    private static StringBuilder builder;
    private static ArrayList<String> list;

    public static Object[] filter(ArrayList<String> l) {
        StringFilter.list = l;
        String string = toString(list);
        string = string.toLowerCase();

        builder = new StringBuilder(string);
        filterCharSet();
        removeSpaces();
        removeWrecks();
        string = builder.toString();
        return new Object[]{string.split("\\n"), list};
    }

    private static String toString(ArrayList<String> list) {
        String result = "";
        for(String string : list) {
            result += string + "\n";
        }
        return result;
    }

    private static void filterCharSet() {
        String charSet = "abcdefghijklmnopqrstuvwxyz0123456789. \n";
        for(int i = 0; i < builder.length(); i++) {
            char c = builder.charAt(i);
            //if out of charset
            if (charSet.indexOf(c) == -1) {
                char replacement = tryToReplace(c);
                //if couldn't find replacement, then delete the char
                if(c == replacement) {
                    builder.deleteCharAt(i);
                    i--;
                } else {
                    builder.setCharAt(i, replacement);
                }
            }
        }
    }

    private static char tryToReplace(char c) {
        byte NO = -1;
        String toO = "òóôõö";
        String toA = "àáâãäåāă";
        String toU = "ùúûü";
        String toI = "ìíîïĩīĭ";
        String toS = "śŝş";
        String to8 = "&";
        String toDot = ",‚";
        if(toO.indexOf(c) != NO)
            return 'o';
        if(toA.indexOf(c) != NO)
            return 'a';
        if(toU.indexOf(c) != NO)
            return 'u';
        if(toI.indexOf(c) != NO)
            return 'i';
        if(toS.indexOf(c) != NO)
            return 's';
        if(toDot.indexOf(c) != NO)
            return '.';
        if(to8.indexOf(c) != NO)
            return '8';
        else return c;
    }

    private static void removeSpaces() {
        for(int i = 1; i < builder.length() - 1; i++) {
            char left = builder.charAt(i-1);
            char middle = builder.charAt(i);
            char right = builder.charAt(i+1);
            if((middle == ' ') && Character.isLetter(left) && Character.isLetter(right)) {
                builder.deleteCharAt(i);
                i--;
            }
        }
    }

    private static void removeWrecks() {
        boolean firstLinePassed = false;
        while (!firstLinePassed) {
            int endIndex = builder.indexOf("\n");
            String first = builder.substring(0, endIndex);
            if((first.length() > 5) && !rimiLogo(first)) {
                firstLinePassed = true;
            } else {
                builder.delete(0, endIndex + 1);
                list.remove(list.get(0));
            }
        }
    }

    private static boolean rimiLogo(String string) {
        String pattern = "supermarket";
        JaroWinkler jaro = new JaroWinkler();
        double distance = jaro.distance(string, pattern);
        return distance < 0.3;
    }
}
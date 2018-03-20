package com.paktalin.receiptanalyzer;

import android.util.Log;
import com.paktalin.receiptanalyzer.similarity.JaroWinkler;
import java.util.ArrayList;

/**
 * Created by Paktalin on 15.03.2018.
 * Second step.
 * This class filters the lines, performing:
 * - empty [crashed] strings removal
 * - simplifying the charset, keeping only standard english letters + numbers
 * - removing spaces between words [only] for simplicity
 */

class StringFilter {
    private static StringBuilder builder;
    private static final String TAG = StringFilter.class.getSimpleName();

    static String filter(ArrayList<String> list) {
        String string = toString(list);
        string = string.toLowerCase();
        builder = new StringBuilder(string);
        filterCharSet();
        removeSpaces();
        return builder.toString();
    }

    private static String toString(ArrayList<String> list) {
        boolean firstLinePassed = false;
        String result = "";
        for(int i = 0; i < list.size(); i++) {
            String string = list.get(i);
            //remove first crashed strings which could be detected from the supermarket's logo
            if (!firstLinePassed) {
                String withoutSpaces = string.replaceAll(" ", "");
                result += string + "\n";
                if (withoutSpaces.length() > 7) {
                    firstLinePassed = true;
                    if(!rimiLogo(withoutSpaces)){
                        result += string + "\n";
                        firstLinePassed = true;
                    } else {
                        list.remove(string);
                        i--;
                    }
                } else {
                    list.remove(string);
                    i--;
                }
            } else {
                result += string + "\n";
            }
        }
       return result;
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

   private static boolean rimiLogo(String string) {
        String pattern = "supermarket";
        JaroWinkler jaro = new JaroWinkler();
        double distance = jaro.distance(string, pattern);
        Log.d(TAG, string);
        Log.d(TAG, "distance: " + distance);
        return distance < 0.3;
    }
}

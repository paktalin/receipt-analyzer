package com.paktalin.receiptanalyzer;

import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by Paktalin on 15.03.2018.
 */

class Filter {
    private static final String TAG = Filter.class.getSimpleName();
    private ArrayList<String> lines;

    Filter(ArrayList<String> lines) {
        this.lines = lines;
        removeSpaces();
    }

   private void removeSpaces() {
       for (int j = 0; j < lines.size(); j++) {
           lines.set(j, lines.get(j).toLowerCase());
           StringBuilder sb = new StringBuilder(lines.get(j));
           for(int i = 1; i < sb.length() - 1; i++) {
               char left = sb.charAt(i-1);
               char middle = sb.charAt(i);
               char right = sb.charAt(i+1);
               if((middle == ' ') && Character.isLetter(left) && Character.isLetter(right)) {
                   sb.deleteCharAt(i);
                   i--;
               }
           }
           lines.set(j, sb.toString());
       }
       String name = new SimpleDateFormat("HH.mm.ss").format(new Date()) + ".txt";
       try {
           FileManager.saveTextFile(name, lines);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   private String ofCharSet(String string) {
        String charSet = "abcdefghijklmnopqrstuvwxyz0123456789.";
        StringBuilder stringBuilder = new StringBuilder(string);
        for(int i = 0; i < stringBuilder.length(); i++) {
            char c = stringBuilder.charAt(i);
            if (charSet.indexOf(c) == -1) {
                char replacement = tryToReplace(stringBuilder, i);
                if(c == replacement) {
                    stringBuilder.deleteCharAt(i);
                    i--;
                } else {
                    stringBuilder.setCharAt(i, replacement);
                }
            }
        }
        return stringBuilder.toString();
   }

    private char tryToReplace(StringBuilder stringBuilder, int index) {
        return ' ';
    }


}

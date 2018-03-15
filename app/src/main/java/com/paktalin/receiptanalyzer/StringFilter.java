package com.paktalin.receiptanalyzer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 * Created by Paktalin on 15.03.2018.
 */

class StringFilter {
    private static final String TAG = StringFilter.class.getSimpleName();
    private ArrayList<String> lines;

    private String data;

    StringFilter(ArrayList<String> lines) {
        this.lines = lines;
        removeSpaces();
    }

   private void removeSpaces() {
       for (int j = 0; j < lines.size(); j++) {
           lines.set(j, lines.get(j).toLowerCase());
           StringBuilder sb = new StringBuilder(lines.get(j));
           sb = filterCharSet(sb);
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
   }

   private StringBuilder filterCharSet(StringBuilder stringBuilder) {
        String charSet = "abcdefghijklmnopqrstuvwxyz0123456789. ";
        for(int i = 0; i < stringBuilder.length(); i++) {
            char c = stringBuilder.charAt(i);
            //if out of charset
            if (charSet.indexOf(c) == -1) {
                char replacement = tryToReplace(c);
                //if couldn't find replacement, then delete the char
                if(c == replacement) {
                    stringBuilder.deleteCharAt(i);
                    i--;
                } else {
                    stringBuilder.setCharAt(i, replacement);
                }
            }
        }
        return stringBuilder;
   }

   private char tryToReplace(char c) {
        byte NO = -1;
        String toO = "òóôõö";
        String toA = "àáâãäåāă";
        String toU = "ùúûü";
        String toI = "ìíîïĩīĭ";
        String toS = "śŝş";
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
        else return c;
    }

    private void save() {
        String name = new SimpleDateFormat("HH.mm.ss").format(new Date()) + ".txt";
        try {
            FileManager.saveTextFile(name, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

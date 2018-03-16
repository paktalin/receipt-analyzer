package com.paktalin.receiptanalyzer;

/**
 * Created by Paktalin on 15.03.2018.
 */

class StringFilter {
    private static StringBuilder sb;

    static String filter(String str) {
        String string = str.toLowerCase();
        sb = new StringBuilder(string);
        sb = filterCharSet();
        return removeSpaces();
    }

   private static String removeSpaces() {
       for(int i = 1; i < sb.length() - 1; i++) {
           char left = sb.charAt(i-1);
           char middle = sb.charAt(i);
           char right = sb.charAt(i+1);
           if((middle == ' ') && Character.isLetter(left) && Character.isLetter(right)) {
               sb.deleteCharAt(i);
               i--;
           }
       }
       return sb.toString();
   }

   private static StringBuilder filterCharSet() {
        String charSet = "abcdefghijklmnopqrstuvwxyz0123456789. \n";
        for(int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            //if out of charset
            if (charSet.indexOf(c) == -1) {
                char replacement = tryToReplace(c);
                //if couldn't find replacement, then delete the char
                if(c == replacement) {
                    sb.deleteCharAt(i);
                    i--;
                } else {
                    sb.setCharAt(i, replacement);
                }
            }
        }
        return sb;
   }

   private static char tryToReplace(char c) {
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
}

package com.paktalin.receiptanalyzer;


import android.util.Log;

import com.paktalin.receiptanalyzer.similarity.WeightedLevenshtein;

/**
 * Created by Paktalin on 10.03.2018.
 */

class StoreName {
    private static final String TAG = StoreName.class.getSimpleName();

    static String analyzeFirstString(String string) {
        string = getFirstString(string);
        string = removeNumbers(string);

        String[] supermarkets = {"maximaeestiou", "rimieestifoodas", "selver", "harjutarbijateuhistu"};
        WeightedLevenshtein levenshtein = new WeightedLevenshtein(Substitution.getInterface());
        for (String supermarket : supermarkets) {
            double distance = levenshtein.distance(string, supermarket);
            Log.d(TAG, string + " " + distance);
        }
        return null;
    }

    private static String removeNumbers(String string) {
        string = string.replaceAll(" ", "");
        string = string.replaceAll("regnr", "");
        return string.replaceAll("\\d","");
    }

    private static String getFirstString(String string) {
        int index = string.indexOf('\n');
        return string.substring(0, index);
    }






    /*private static final String TAG = StoreName.class.getSimpleName();
    private static String[] supermarkets = {"maxima", "rimi", "selver", "konsum", "comarket"};

    static String getSupermarket(String string) {
        firstString(string);
        for (String supermarket : supermarkets)
            if (string.contains(supermarket))
                return supermarket;
        String code = StringFilter.getRegCode(string);
        return getNameByCode(code);
    }

    private static String getNameByCode(String code) {
        String[][] codes = {
                {"10765896", "10263574", "10379733"},
                supermarkets};
        for(int i = 0; i < codes[0].length; i ++) {
            String storeCode = codes[0][i];
            if (code != null)
                if (code.equals(storeCode))
                    return codes[1][i];
        }
        return "Couldn't find the name registered";
    }

    static void firstString(String string) {
        String[] supermarkets = {"maximaeestiou", "rimieestifoodas", "selver", "harjutarbijateuhistu"};
        CharacterSubstitutionInterface substitutionalInterface = Substitution.substitute;
        WeightedLevenshtein levenshtein = new WeightedLevenshtein(substitutionalInterface);
        for (String supermarket : supermarkets) {
            double distance = levenshtein.distance(string, supermarket);
            Log.d(TAG, string + " " + distance);
        }
    }*/

}

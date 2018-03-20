package com.paktalin.receiptanalyzer;

import android.util.Log;

import com.paktalin.receiptanalyzer.similarity.JaroWinkler;

/**
 * Created by Paktalin on 10.03.2018.
 */

class StoreName {
    private static final String TAG = StoreName.class.getSimpleName();
    private static JaroWinkler jaro = new JaroWinkler();

    static String analyzeFirstString(String string) {
        string = getFirstString(string);
        string = clean(string);

        String[][] supermarkets = {
                StringArrays.getSupermarketTitles(),
                StringArrays.getSupermarketNames()};
        for(int i = 0; i < supermarkets[0].length; i++){
            String supermarket = supermarkets[0][i];
            double distance = jaro.distance(string, supermarket);
            Log.d(TAG, supermarket + " distance: " + distance);
            if (distance < 0.15){
                return supermarkets[1][i];
            }
        }
        double prismaDistance = minDistance(string, 0);
        double selverDistance = minDistance(string, 1);
        Log.d(TAG, "PRISMA: " + prismaDistance + "; SELVER: " + selverDistance);


        return null;
    }

    private static String clean(String string) {
        string = string.replaceAll(" ", "");
        string = string.replaceAll("\\.", "");
        string = string.replaceAll("\\d","");
        return string;
    }

    private static String getFirstString(String string) {
        int index = string.indexOf('\n');
        return string.substring(0, index);
    }

    private static double minDistance(String string, int supermarket) {
        double minDistance = 1;
        String[] titles = null;
        switch (supermarket){
            case 0:
                titles = StringArrays.getPrismaTitles();
                break;
            case 1:
                titles = StringArrays.getSelverTitles();
                break;
        }
        if(titles != null){
            for (String title : titles) {
                double distance = jaro.distance(string, title);
                if(distance < minDistance)
                    minDistance = distance;
            }
        } else {
            Log.d(TAG, "Null titles object!");
        }
        return minDistance;
    }
}

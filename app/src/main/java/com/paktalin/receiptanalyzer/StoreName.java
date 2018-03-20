package com.paktalin.receiptanalyzer;

import android.util.Log;

import com.paktalin.receiptanalyzer.similarity.JaroWinkler;

/**
 * Created by Paktalin on 10.03.2018.
 */

class StoreName {
    private static final String TAG = StoreName.class.getSimpleName();
    private static JaroWinkler jaro = new JaroWinkler();
    private static final double SMALL_DISTANCE = 0.1;

    static String getStore(String string) {
        string = getFirstString(string);
        string = clean(string);
        int PRISMA_CODE = 0, SELVER_CODE = 1;

        String[] supermarketTitles = StringArrays.getSupermarketTitles();
        for (int i = 0; i < supermarketTitles.length; i++) {
            String supermarketTitle = supermarketTitles[i];
            double distance = jaro.distance(string, supermarketTitle);
            if (distance < SMALL_DISTANCE)
                return StringArrays.getNameByIndex(i);
        }
        String prismaDistance = calculateDistance(string, PRISMA_CODE);
        if(prismaDistance == null) {
            String selverDistance = calculateDistance(string, SELVER_CODE);
            if(selverDistance == null)
                Log.d(TAG, "Couldn't identify the supermarket");
            else
                return selverDistance;

        } else
            return prismaDistance;
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

    private static String calculateDistance(String string, int supermarket) {
        double minDistance = 1;
        int minIndex = -1;
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
            for (int i = 0; i < titles.length; i++) {
                String title = titles[i];
                double distance = jaro.distance(string, title);
                if(distance < minDistance) {
                    minDistance = distance;
                    minIndex = i;
                }
            }
        } else {
            Log.d(TAG, "Null titles object!");
        }
        Log.d(TAG, minDistance + "");
        if (minDistance < SMALL_DISTANCE)
            return titles[minIndex];
        else
            return null;
    }
}

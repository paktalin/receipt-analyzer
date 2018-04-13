package com.paktalin.receiptanalyzer;

import android.content.SharedPreferences;
import static com.paktalin.receiptanalyzer.Supermarkets.*;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class DataKeeper {
    public static final String APP_PREFERENCES = "app_data";

    private static final String KEY_SELVER_COUNTER = "selver_counter";
    private static final String KEY_MAXIMA_COUNTER = "maxima_counter";
    private static final String KEY_RIMI_COUNTER = "rimi_counter";
    private static final String KEY_KONSUM_COUNTER = "konsum_counter";
    private static final String KEY_PRISMA_COUNTER = "prisma_counter";

    static int SELVER_COUNTER;
    static int MAXIMA_COUNTER;
    static int RIMI_COUNTER;
    static int KONSUM_COUNTER;
    static int PRISMA_COUNTER;
    private SharedPreferences mAppData;


    static String getKey(String supermarket) {
        switch (supermarket) {
            case SELVER:
                return KEY_SELVER_COUNTER;
            case MAXIMA:
                return KEY_MAXIMA_COUNTER;
            case RIMI:
                return KEY_RIMI_COUNTER;
            case KONSUM:
                return KEY_KONSUM_COUNTER;
            case PRISMA:
                return KEY_PRISMA_COUNTER;
            default:
                return null;
        }
    }
}

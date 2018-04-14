package com.paktalin.receiptanalyzer;

import static com.paktalin.receiptanalyzer.Supermarkets.*;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class DataKeeper {
    public static final String APP_PREFERENCES = "app_data";
    public static final String RETAILERS_PREFERENCES = "retailers";

    public static final String[] KEYS_SUPERMARKETS = {
            SELVER, MAXIMA, RIMI, KONSUM, PRISMA};

    public static final String[] SUPERMARKETS = {
            SELVER, MAXIMA, RIMI, KONSUM, PRISMA};

    public static String getKey(String supermarket) {
        switch (supermarket) {
            case SELVER:
                return KEYS_SUPERMARKETS[0];
            case MAXIMA:
                return KEYS_SUPERMARKETS[1];
            case RIMI:
                return KEYS_SUPERMARKETS[2];
            case KONSUM:
                return KEYS_SUPERMARKETS[3];
            case PRISMA:
                return KEYS_SUPERMARKETS[4];
            default:
                return null;
        }
    }
}

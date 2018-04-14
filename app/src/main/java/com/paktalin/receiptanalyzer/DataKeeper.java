package com.paktalin.receiptanalyzer;

import static com.paktalin.receiptanalyzer.Supermarkets.*;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class DataKeeper {
    public static final String APP_PREFERENCES = "app_data";

    private static final int SELVER_INDEX = 0;
    private static final int MAXIMA_INDEX = 1;
    private static final int RIMI_INDEX = 2;
    private static final int KONSUM_INDEX = 3;
    private static final int PRISMA_INDEX = 4;

    public static final String[] KEYS_SUPERMARKETS = {
            "selver_counter",
            "maxima_counter",
            "rimi_counter",
            "konsum_counter",
            "prisma_counter"};

    public static final String[] SUPERMARKETS = {
            "Selver",
            "Maxima",
            "Rimi",
            "Konsum",
            "Prisma"};

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

package com.paktalin.receiptanalyzer;

import android.content.SharedPreferences;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class DataKeeper {
    public static final String APP_PREFERENCES = "app_data";

    public static final String SELVER_COUNTER = "selver_counter";
    public static final String MAXIMA_COUNTER = "maxima_counter";
    public static final String RIMI_COUNTER = "rimi_counter";
    public static final String KONSUM_COUNTER = "konsum_counter";
    public static final String PRISMA_COUNTER = "prisma_counter";

    private SharedPreferences mAppData;

}

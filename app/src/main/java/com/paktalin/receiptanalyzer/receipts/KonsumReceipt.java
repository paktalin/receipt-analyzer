package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class KonsumReceipt extends Receipt{
    private static final String TAG = KonsumReceipt.class.getSimpleName();

    public KonsumReceipt(String[] lines) {
        super(lines);
        name = KONSUM;
        startLine = startLine("kaubanimetuskogushindsumma", 9) + 1;
        Log.d(TAG, startLine + "");
    }
}

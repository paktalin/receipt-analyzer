package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class RimiReceipt extends Receipt{
    private static final String TAG = RimiReceipt.class.getSimpleName();

    public RimiReceipt(String[] lines) {
        super(lines);
        name = RIMI;
        startLine = startLine("klient", 7);
        Log.d(TAG, startLine + "");
    }

    @Override
    int startLine(String startString, int number) {
        String string = lines[number - 1];
        string = string.substring(0, 6);
        Log.d(TAG, "cut string: " + string);
        if (StringManager.similar(string, startString))
            return number;
        return -1;
    }
}

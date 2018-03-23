package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class MaximaReceipt extends Receipt{
    private static final String TAG = MaximaReceipt.class.getSimpleName();

    public MaximaReceipt(String[] lines) {
        super(lines);
        name = MAXIMA;
        startLine = startLine("kviitungnr", 4);
    }

    @Override
    int startLine(String startString, int number) {
        String string = lines[number - 1];
        string = string.substring(0, 10);
        Log.d(TAG, "cut string: " + string);
        if (StringManager.identical(string, startString))
            return number;
        return -1;
    }
}

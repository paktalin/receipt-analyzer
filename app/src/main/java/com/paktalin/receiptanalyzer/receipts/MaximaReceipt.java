package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class MaximaReceipt extends Receipt {
    private static final String TAG = MaximaReceipt.class.getSimpleName();

    public MaximaReceipt(String[] lines) {
        super(lines);
        purchasesStart = startLine("kviitungnr", 4);
        purchasesEnd = endLine("kmtakmkmga", false);
    }

    @Override
    int startLine(String startString, int number) {
        String string = lines[number];
        string = string.substring(0, 10);
        Log.d(TAG, "cut string: " + string);
        if (StringManager.identical(string, startString))
            return number + 1;
        return -1;
    }
}

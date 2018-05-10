package com.paktalin.receiptanalyzer.receipts_data.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.managers.StringManager;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class MaximaReceipt extends Receipt {
    private static final String TAG = MaximaReceipt.class.getSimpleName();

    public MaximaReceipt(String[] lines) {
        super(lines);
        purchasesStart = startLine("kviitungnr", 4);
        purchasesEnd = endLine("kmtakmkmga", false);
        priceFlag = "kokkumaksta";
        calculateFinalPrice();
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

package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class RimiReceipt extends Receipt {
    private static final String TAG = RimiReceipt.class.getSimpleName();

    public RimiReceipt(String[] lines) {
        super(lines);
        purchasesStart = startLine("klient", 6);
        purchasesEnd = endLine("kaardimakse", false);
    }

    @Override
    int startLine(String startString, int number) {
        String string = StringManager.clean(lines[number]);
        string = string.substring(0, 7);
        if (StringManager.similar(string, startString))
            return number + 1;
        return -1;
    }

    @Override
    public String cutRetailersLine(String line) {
        Log.d(TAG, line.substring(4, line.length()));
        return line.substring(4, line.length());
    }
}

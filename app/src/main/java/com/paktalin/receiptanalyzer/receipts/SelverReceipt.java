package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringArrays;
import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class SelverReceipt extends Receipt {
    private static final String TAG = SelverReceipt.class.getSimpleName();

    public SelverReceipt(String[] lines) {
        name = SELVER;
        this.lines = lines;
        setAdditionalName(StringArrays.getSelverFirstStrings());
        address = StringArrays.getSelverAddress(supermarketIndex);
        startLine = StringManager.findLine(lines, "nimetuskogushindsumma", 8);
        Log.d(TAG, startLine + "");
    }
}

package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringArrays;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class SelverReceipt extends Receipt {
    private static final String TAG = SelverReceipt.class.getSimpleName();

    public SelverReceipt(String[] lines) {
        super(lines);
        name = SELVER;
        setSupermarketIndex(StringArrays.getSelverFirstStrings());
        additionalName = StringArrays.getSelverAdditionalName(supermarketIndex);
        address = StringArrays.getSelverAddress(supermarketIndex);
        startLine = startLine("nimetuskogushindsumma", 8);
        Log.d(TAG, startLine + "");
    }
}

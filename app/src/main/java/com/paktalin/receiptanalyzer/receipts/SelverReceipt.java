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
        setSupermarketIndex(cutFirstLine(), StringArrays.getSelverFirstStrings());
        additionalName = StringArrays.getSelverAdditionalName(supermarketIndex);
        address = StringArrays.getSelverAddress(supermarketIndex);
        startLine = startLine("nimetuskogushindsumma", 7);
        endLine = endLine("vahesumma", true);
        setPurchases();
    }

    private String cutFirstLine() {
        int length = lines[0].length();
        return lines[0].substring(0, length-6);
    }
}

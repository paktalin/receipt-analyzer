package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringArrays;
import com.paktalin.receiptanalyzer.StringManager;

import static com.paktalin.receiptanalyzer.StringManager.similar;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class SelverReceipt extends Receipt {
    private static final String TAG = SelverReceipt.class.getSimpleName();

    public SelverReceipt(String input) {
        name = "Selver";
        this.input = input;
        setAdditionalName(StringArrays.getSelverNames());
        address = StringArrays.getSelverAddress(supermarketIndex);
        startLine = StringManager.findLine(input, "nimetuskogushindsumma", 8);
        Log.d(TAG, startLine + "");
    }
}

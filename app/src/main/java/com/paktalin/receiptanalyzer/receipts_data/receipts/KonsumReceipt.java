package com.paktalin.receiptanalyzer.receipts_data.receipts;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class KonsumReceipt extends Receipt {
    private static final String TAG = KonsumReceipt.class.getSimpleName();

    public KonsumReceipt(String[] lines) {
        super(lines);
        Log.d(TAG, Arrays.toString(lines));
        purchasesStart = 10;
        purchasesEnd = endLine(new String[]{"maksta"}, true);
        setPrice(new String[]{"makstudkaardimakse", "maksta"});
    }
}

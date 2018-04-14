package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class PrismaReceipt extends Receipt {
    private static final String TAG = PrismaReceipt.class.getSimpleName();

    public PrismaReceipt(String[] lines){
        super(lines);
        purchasesStart = startLine("arvekviitung", 2) + 1;
        purchasesEnd = endLine("kokku", true);
    }

    @Override
    public String cutRetailersLine(String line) {
        String cut = line.substring(18, line.length() - 6);
        Log.d(TAG, cut);
        return cut;
    }
}

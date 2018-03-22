package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringArrays;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class PrismaReceipt extends Receipt{
    private static final String TAG = PrismaReceipt.class.getSimpleName();

    public PrismaReceipt(String[] lines){
        super(lines);
        name = PRISMA;
        setSupermarketIndex(StringArrays.getPrismaFirstStrings());
        additionalName = StringArrays.getPrismaAdditionalName(supermarketIndex);
        address = StringArrays.getPrismaAddress(supermarketIndex);
        startLine = startLine("arvekviitung", 3) + 2;
        Log.d(TAG, startLine + "");
    }
}

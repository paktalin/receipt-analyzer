package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringArrays;
import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class PrismaReceipt extends Receipt{
    private static final String TAG = PrismaReceipt.class.getSimpleName();

    public PrismaReceipt(String input){
        name = "Prisma";
        this.input = input;
        setAdditionalName(StringArrays.getPrismaNames());
        address = StringArrays.getPrismaAddress(supermarketIndex);
        startLine = StringManager.findLine(input, "", 0);
        Log.d(TAG, startLine + "");
    }
}

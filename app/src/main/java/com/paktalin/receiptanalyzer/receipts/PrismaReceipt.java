package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringArrays;
import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class PrismaReceipt extends Receipt{
    private static final String TAG = PrismaReceipt.class.getSimpleName();

    public PrismaReceipt(String[] lines){
        name = PRISMA;
        this.lines = lines;
        setAdditionalName(StringArrays.getPrismaFirstStrings());
        address = StringArrays.getPrismaAddress(supermarketIndex);
        startLine = StringManager.findLine(lines, "Arve/Kviitung", 3) + 2;
        Log.d(TAG, startLine + "");
    }
}

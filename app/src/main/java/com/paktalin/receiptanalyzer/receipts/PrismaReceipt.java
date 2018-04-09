package com.paktalin.receiptanalyzer.receipts;

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
    public String cutFirstLine(String firstLine) {
        return firstLine.substring(18, firstLine.length());
    }
}

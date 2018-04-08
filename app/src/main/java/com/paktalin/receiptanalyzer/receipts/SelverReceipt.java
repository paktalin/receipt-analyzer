package com.paktalin.receiptanalyzer.receipts;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class SelverReceipt extends Receipt {
    private static final String TAG = SelverReceipt.class.getSimpleName();

    public SelverReceipt(String[] lines) {
        super(lines);
        startLine = startLine("nimetuskogushindsumma", 7);
        endLine = endLine("vahesumma", true);
        purchases = extractPurchases();
    }

    @Override
    public String cutFirstLine(String firstLine) {
        int length = firstLine.length();
        return firstLine.substring(0, length-6);
    }
}

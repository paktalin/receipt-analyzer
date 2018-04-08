package com.paktalin.receiptanalyzer.receipts;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class SelverReceipt extends Receipt {
    private static final String TAG = SelverReceipt.class.getSimpleName();

    public SelverReceipt(String[] lines) {
        super(lines);
        name = SELVER;
        startLine = startLine("nimetuskogushindsumma", 7);
        endLine = endLine("vahesumma", true);
        purchases = extractPurchases();
    }

    private String cutFirstLine() {
        int length = lines[0].length();
        return lines[0].substring(0, length-6);
    }
}

package com.paktalin.receiptanalyzer.receipts_data.receipts;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class KonsumReceipt extends Receipt {
    private static final String TAG = KonsumReceipt.class.getSimpleName();

    public KonsumReceipt(String[] lines) {
        super(lines);
        purchasesStart = startLine("kaubanimetuskogushindsumma", 8) + 1;
        purchasesEnd = endLine("maksta", true);
        priceFlag = "maksta";
        calculateFinalPrice();
    }
}

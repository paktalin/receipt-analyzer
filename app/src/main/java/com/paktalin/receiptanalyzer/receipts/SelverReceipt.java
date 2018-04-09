package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

import java.util.Arrays;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class SelverReceipt extends Receipt {
    private static final String TAG = SelverReceipt.class.getSimpleName();

    public SelverReceipt(String[] lines) {
        super(lines);
        Log.d(TAG, "lines:\n" + Arrays.toString(lines));
        purchasesStart = startLine("nimetuskogushindsumma", 7);
        purchasesEnd = endLine("vahesumma", true);
        //purchases = extractPurchases();
        getFinalPrice("maksekaart", false);
    }

    @Override
    public String cutFirstLine(String firstLine) {
        int length = firstLine.length();
        return firstLine.substring(0, length-6);
    }

    private void getFinalPrice(String string, boolean takeLast) {
        String priceString = "";
        for (int i = purchasesEnd + 1; i < lines.length; i++) {
            String line = lines[i];
            Log.d(TAG, "line" + i + " = " + line);
            String cut;
            try {
                cut = line.substring(0, string.length());
                if (StringManager.similar(cut, string)) {
                    try {
                        priceString = line.substring(string.length() + 1, line.length());
                        if (!takeLast)
                            break;
                        Log.d(TAG, "priceString = " + priceString);
                    } catch (StringIndexOutOfBoundsException e) {
                        Log.e(TAG, "The line doesn't contain the price, we're looking for another one");
                        getFinalPrice("vahesumma", true);
                    }
                }
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
        finalPrice = StringManager.toFloat(priceString);
        Log.d(TAG, "finalPrice = " + finalPrice);
    }


}

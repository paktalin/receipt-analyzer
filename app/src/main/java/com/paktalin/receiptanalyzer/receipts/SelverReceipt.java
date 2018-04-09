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
        getFinalPrice("vahesumma");
    }

    @Override
    public String cutFirstLine(String firstLine) {
        int length = firstLine.length();
        return firstLine.substring(0, length-6);
    }

    private void getFinalPrice(String string) {
        for (int i = lines.length - 1; i > purchasesEnd; i--) {
            String line = lines[i];
            Log.d(TAG, "line" + i + " = " + line);
            String cut;
            try {
                cut = line.substring(0, string.length());
                if (StringManager.similar(cut, string)) {
                    try {
                        finalPrice = StringManager.extractFloat(line);
                        checkFinalPrice(string);
                        break;
                    } catch (StringIndexOutOfBoundsException e) {
                        checkFinalPrice(string);
                        return;
                    }
                }
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
        Log.d(TAG, "finalPrice = " + finalPrice);
    }

    private void checkFinalPrice(String string){
        if ((finalPrice == -1) && (string.equals("vahesumma"))) {
            Log.e(TAG, "The line doesn't contain the price, we're looking for another one");
            getFinalPrice("maksekaart");
        }
    }
}

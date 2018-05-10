package com.paktalin.receiptanalyzer.receipts_data.receipts;

import android.content.Context;
import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class RimiReceipt extends Receipt {
    private static final String TAG = RimiReceipt.class.getSimpleName();

    public RimiReceipt(String[] lines) {
        super(lines);
        Log.d(TAG, "lines:\n" + Arrays.toString(lines));
        purchasesStart = startLine("klient", 6);
        purchasesEnd = endLine("kaardimakse", false);
        priceFlag = "kokku";
        calculateFinalPrice();
        if (finalPrice == -1) {
            priceFlag = "kaardimakse";
            calculateFinalPrice();
        }
    }

    @Override
    int startLine(String startString, int number) {
        String string = StringManager.clean(lines[number]);
        string = string.substring(0, startString.length());
        if (StringManager.similar(string, startString))
            return number + 1;
        return -1;
    }

    @Override
    public String cutRetailersLine(String line) {
        try {
            return line.substring(4, line.length());
        }catch (Exception e) {
            return "";
        }
    }

    @Override
    void calculateFinalPrice() {
        for (int i = lines.length - 1; i > purchasesEnd; i--) {
            String line = lines[i];
            try {
                String cut = line.substring(0, priceFlag.length());
                if (StringManager.similar(cut, priceFlag)) {
                    finalPrice = StringManager.extractFloat(line, priceFlag.length());
                    break;
                }
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
    }

    @Override
    public void extractPurchases(Context context) {
        ArrayList<Purchase> purchases = new ArrayList<>();
        int beginning = -1;
        for (int i = purchasesStart; i <= purchasesEnd; i++) {
            String[] split = lines[i].split(" ");
            if (Purchase.purchase(split[0], "allah.")) {
                if (!containsPrice(split))
                    beginning = i;
                else {
                    Purchase purchase = new Purchase(context, lines[i], initialLines.get(i));
                    if (beginning != -1) {
                        purchase.setTitle(initialLines.get(beginning) + " " + purchase.getTitle());
                        beginning = -1;
                    }
                    purchases.add(purchase);
                }
            }
        }
        this.purchases = purchases.toArray(new Purchase[purchases.size()]);
    }

    private boolean containsPrice(String[] split) {
        try {
            Float.parseFloat(split[split.length - 1]);
        } catch (NumberFormatException e) {
            if (split.length > 1) {
                try {
                    Float.parseFloat(split[split.length - 2]);
                } catch (NumberFormatException ee) {
                    return false;
                }
            } else return false;
        }
        return true;
    }

}

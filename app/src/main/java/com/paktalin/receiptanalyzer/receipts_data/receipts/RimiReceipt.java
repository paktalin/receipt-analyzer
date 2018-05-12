package com.paktalin.receiptanalyzer.receipts_data.receipts;

import android.content.Context;
import android.util.Log;

import com.paktalin.receiptanalyzer.managers.StringManager;
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
        Log.d(TAG, Arrays.toString(lines));
        purchasesStart = 6;
        purchasesEnd = endLine(new String[]{"sinusoodustused", "kaardimakse"}, false);
        Log.d(TAG, "purchases end: " + purchasesEnd);
        setPrice(new String[]{"kaardimakse", "kokku", "eur", "summa", "kokkueur", "kaardimakseeur", "kokkukaardimakseeur"});
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
    void calculateFinalPrice(String priceFlag) {
        for (int i = lines.length - 1; i > purchasesEnd; i--) {
            try {
                String line = lines[i];
                if (StringManager.similar(line, priceFlag, StringManager.MAKE_EQUAL)) {
                    finalPrice = StringManager.extractFinalPriceFloat(line, priceFlag.length());
                    break;
                }
            } catch (StringIndexOutOfBoundsException ignored) {}
        }
    }

    @Override
    public void extractPurchases(Context context) {
        ArrayList<Purchase> purchases = new ArrayList<>();
        int beginning = -1;
        for (int i = purchasesStart; i <= purchasesEnd; i++) {
            String[] split = lines[i].split(" ");
            if (isPurchase(split[0])) {
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

    private boolean isPurchase(String string) {
        String[] notPurchases = new String[]
                {"allah.", "www.rimi.ee", "klient944038XXXXXXXXX4461", "klient", "sinusoodustused",
                        "oledsaastnud", "muuk", "kviitung", "terminal"};
        for (String notPurchase : notPurchases)
            if (!Purchase.purchase(string, notPurchase))
                return false;
        return true;
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

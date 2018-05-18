package com.paktalin.receiptanalyzer.receipts_data.receipts;


import android.content.Context;

import com.paktalin.receiptanalyzer.receipts_data.Purchase;

import java.util.ArrayList;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class MaximaReceipt extends Receipt {
    private static final String TAG = MaximaReceipt.class.getSimpleName();

    public MaximaReceipt(String[] lines) {
        super(lines);
        purchasesStart = 4;
        purchasesEnd = endLine(new String[] {"ilmakmta", "kmtakmkmga"}, false);

        setPrice(new String[]{"makstudpangakaart", "kokkumaksta", "summa"});
    }


    @Override
    public void extractPurchases(Context context) {
        ArrayList<Purchase> purchases = new ArrayList<>();
        for (int i = purchasesStart; i <= purchasesEnd; i++) {
            String line = lines[i];
            if (isPurchase(line.split(" ")[0])) {
                Purchase purchase = new Purchase(context, line, initialLines.get(i));
                purchases.add(purchase);
            }
        }
        this.purchases = purchases.toArray(new Purchase[purchases.size()]);
    }

    private boolean isPurchase(String string) {
        String[] notPurchases = new String[]
                {"tsekk", "allahindlus", "kviitungnr", "kmtakmkmga"};
        for (String notPurchase : notPurchases)
            if (!Purchase.purchase(string, notPurchase))
                return false;
        return true;
    }
}

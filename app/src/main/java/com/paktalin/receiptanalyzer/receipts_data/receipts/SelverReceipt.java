package com.paktalin.receiptanalyzer.receipts_data.receipts;

import android.content.Context;
import android.util.Log;

import com.paktalin.receiptanalyzer.managers.StringManager;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class SelverReceipt extends Receipt {
    private static final String TAG = SelverReceipt.class.getSimpleName();
    private boolean byCard = false;
    private final double IDENTICAL = 0.07;
    private final double SIMILAR = 0.17;

    public SelverReceipt(String[] lines) {
        super(lines);
        Log.d(TAG, "lines:\n" + Arrays.toString(lines));
        purchasesStart = 8;
        purchasesEnd = endLine("vahesumma", true);
        setPrice(new String[]{"maksekaart", "summa", "vahesumma"});
    }

    @Override
    public String cutRetailersLine(String line) {
        int length = line.length();
        return line.substring(0, length-6);
    }

    @Override
    void calculateFinalPrice(String priceFlag) {
        finalPrice = getPayment("maksekaart", SIMILAR);
        if(finalPrice < 0) {
            finalPrice = getPayment("CASH_PAYMENT", SIMILAR);
            if (byCard || (finalPrice < 0)) //if the payment reading was not successful
                finalPrice = getSum();
        } else if (finalPrice > 100) {
            float checkPrice = getSum();
            if (checkPrice != finalPrice) {
                finalPrice = checkPrice;
            }
        }
    }

    private float getPayment(String payment, double similarity) {
        float price = -1;
        boolean cardPayment = payment.equals("maksekaart");
        for (int i = lines.length - 1; i > purchasesEnd; i--) {
            String line = lines[i];
            try {
                String cut = line.substring(0, payment.length());
                if (StringManager.similarity(cut, payment) < similarity) {
                    if (cardPayment)
                        byCard = true;
                    price = StringManager.extractFinalPriceFloat(line, payment.length());
                    break;
                }
            }catch (StringIndexOutOfBoundsException ignored) {
            }
        }
        return price;
    }
    private float getSum() {
        String bonusPointString = "boonusmakse";
        float bonusPoints = getPayment(bonusPointString, IDENTICAL);
        float sum = getPayment("vahesumma", SIMILAR);
        if (!(bonusPoints  < 0))
            sum = sum - bonusPoints;
        return sum;
    }

    @Override
    public void extractPurchases(Context context) {
        ArrayList<Purchase> purchases = new ArrayList<>();
        for (int i = purchasesStart; i <= purchasesEnd; i++) {
            String[] split = lines[i].split(" ");
            if (isPurchase(split[0])) {
                Purchase purchase = new Purchase(context, lines[i], initialLines.get(i));
                purchases.add(purchase);
            }
        }
        this.purchases = purchases.toArray(new Purchase[purchases.size()]);
    }

    private boolean isPurchase(String string) {
        String[] notPurchases = new String[]
                {"nimetuskogushindsumma.", "pusikliendivoit"};
        for (String notPurchase : notPurchases)
            if (!Purchase.purchase(string, notPurchase))
                return false;
        return true;
    }
}

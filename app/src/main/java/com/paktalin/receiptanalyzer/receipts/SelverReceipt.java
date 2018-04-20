package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

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
        purchasesStart = startLine("nimetuskogushindsumma", 7);
        purchasesEnd = endLine("vahesumma", true);
        calculateFinalPrice();
    }

    @Override
    public String cutRetailersLine(String line) {
        int length = line.length();
        return line.substring(0, length-6);
    }

    @Override
    void calculateFinalPrice() {
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
                    price = StringManager.extractFloat(line, payment.length());
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
}

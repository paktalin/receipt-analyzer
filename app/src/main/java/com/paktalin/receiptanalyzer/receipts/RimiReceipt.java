package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class RimiReceipt extends Receipt {
    private static final String TAG = RimiReceipt.class.getSimpleName();

    public RimiReceipt(String[] lines) {
        super(lines);
        purchasesStart = startLine("klient", 6);
        purchasesEnd = endLine("kaardimakse", false);
        calculateFinalPrice();
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
        Log.d(TAG, line.substring(4, line.length()));
        return line.substring(4, line.length());
    }

    @Override
    void calculateFinalPrice() {
        finalPrice = getPayment("kokku");
    }

    private float getPayment(String payment) {
        float price = -1;
        for (int i = lines.length - 1; i > purchasesEnd; i--) {
            String line = lines[i];
            try {
                String cut = line.substring(0, payment.length());
                if (StringManager.similar(cut, payment)) {
                    price = StringManager.extractFloat(line);
                    break;
                }
            }catch (StringIndexOutOfBoundsException ignored) {
            }
        }
        return price;
    }
}

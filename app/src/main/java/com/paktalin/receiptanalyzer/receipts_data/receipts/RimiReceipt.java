package com.paktalin.receiptanalyzer.receipts_data.receipts;

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
        priceFlag = "kokku";
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
        for (int i = lines.length - 1; i > purchasesEnd; i--) {
            String line = lines[i];
            try {
                String cut = line.substring(0, priceFlag.length());
                if (StringManager.similar(cut, priceFlag)) {
                    finalPrice = StringManager.extractFloat(line, priceFlag.length());
                    break;
                }
            }catch (StringIndexOutOfBoundsException ignored) {
            }
        }
    }
}

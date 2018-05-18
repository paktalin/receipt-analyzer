package com.paktalin.receiptanalyzer.receipts_data.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.managers.StringManager;

import java.util.Arrays;

import static com.paktalin.receiptanalyzer.managers.StringManager.MAKE_EQUAL;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class PrismaReceipt extends Receipt {
    private static final String TAG = PrismaReceipt.class.getSimpleName();

    public PrismaReceipt(String[] lines){
        super(lines);
        Log.d(TAG, Arrays.toString(lines));
        purchasesStart = 4;
        purchasesEnd = endLine(new String[]{"kokku"}, true);
        setPrice(new String[]{"kokku", "charge"});
    }

    @Override
    public String cutRetailersLine(String line) {
        if (line.length() - 6 > 18)
            return line.substring(18, line.length() - 6);
        return line;
    }

    @Override
    void calculateFinalPrice(String priceFlag) {
        for (String line : lines) {
            if (StringManager.similar(line, priceFlag, MAKE_EQUAL)) {
                finalPrice = StringManager.extractFinalPriceFloat(line, priceFlag.length());
                break;
            }
        }
    }
}

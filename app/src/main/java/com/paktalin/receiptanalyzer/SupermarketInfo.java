package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.util.Log;

import com.paktalin.receiptanalyzer.receipts.Receipt;

import static com.paktalin.receiptanalyzer.Supermarkets.*;


/**
 * Created by Paktalin on 08/04/2018.
 */

class SupermarketInfo {
    private static final String TAG = SupermarketInfo.class.getSimpleName();

    private static String supermarket;
    private static int index = -1;
    private static Receipt receipt;

    static Receipt setInfo(Receipt receipt, Context context) {
        SupermarketInfo.receipt = receipt;
        supermarket = receipt.getSupermarket();

        if (!supermarket.equals(MAXIMA)) {
            String retailersLine = getRetailersLine();

            setIndex(receipt.cutRetailersLine(retailersLine), context);
            if (!(index == -1)) {
                String retailer = getRetailer(context);
                String address = getAddress(context);
                receipt.setRetailer(retailer);
                receipt.setAddress(address);
            }
        }
        return receipt;
    }

    private static void setIndex(String firstLine, Context context) {
        String firstLinesPath = supermarket + "/first_lines";
        String[] firstLines = FileManager.getStringFromTextFile(context, firstLinesPath);

        for (int i = 0; i < firstLines.length; i++) {
            String fL = firstLines[i];
            if (StringManager.similar(firstLine, fL)){
                index = i;
                Log.d(TAG, "index: " + i);
                return;
            }
        }
    }

    private static String getRetailer(Context context) {
        String retailersPath = supermarket + "/retailers";
        String[] retailers = FileManager.getStringFromTextFile(context, retailersPath);
        return retailers[index];
    }

    private static String getAddress(Context context) {
        String addressesPath = supermarket + "/addresses";
        String[] addresses = FileManager.getStringFromTextFile(context, addressesPath);
        return addresses[index];
    }

    static private String getRetailersLine() {
        String result = receipt.getLine(0);
        if (supermarket.equals(RIMI))
            result = receipt.getLine(3);
        return StringManager.clean(result);
    }
}

package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.util.Log;

import com.paktalin.receiptanalyzer.receipts.KonsumReceipt;
import com.paktalin.receiptanalyzer.receipts.MaximaReceipt;
import com.paktalin.receiptanalyzer.receipts.PrismaReceipt;
import com.paktalin.receiptanalyzer.receipts.Receipt;
import com.paktalin.receiptanalyzer.receipts.RimiReceipt;
import com.paktalin.receiptanalyzer.receipts.SelverReceipt;


/**
 * Created by Paktalin on 08/04/2018.
 */

class SupermarketInfo {
    private static final String TAG = SupermarketInfo.class.getSimpleName();

    private static String supermarket;
    private static int index = -1;

    static Receipt setInfo(Receipt receipt, Context context) {
        String firstLine = receipt.getFirstLine();
        supermarket = receipt.getSupermarket();

        setIndex(receipt.cutFirstLine(firstLine), context);
        if (!(index == -1)) {
            String retailer = getRetailer(context);
            String address = getAddress(context);
            receipt.setRetailer(retailer);
            receipt.setAddress(address);
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
}

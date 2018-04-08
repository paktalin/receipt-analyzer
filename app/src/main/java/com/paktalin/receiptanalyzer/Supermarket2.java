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

class Supermarket2 {
    private static final String TAG = Supermarket2.class.getSimpleName();

    private static String supermarket;
    private static String firstLine;
    private static String retailer, address;
    private static  int index;
    String[] linesArray;

    Supermarket2(String[] lines, Context context) {
        linesArray = lines;
        firstLine = StringManager.clean(linesArray[0]);
        supermarket = StoreName.getStoreName(firstLine);

        setIndex(context);
        setRetailer(context);
        setAddress(context);
    }

    private void setIndex(Context context) {
        String firstLinesPath = supermarket + "/first_lines";
        String[] firstLines = FileManager.getStringFromTextFile(context, firstLinesPath);
        index = getSupermarketIndex(firstLines);
    }

    private int getSupermarketIndex(String[] array) {
        for (int i = 0; i < array.length; i++) {
            String anArray = array[i];
            if (StringManager.identical(firstLine, anArray)){
                return i;
            }
        }
        return -1;
    }

    private void setRetailer(Context context) {
        String retailersPath = supermarket + "/retailers";
        String[] retailers = FileManager.getStringFromTextFile(context, retailersPath);
        retailer = retailers[index];
        Log.d(TAG, "retailer: " + retailer);
    }

    private void setAddress(Context context) {
        String addressesPath = supermarket + "/addresses";
        String[] addresses = FileManager.getStringFromTextFile(context, addressesPath);
        address = addresses[index];
    }

    Receipt createReceipt() {
        Receipt receipt = null;
        switch (supermarket){
            case "Selver":
                receipt = new SelverReceipt(linesArray);
                break;
            case "Prisma":
                receipt = new PrismaReceipt(linesArray);
                break;
            case "Rimi":
                receipt = new RimiReceipt(linesArray);
                break;
            case "Konsum":
                receipt = new KonsumReceipt(linesArray);
                break;
            case "Maxima":
                receipt = new MaximaReceipt(linesArray);
                break;
        }
        receipt.setRetailer(retailer);
        receipt.setAddress(address);
        return receipt;
    }

    //TODO fix the issue with cutting strings for getting retailers
}

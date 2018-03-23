package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

import java.util.Arrays;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class Receipt {
    private static final String TAG = Receipt.class.getSimpleName();

    String name = null;
    String additionalName = "";
    String address = "";
    int startLine, endLine;
    int supermarketIndex = -1;
    String[] lines, purchases;


    final String
            SELVER = "Selver",
            PRISMA = "Prisma",
            MAXIMA = "Maxima",
            RIMI = "Rimi",
            KONSUM = "Konsum";


    Receipt(String[] lines) {
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public String getAddress() {
        return address;
    }

    void setSupermarketIndex(String firstLine, String[] array) {
        Log.d(TAG, "setting index");
        firstLine = StringManager.clean(firstLine);
        for (int i = 0; i < array.length; i++) {
            String anArray = array[i];
            if (StringManager.identical(firstLine, anArray)){
                supermarketIndex = i;
                break;
            }
        }
    }

    /**
     * @param startString the starting string of this particular supermarket
     * @param number the number of line where the line is supposed to be
     */
    int startLine(String startString, int number) {
        if (StringManager.similar(lines[number], startString))
            return number + 1;
        return -1;
    }

    int endLine(String endLine, boolean removeNumbers) {
        for (int i = startLine + 1; i < lines.length; i++) {
            String line = lines[i];
            if (removeNumbers)
                line = StringManager.removeNumbers(line);
            Log.d(TAG, i + " " + line);
            if (StringManager.identical(line, endLine))
                return i - 1;
        }
        return -1;
    }

    void setPurchases() {
        purchases = Arrays.copyOfRange(lines, startLine, endLine + 1);
    }

    public void logReceipt() {
        Log.d(TAG, "\n\n______RECEIPT______");
        Log.d(TAG, "Name = " + name);
        Log.d(TAG, "additionalName = " + additionalName);
        Log.d(TAG, "address = " + address);
        //Log.d(TAG, "startLine = " + startLine + "");
        //Log.d(TAG, "endLine = " + endLine + "");
        Log.d(TAG, "\n_PURCHASES_");
        log(Arrays.toString(purchases));
        //Log.d(TAG, Arrays.toString(lines));
    }

    private void log(String string) {
        Log.d(TAG, string);
    }
}

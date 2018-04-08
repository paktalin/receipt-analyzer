package com.paktalin.receiptanalyzer.receipts;

import android.database.Cursor;
import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class Receipt {
    private static final String TAG = Receipt.class.getSimpleName();

    String name = null;
    String retailer = "";
    String address = "";
    int startLine, endLine;
    int supermarketIndex = -1;
    String[] lines;
    private String[] purchasesStrings;
    ArrayList<Purchase> purchases;

    final static String
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

    void setSupermarketIndex(String firstLine, String[] array) {
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
            if (StringManager.identical(line, endLine))
                return i - 1;
        }
        return -1;
    }

    void setPurchases() {
        purchasesStrings = Arrays.copyOfRange(lines, startLine, endLine + 1);
    }

    ArrayList<Purchase> extractPurchases() {
        setPurchases();
        ArrayList<Purchase> purchases = new ArrayList<>();
        for (String string : purchasesStrings){
            String[] string_arr = string.split(" ");
            Log.d(TAG, Arrays.toString(string_arr));
            if (Purchase.purchase(string_arr)) {
                Purchase purchase = new Purchase(new ArrayList<>(Arrays.asList(string_arr)));
                purchases.add(purchase);
            } else {
                Log.d(TAG, Arrays.toString(string_arr) + " is not a purchase");
            }
        }
        return purchases;
    }

    public void logReceipt() {
        Log.d(TAG, "\n\n______RECEIPT______");
        Log.d(TAG, "Name = " + name);
        Log.d(TAG, "retailer = " + retailer);
        Log.d(TAG, "address = " + address);
        //Log.d(TAG, "startLine = " + startLine + "");
        //Log.d(TAG, "endLine = " + endLine + "");
        Log.d(TAG, "_PURCHASES_");
        //Log.d(TAG, Arrays.toString(lines));
        for (Purchase p : purchases){
            p.purchaseInfo();
        }
    }

    public String getInfo() {
        StringBuilder info;
        info = new StringBuilder("______RECEIPT______" +
                "\nName = " + name +
                "\nretailer = " + retailer +
                "\naddress = " + address +
                "\n\n_PURCHASES_");
        for (Purchase p : purchases){
            info.append(p.purchaseInfo());
        }
        return info.toString();
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

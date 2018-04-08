package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Paktalin on 08/04/2018.
 */

public class Receipt {
    private static final String TAG = Receipt.class.getSimpleName();

    String[] lines;
    private String supermarket;
    private String retailer, address;
    int startLine, endLine;
    ArrayList<Purchase> purchases;

    Receipt(String[] lines) {
        this.lines = lines;
    }

    public String cutFirstLine(String firstLine) {
        return firstLine;
    }

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

    ArrayList<Purchase> extractPurchases() {
        ArrayList<Purchase> purchases = new ArrayList<>();
        String[] purchasesStrings = Arrays.copyOfRange(lines, startLine, endLine + 1);
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
        Log.d(TAG, "Name = " + supermarket);
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
                "\nName = " + supermarket +
                "\nretailer = " + retailer +
                "\naddress = " + address +
                "\n\n_PURCHASES_");
        /*for (Purchase p : purchases){
            info.append(p.purchaseInfo());
        }*/
        return info.toString();
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setSupermarket(String supermarket) {
        this.supermarket = supermarket;
    }
}

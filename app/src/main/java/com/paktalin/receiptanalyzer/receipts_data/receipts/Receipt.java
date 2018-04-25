package com.paktalin.receiptanalyzer.receipts_data.receipts;

import android.content.Context;
import android.util.Log;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.StringManager;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;

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
    int purchasesStart, purchasesEnd;
    float finalPrice = -1;
    String priceFlag;
    private ArrayList<Purchase> purchases;
    private ArrayList<String> initialLines;
    private long date;
    private long ID;

    Receipt(String[] lines) {
        this.lines = lines;
    }

    public Receipt() {}

    public String cutRetailersLine(String line) {
        return line;
    }

    int startLine(String startString, int number) {
        //TODO check other lines if startLine isn't found
        if (StringManager.similar(lines[number], startString))
            return number + 1;
        return -1;
    }
    int endLine(String endLine, boolean removeNumbers) {
        for (int i = purchasesStart + 1; i < lines.length; i++) {
            String line = lines[i];
            if (removeNumbers)
                line = StringManager.clean(line);
                line = StringManager.removeNumbers(line);
            if (StringManager.similar(line, endLine))
                return i - 1;
        }
        return -1;
    }

    public void extractPurchases(Context context) {
        purchases = new ArrayList<>();
        for (int i = purchasesStart; i <= purchasesEnd; i++)
            if (Purchase.purchase(lines[i].split(" ")[0])) {
                Purchase purchase = new Purchase(context, lines[i], initialLines.get(i));
                purchases.add(purchase);
            }
    }

    public void logReceipt() {
        Log.d(TAG, "\n\n______RECEIPT______");
        Log.d(TAG, "Name = " + supermarket);
        Log.d(TAG, "retailer = " + retailer);
        Log.d(TAG, "address = " + address);
        Log.d(TAG, "final price = " + finalPrice);
        //Log.d(TAG, "purchasesStart = " + purchasesStart + "");
        //Log.d(TAG, "purchasesEnd = " + purchasesEnd + "");
    }

    void calculateFinalPrice() {
        for (String line : lines) {
            try {
                String cut = line.substring(0, priceFlag.length());
                if (StringManager.similar(cut, priceFlag)) {
                    finalPrice = StringManager.extractFloat(line, priceFlag.length());
                    break;
                }
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
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
    public void setInitialLines(ArrayList<String> initialLines) {
        this.initialLines = initialLines;
    }
    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public void setID(long ID) {
        this.ID = ID;
    }

    public String getSupermarket() {
        return supermarket;
    }
    public String getRetailer() {
        return retailer;
    }
    public String getAddress() {
        return address;
    }
    public float getFinalPrice() {
        return finalPrice;
    }
    public String getLine(int index) {
        return lines[index];
    }
    public ArrayList<Purchase> getPurchases() {
        return purchases;
    }
    public long getDate() {
        return date;
    }
    public long getID() {
        return ID;
    }
}

package com.paktalin.receiptanalyzer.receipts_data.receipts;

import android.content.Context;

import com.paktalin.receiptanalyzer.managers.StringManager;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;

import java.util.ArrayList;

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
    Purchase[] purchases;
    ArrayList<String> initialLines;
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
        if (StringManager.similar(lines[number], startString, true))
            return number + 1;
        return -1;
    }

    int endLine(String endLine, boolean removeNumbers) {
        for (int i = purchasesStart + 1; i < lines.length; i++) {
            String line = lines[i];
            if (removeNumbers)
                line = StringManager.clean(line);
                line = StringManager.removeNumbers(line);
            if (StringManager.similar(line, endLine, true))
                return i - 1;
        }
        return lines.length - 1;
    }

    int endLine(String[] endLines, boolean removeNumbers) {
        for (String endLine : endLines)
            for (int i = purchasesStart + 1; i < lines.length; i++) {
                String line = StringManager.clean(lines[i]);
                if (removeNumbers)
                    line = StringManager.removeNumbers(line);
                if (StringManager.similar(line, endLine, true))
                    return i - 1;
            }
        return -1;
    }

    public void extractPurchases(Context context) {
        ArrayList<Purchase> purchases = new ArrayList<>();
        for (int i = purchasesStart; i <= purchasesEnd; i++) {
            Purchase purchase = new Purchase(context, lines[i], initialLines.get(i));
            purchases.add(purchase);
        }
        this.purchases = purchases.toArray(new Purchase[purchases.size()]);
    }

    void calculateFinalPrice() {
        for (String line : lines) {
            try {
                if (StringManager.similar(line, priceFlag, true)) {
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
    public Purchase[] getPurchases() {
        return purchases;
    }
    public long getDate() {
        return date;
    }
    public long getID() {
        return ID;
    }
}

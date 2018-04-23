package com.paktalin.receiptanalyzer.receipts_data;

import android.content.Context;
import android.util.Log;

import static com.paktalin.receiptanalyzer.FileManager.getStringFromTextFile;
import static com.paktalin.receiptanalyzer.StringManager.similar;

/**
 * Created by Paktalin on 23/04/2018.
 */

public class Purchase {
    private static final String TAG = Purchase.class.getSimpleName();

    private String title = null;
    private String category = null;
    private float price = 0;

    public Purchase(Context context, String line, String initial) {
        title = initial;
        setCategory(context, line);
        extractPrice(line.split(" "));
    }

    public static boolean purchase(String string){
        return !similar(string, "pusikliendivoit");
    }

    private void extractPrice(String[] items) {
        for (int i = items.length - 1; i >= 0; i--) {
            if (casted(items[i]))
                return;
        }
    }

    private boolean casted(String string) {
        try {
            price = Float.parseFloat(string);
            return true;
        } catch (NumberFormatException e) {
            string = string.replaceAll("o", "0");
            string = string.replaceAll("g", "9");
            string = string.replaceAll(" ", "");
            try {
                price = Float.parseFloat(string);
                return true;
            } catch (NumberFormatException ignored){
            }
        }
        return false;
    }

    private void setCategory(Context context, String string) {
        String[] tags = getStringFromTextFile(context, "categories/tags");
        String currentCategory = null;
        for (String line : tags) {
            if (line.substring(0, 2).equals("__"))
                currentCategory = line.substring(2, line.length());
            else if (string.contains(line))
                category = currentCategory;
        }
    }

    public void purchaseInfo(){
        Log.d(TAG, "title: " + title +
                "; price: " + price +
                "; category: " + category + "\n");
    }

    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }
    public float getPrice() {
        return price;
    }
}

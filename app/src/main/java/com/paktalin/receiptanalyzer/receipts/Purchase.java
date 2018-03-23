package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringManager;

import java.util.Arrays;

import static com.paktalin.receiptanalyzer.receipts.Receipt.SELVER;

/**
 * Created by Paktalin on 23-Mar-18.
 */

class Purchase {
    private static final String TAG = Purchase.class.getSimpleName();

    private String title;
    private float weight_amount, price, sum;

    Purchase(String[] items, String store){
        //Log.d(TAG, Arrays.toString(items));
        int length = items.length;

        switch (store) {
            case SELVER:
                if(length == 6) {
                    if (containsChar(items[2]))
                        extract(items[0] + items[1] + items[2], items[3], items[4], items[5]);
                }
                if(length == 5) {
                    if (containsChar(items[1]))
                        extract(items[0] + items[1], items[2], items[3], items[4]);
                    else if(containsChar(items[2]))
                        extract(items[0] + items[1] + items[2], "1", items[3], items[4]);
                }
                if (length == 4) {
                    if (containsChar(items[1]))
                        extract(items[0] + items[1], "1", items[2], items[3]);
                    else
                        extract(items[0], items[1], items[2], items[3]);
                }
                else if(length == 3)
                    extract(items[0], "1", items[1], items[2]);
        }
        printPurchase();
    }

    private void extract(String t, String w_a, String p, String s){
        title = t;
        weight_amount = toFloat(w_a);
        price = toFloat(p);
        sum = toFloat(s);
    }

    private float toFloat(String string){
        float f = -1;
        try {
            f = Float.parseFloat(string);
        }catch (NumberFormatException e){
            e.printStackTrace();
            String cut = string.substring(2, string.length());
            int index = cut.indexOf('.') + 2;
            String first = string.substring(0, index - 2);
            String second = string.substring(index - 1, string.length());
            Log.d(TAG, "first: " + first + " ; second: " + second);
        }

        return f;
    }

    static boolean purchase(String[] string){
        return !StringManager.similar(string[0], "pusikliendivoit");
    }

    void printPurchase(){
        Log.d(TAG, "title: " + title + "; weight/amount: " + weight_amount +
                "; price: " + price + "; sum: " + sum);
    }

    private static boolean containsChar(String string){
        return string.matches(".*[a-z].*");
    }
}

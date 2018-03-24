package com.paktalin.receiptanalyzer.receipts;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Paktalin on 24-Mar-18.
 */

class Purchase2 {
    private static final String TAG = Purchase2.class.getSimpleName();

    private String title = null;
    private float amount = -1;
    private float price = -1;
    float sum = -1;
    private ArrayList<String> items;
    private String last;

    Purchase2 (ArrayList<String> items) {
        this.items = items;
        while(items.size() > 0) {
            last = last();
            if (!set(sum)) {
                sum = tryToCast();
                Log.d(TAG, "Sum: "+ String.valueOf(sum));
                checkIfCasted(sum);
            } else if (!set(price)) {
                price = tryToCast();
                Log.d(TAG, "Price: "+ String.valueOf(price));
                checkIfCasted(price);
            } else if(!set(amount)) {
                amount = tryToCast();
                Log.d(TAG, "Amount: " + String.valueOf(amount));
                checkIfCasted(amount);
            } else {
                if (!(items.size() == 1)) {
                    for (String string : items) {
                        title += string;
                        items.remove(string);
                    }
                } else {
                    title = last;
                    items.remove(last);
                }
                Log.d(TAG, "Title: " + title);
            }
        }
    }

    private boolean set(float f){
        return f != -1;
    }

    private float tryToCast(){
        try {
            return Float.parseFloat(last);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String last() {
        return items.get(items.size()-1);
    }

    private void checkIfCasted(float field){
        if (set(field))
            items.remove(last);
        else
            tryToSplit(last);

    }

    private void tryToSplit(String string) {
        String cut = string.substring(2, string.length());
        int index = cut.indexOf('.') + 2;
        String first = string.substring(0, index - 2);
        String second = string.substring(index - 1, string.length());
        items.remove(last);
        items.add(first);
        items.add(second);
        Log.d(TAG, "first: " + first + " ; second: " + second);
    }

    void printPurchase(){
        Log.d(TAG, "title: " + title + "; weight/amount: " + amount +
                "; price: " + price + "; sum: " + sum);
    }
}

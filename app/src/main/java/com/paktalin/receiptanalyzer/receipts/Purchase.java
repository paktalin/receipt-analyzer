package com.paktalin.receiptanalyzer.receipts;

import android.content.Context;
import android.util.Log;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.StringManager;

import java.util.ArrayList;

/**
 * Created by Paktalin on 24-Mar-18.
 */

public class Purchase {
    private static final String TAG = Purchase.class.getSimpleName();

    private String title = "";
    private float amount = -1;
    private float price = -1;
    private float sum = -1;
    private String category = null;
    private ArrayList<String> items;
    private String last;

    Purchase(ArrayList<String> items, Context context) {
        this.items = items;
        if (items.size() == 6)
            if(!purchase(items.get(4))) {
                items.remove(items.get(5));
                items.remove(items.get(4));
            }
        while(items.size() > 0) {
            last = last();
            if (!set(sum)) {
                sum = tryToCast();
                checkIfCasted(sum, false);
            } else if (!set(price)) {
                price = tryToCast();
                checkIfCasted(price, false);
            } else if(!set(amount)) {
                amount = tryToCast();
                checkIfCasted(amount, true);
            } else {
                int size = items.size();
                if(size == 1){
                    title = last;
                    items.remove(last);
                } else {
                    ArrayList<String> titleList = (ArrayList<String>)items.clone();
                    for (String string : titleList)
                        title += string;
                    items.clear();
                }
            }
        }
        category = setCategory(context);
    }

    static boolean purchase(String string){
        return !StringManager.similar(string, "pusikliendivoit");
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

    private void checkIfCasted(float field, boolean amount){
        if (set(field))
            items.remove(last);
        else {
            tryToSplit(last);
            if (amount && last.equals(last()))
                items.add("1");
        }
    }

    private void tryToSplit(String string) {
        if(string.contains("."))  {
            String cut = string.substring(2, string.length());
            int index = cut.indexOf('.') + 2;
            String first = string.substring(0, index - 1);
            String second = string.substring(index - 1, string.length());
            items.remove(last);
            items.add(first);
            items.add(second);
            Log.d(TAG, "first: " + first + " ; second: " + second);
        }
    }

    public String purchaseInfo(){
        return  "title: " + title +
                "; amount: " + amount +
                "; price: " + price +
                "; sum: " + sum +
                "; category: " + category + "\n";
    }

    private String setCategory(Context context) {
        String[] tags = FileManager.getStringFromTextFile(context, "categories/tags");
        String category = null;
        for (String line : tags) {
            if (line.substring(0, 2).equals("__"))
                category = line.substring(2, line.length());
            else
                if (title.contains(line))
                    return category;
        }
        return null;
    }
}

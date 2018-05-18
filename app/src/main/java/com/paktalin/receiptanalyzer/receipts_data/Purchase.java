package com.paktalin.receiptanalyzer.receipts_data;

import android.content.Context;
import android.util.Log;

import com.paktalin.receiptanalyzer.managers.StringManager;

import static com.paktalin.receiptanalyzer.managers.FileManager.getStringFromTextFile;
import static com.paktalin.receiptanalyzer.managers.StringManager.MAKE_EQUAL;
import static com.paktalin.receiptanalyzer.managers.StringManager.similar;

/**
 * Created by Paktalin on 23/04/2018.
 */

public class Purchase {
    private static final String TAG = Purchase.class.getSimpleName();

    private String title = null;
    private String category = null;
    private float price = 0;

    public Purchase() {}
    public Purchase(Context context, String line, String initial) {
        title = initial;
        setCategory(context, line);
        extractPrice(line.split(" "));
    }

    public static boolean purchase(String string, String notPurchase){
        if (percentOfLetters(string) < 0.7)
            return false;
        return !similar(string, notPurchase, MAKE_EQUAL);
    }

    private static float percentOfLetters(String string) {
        float letters = string.replaceAll("\\d", "").length();
        return letters/string.length();
    }

    private void extractPrice(String[] items) {
        for (int i = items.length - 1; i >= 0; i--) {
            price = tryCastToFloat(items[i]);
            if (price != 0) {
                if (tooBig(price) && isInt(price)) {
                    try {
                        price = tryToReducePrice(items[i-1]);
                    } catch (IndexOutOfBoundsException ignored) {}
                }
                return;
            }
        }
    }

    private boolean isInt(float f) {
        return f % 1 == 0;
    }

    private float tryToReducePrice(String previous) {
        int intPrice = (int) price;
        int intPrevious = (int) tryCastToFloat(previous);

        float joined = tryCastToFloat(intPrevious + "." + intPrice);
        if (!tooBig(joined))
            return joined;
        return price;
    }

    private boolean tooBig(float number) {
        return number > 10;
    }

    private static float tryCastToFloat(String string) {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            string = replaceSimilarLettersWithNumbers(string);
            try {
                return Float.parseFloat(string);
            } catch (NumberFormatException ignored){}
        }
        return 0;
    }

    private static String replaceSimilarLettersWithNumbers(String string) {
        string = string.replaceAll("o", "0");
        string = string.replaceAll("g", "9");
        string = string.replaceAll(" ", "");
        string = string.replaceAll("c", "");
        return StringManager.removeLetters(string);
    }

    private void setCategory(Context context, String string) {
        String[] tags = getStringFromTextFile(context, "categories/tags");
        String currentCategory = null;
        for (String line : tags) {
            if (line.substring(0, 2).equals("__"))
                currentCategory = line.substring(2, line.length());
            else if (string.contains(line)) {
                category = currentCategory;
                break;
            }
        }
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

    public void setTitle(String title) {
        this.title = title;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    public void logPurchase() {
        Log.d(TAG,
                "title: " + title + "\n" +
                        "price: " + price + "\n" +
                        "category: " + category + "\n");
    }
}
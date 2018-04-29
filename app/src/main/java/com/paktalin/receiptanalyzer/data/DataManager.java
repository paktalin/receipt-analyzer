package com.paktalin.receiptanalyzer.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.TreeMap;

import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.COLUMN_CATEGORY;
import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.TABLE_NAME_PURCHASE;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_DATE_RECEIPT;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_FINAL_PRICE;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_SUPERMARKET;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.TABLE_NAME_RECEIPT;

/**
 * Created by Paktalin on 29/04/2018.
 */

public class DataManager {
    private static SQLiteDatabase db;
    private static TreeMap<String, Integer> categories;
    private static TreeMap<String, Integer> supermarkets;
    private static float expenses = 0;
    private static long startingTime;
    private static long currentTime;

    public static Object[] extractData(SQLiteDatabase db, long from, long to) {
        DataManager.db = db;
        startingTime = from;
        currentTime = to;
        supermarkets = new TreeMap<>();
        categories = new TreeMap<>();
        setCategories();
        setSupermarketsAndExpenses();
        return new Object[] {supermarkets, categories, expenses};
    }

    private static void setSupermarketsAndExpenses() {
        String selection = COLUMN_SUPERMARKET + ", " + COLUMN_FINAL_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_RECEIPT + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + startingTime + " AND " + currentTime;
        Cursor cursor = db.rawQuery(query, null);

        int finalPriceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);
        int supermarketIndex = cursor.getColumnIndex(COLUMN_SUPERMARKET);

        while (cursor.moveToNext()) {
            expenses += cursor.getFloat(finalPriceIndex);
            String key = cursor.getString(supermarketIndex);
            if (supermarkets.containsKey(key))
                supermarkets.put(key, supermarkets.get(key) + 1);
            else
                supermarkets.put(key, 1);
        }
        cursor.close();
    }

    private static void setCategories() {
        String query = "SELECT " + COLUMN_CATEGORY + " FROM " + TABLE_NAME_PURCHASE + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + startingTime + " AND " + currentTime;
        Cursor cursor = db.rawQuery(query, null);
        int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

        while (cursor.moveToNext()) {
            String key = cursor.getString(categoryIndex);
            if (categories.containsKey(key))
                categories.put(key, categories.get(key) + 1);
            else
                categories.put(key, 1);
        }
        cursor.close();
    }

}

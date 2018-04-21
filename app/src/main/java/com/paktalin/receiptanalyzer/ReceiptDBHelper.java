package com.paktalin.receiptanalyzer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.receipts.Receipt;

import com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class ReceiptDBHelper {
    private static final String TAG = ReceiptDBHelper.class.getSimpleName();
    private static DatabaseHelper dbHelper;

    private static void saveReceipt(Receipt receipt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReceiptEntry.COLUMN_SUPERMARKET, receipt.getSupermarket());
        values.put(ReceiptEntry.COLUMN_RETAILER, receipt.getRetailer());
        values.put(ReceiptEntry.COLUMN_ADDRESS, receipt.getAddress());
        values.put(ReceiptEntry.COLUMN_FINAL_PRICE, receipt.getFinalPrice());
        long newRowId = db.insert(ReceiptEntry.TABLE_NAME, null, values);
        if (newRowId == -1)
            Log.e(TAG, "Couldn't insert the receipt into DB");
    }

    private static void displayDBData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ReceiptEntry._ID,
                ReceiptEntry.COLUMN_SUPERMARKET,
                ReceiptEntry.COLUMN_RETAILER,
                ReceiptEntry.COLUMN_ADDRESS,
                ReceiptEntry.COLUMN_FINAL_PRICE,
                ReceiptEntry.COLUMN_DATE};

        Cursor cursor = db.query(
                ReceiptEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        int idColumnIndex = cursor.getColumnIndex(ReceiptEntry._ID);
        int supermarketColumnIndex = cursor.getColumnIndex(ReceiptEntry.COLUMN_SUPERMARKET);
        int retailerColumnIndex = cursor.getColumnIndex(ReceiptEntry.COLUMN_RETAILER);
        int addressColumnIndex = cursor.getColumnIndex(ReceiptEntry.COLUMN_ADDRESS);
        int finalPriceColumnIndex = cursor.getColumnIndex(ReceiptEntry.COLUMN_FINAL_PRICE);
        int dateColumnIndex = cursor.getColumnIndex(ReceiptEntry.COLUMN_DATE);

        while (cursor.moveToNext()) {
            Log.d(TAG, cursor.getInt(idColumnIndex) + "");
            Log.d(TAG, cursor.getString(supermarketColumnIndex));
            Log.d(TAG, cursor.getString(retailerColumnIndex));
            Log.d(TAG, cursor.getString(addressColumnIndex));
            Log.d(TAG, cursor.getFloat(finalPriceColumnIndex) + "");
            Log.d(TAG, cursor.getString(dateColumnIndex));
            //TODO resolve the issue with real in db (read as 4.639999 instead of 4.64)
        }
        cursor.close();
    }

}

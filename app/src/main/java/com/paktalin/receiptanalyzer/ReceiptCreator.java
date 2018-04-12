package com.paktalin.receiptanalyzer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextRecognizer;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.receipts.Receipt;

import java.util.ArrayList;

import com.paktalin.receiptanalyzer.data.ReceiptContract.ReceiptEntry;

/**
 * Created by Paktalin on 22-Mar-18.
 */

class ReceiptCreator {
    private static final String TAG = ReceiptCreator.class.getSimpleName();
    private static DatabaseHelper dbHelper;

    static String analyze(Context context, Bitmap bitmap) {
        ArrayList<String> lines = recognize(context, bitmap);
        String filteredString = StringFilter.filter(lines);
        String[] linesArray = filteredString.split("\n");
        SupermarketInfo info = new SupermarketInfo(context, linesArray);
        if(info.supermarket != null) {
            Receipt receipt = info.createReceipt();
            dbHelper = new DatabaseHelper(context);
            saveReceipt(receipt);
            displayDBData();
            return receipt.getInfo();
        }
        return "Sorry, we couldn't identify the supermarket. Please, try to take picture again";
    }

    private static ArrayList<String> recognize(Context context, Bitmap bitmap) {
        TextRecognizer detector = new TextRecognizer.Builder(context).build();
        if (detector.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            return LineSorter.toTextLines(detector.detect(frame));
        }else {
            Log.d(TAG, "Could not set up the detector!");
            return null;
        }
    }

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

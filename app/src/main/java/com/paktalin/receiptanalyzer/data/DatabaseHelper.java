package com.paktalin.receiptanalyzer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paktalin.receiptanalyzer.data.Contracts.*;


/**
 * Created by Paktalin on 10/04/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "receipt_item.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_RECEIPTS_TABLE = "CREATE TABLE " + ReceiptEntry.TABLE_NAME_RECEIPT + " ("
                + ReceiptEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReceiptEntry.COLUMN_SUPERMARKET + " TEXT NOT NULL, "
                + ReceiptEntry.COLUMN_RETAILER + " TEXT, "
                + ReceiptEntry.COLUMN_ADDRESS + " TEXT, "
                + ReceiptEntry.COLUMN_FINAL_PRICE + " NUMERIC, "
                + ReceiptEntry.COLUMN_DATE + " INTEGER, "
                + ReceiptEntry.COLUMN_FIRST_PURCHASE_ID + " INTEGER, "
                + ReceiptEntry.COLUMN_PURCHASES_LENGTH + " INTEGER);";

        String SQL_CREATE_PURCHASES_TABLE = "CREATE TABLE " + PurchaseEntry.TABLE_NAME_PURCHASE + " ("
                + PurchaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PurchaseEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + PurchaseEntry.COLUMN_CATEGORY + " TEXT, "
                + PurchaseEntry.COLUMN_PRICE + " NUMERIC);";

        db.execSQL(SQL_CREATE_RECEIPTS_TABLE);
        db.execSQL(SQL_CREATE_PURCHASES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Updating from version " + oldVersion + " to version " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + ReceiptEntry.TABLE_NAME_RECEIPT);
        onCreate(db);
    }
}

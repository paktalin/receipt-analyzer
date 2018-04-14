package com.paktalin.receiptanalyzer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paktalin.receiptanalyzer.data.ReceiptContract.ReceiptEntry;

/**
 * Created by Paktalin on 10/04/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "receipt.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_RECEIPTS_TABLE = "CREATE TABLE " + ReceiptEntry.TABLE_NAME + " ("
                + ReceiptEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReceiptEntry.COLUMN_SUPERMARKET + " TEXT NOT NULL, "
                + ReceiptEntry.COLUMN_RETAILER + " TEXT NOT NULL, "
                + ReceiptEntry.COLUMN_ADDRESS + " TEXT NOT NULL, "
                + ReceiptEntry.COLUMN_FINAL_PRICE + " NUMERIC, "
                + ReceiptEntry.COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

        db.execSQL(SQL_CREATE_RECEIPTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Updating from version " + oldVersion + " to version " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + ReceiptEntry.TABLE_NAME);
        onCreate(db);
    }
}

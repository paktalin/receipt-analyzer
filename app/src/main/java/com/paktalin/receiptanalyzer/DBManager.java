package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Paktalin on 07/04/2018.
 */

class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();

    static void execute(Context context) {
        DatabaseHelper myDbHelper = new DatabaseHelper(context);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Log.e(TAG, "Unable to create database");
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            throw e;
        }

        Log.d(TAG, "Successfully Imported");
        Cursor cursor = myDbHelper.query("Selver", null,
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Log.d(TAG, "_id: " + cursor.getString(0) + "\n" +
                        "retailer: " + cursor.getString(1) + "\n" +
                        "first line: " + cursor.getString(2) + "\n" +
                        "address:  " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
    }
}

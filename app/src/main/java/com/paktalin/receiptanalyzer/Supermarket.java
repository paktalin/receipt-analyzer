package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Paktalin on 07/04/2018.
 */

class Supermarket {
    private static final String TAG = Supermarket.class.getSimpleName();

    final static String
            SELVER = "Selver",
            PRISMA = "Prisma",
            MAXIMA = "Maxima",
            RIMI = "Rimi",
            KONSUM = "Konsum";

    private String supermarket, firstLine;
    private Context context;
    private String retailer, address;
    private ArrayList<String> firstLines;
    private DatabaseHelper helper;
    private final String COLUMN_RETAILER = "retailer";
    private final String COLUMN_ADDRESS = "address";


    Supermarket (Context context, String firstLine) {
        this.context = context;
        this.firstLine = firstLine;
        helper = new DatabaseHelper(context);
    }

    void execute() {
        supermarket = StoreName.getStoreName(firstLine);

        firstLines = loadFirstLines();
        Log.d(TAG, firstLines.toString());
        int index = getSupermarketIndex(firstLines);
        Log.d(TAG, "Index: " + index);
        getInfoByIndex(index);
        Log.d(TAG, retailer + " " + address);
        //createReceipt();
    }

    private ArrayList<String> loadFirstLines() {
        helper.openDataBase();
        Cursor cursor = helper.query(supermarket, new String[]{"first_line"},
                null, null, null, null, null);
        ArrayList<String> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return list;
    }

    private int getSupermarketIndex(ArrayList<String> list) {
        firstLine = StringManager.clean(firstLine);
        for (int i = 0; i < list.size(); i++) {
            String anArray = list.get(i);
            if (StringManager.identical(firstLine, anArray)){
                return i;
            }
        }
        return -1;
    }

    private void getInfoByIndex(int index) {
        /*String query = "SELECT * FROM " + supermarket + " where _id=?" + index;
        helper.openDataBase();
        Cursor cursor = helper.rawQuery(query, new String[]{String.valueOf(index)});
        if (cursor.moveToFirst()) {
            retailer = cursor.getString(1);
            address = cursor.getString(3);
        }*/
        helper.openDataBase();
        Cursor cursor = helper.query(supermarket,
                new String[]{COLUMN_RETAILER, COLUMN_ADDRESS},
                index + "=?",
                new String[]{String.valueOf(index)}, null, null, null);
        if (cursor.moveToFirst()) {
            retailer = cursor.getString(1);
            address = cursor.getString(3);
        }
    }

    private void createReceipt() {
    }
}

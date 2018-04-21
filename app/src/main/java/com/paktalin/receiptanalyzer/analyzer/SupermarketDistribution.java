package com.paktalin.receiptanalyzer.analyzer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class SupermarketDistribution {
    private static final String TAG = SupermarketDistribution.class.getSimpleName();

    private static final int SELVER_INDEX = 0;
    private static final int MAXIMA_INDEX = 1;
    private static final int RIMI_INDEX =   2;
    private static final int KONSUM_INDEX = 3;
    private static final int PRISMA_INDEX = 4;

    private static DatabaseHelper dbHelper;

    public static int[] calculate(Context context) {
        int[] distribution = new int[5];
        dbHelper = new DatabaseHelper(context);
        distribution[SELVER_INDEX] = count("Selver");
        distribution[MAXIMA_INDEX] = count("Maxima");
        distribution[RIMI_INDEX] = count("Rimi");
        distribution[KONSUM_INDEX] = count("Konsum");
        distribution[PRISMA_INDEX] = count("Prisma");

        return distribution;
    }

    private static int count(String supermarket) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String queryString = "SELECT count(*) FROM " + ReceiptEntry.TABLE_NAME +
                " WHERE " + ReceiptEntry.COLUMN_SUPERMARKET + " = ?;";
        Cursor cursor = db.rawQuery(queryString, new String[]{supermarket});

        cursor.moveToFirst();
        int count = cursor.getInt(0);

        Log.d(TAG, count + "");
        cursor.close();

        return count;
    }

}

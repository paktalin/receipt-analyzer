package com.paktalin.receiptanalyzer.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.data.Contracts;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.*;

/**
 * Created by Paktalin on 25/04/2018.
 */

public class ViewReceiptActivity extends AppCompatActivity {
    private static final String TAG = ViewReceiptActivity.class.getSimpleName();

    long[] purchasesIDs;
    long id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt);

        this.id = getIntent().getIntExtra("id", 0);
        Receipt receipt = getReceipt();


    }


    private Receipt getReceipt() {
        DatabaseHelper dbHelper = new DatabaseHelper(ViewReceiptActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_NAME,
                        new String[]{COLUMN_SUPERMARKET, COLUMN_RETAILER, COLUMN_ADDRESS, COLUMN_PURCHASES, COLUMN_DATE, COLUMN_FINAL_PRICE},
                        _ID + " = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);

        if (cursor != null) {
            cursor.moveToFirst();
        } else
            Log.d(TAG, "Cursor is null");

        int supermarketIndex = cursor.getColumnIndex(COLUMN_SUPERMARKET);
        int retailerIndex = cursor.getColumnIndex(COLUMN_RETAILER);
        int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
        int purchasesIndex = cursor.getColumnIndex(COLUMN_PURCHASES);
        int finalPriceIndex = cursor.getColumnIndex(Contracts.ReceiptEntry.COLUMN_FINAL_PRICE);
        int dateIndex = cursor.getColumnIndex(Contracts.ReceiptEntry.COLUMN_DATE);

        Receipt receipt = new Receipt();
        receipt.setSupermarket(cursor.getString(supermarketIndex));
        receipt.setRetailer(cursor.getString(retailerIndex));
        receipt.setAddress(cursor.getString(addressIndex));
        receipt.setFinalPrice(Float.parseFloat(cursor.getString(finalPriceIndex)));
        receipt.setDate(Long.parseLong(cursor.getString(dateIndex)));


        String[] stringIDs = FileManager.convertStringToArray(cursor.getString(purchasesIndex));
        for (int i = 0; i < stringIDs.length; i++)
            purchasesIDs[i] = Long.parseLong(stringIDs[i]);
        return receipt;
    }
}

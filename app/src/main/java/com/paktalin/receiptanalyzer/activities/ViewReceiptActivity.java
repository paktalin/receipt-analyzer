package com.paktalin.receiptanalyzer.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
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

        this.id = getIntent().getLongExtra("id", 1);
        Receipt receipt = getReceipt();

        ((TextView)findViewById(R.id.supermarket_1)).setText(receipt.getSupermarket());
        ((TextView)findViewById(R.id.retailer)).setText(receipt.getRetailer());
        ((TextView)findViewById(R.id.address)).setText(receipt.getAddress());
    }


    private Receipt getReceipt() {
        DatabaseHelper dbHelper = new DatabaseHelper(ViewReceiptActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Log.d(TAG, "id: " + String.valueOf(id));

        Cursor cursor =
                db.query(TABLE_NAME,
                        new String[]{_ID, COLUMN_SUPERMARKET, COLUMN_RETAILER, COLUMN_ADDRESS, COLUMN_PURCHASES, COLUMN_DATE, COLUMN_FINAL_PRICE},
                        "_id= ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null);

        if (cursor != null)
            cursor.moveToFirst();
        int supermarketIndex = cursor.getColumnIndex(COLUMN_SUPERMARKET);
        int retailerIndex = cursor.getColumnIndex(COLUMN_RETAILER);
        int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
        int purchasesIndex = cursor.getColumnIndex(COLUMN_PURCHASES);
        int finalPriceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);
        int dateIndex = cursor.getColumnIndex(COLUMN_DATE);

        Receipt receipt = new Receipt();
        receipt.setSupermarket(cursor.getString(supermarketIndex));
        receipt.setRetailer(cursor.getString(retailerIndex));
        receipt.setAddress(cursor.getString(addressIndex));
        receipt.setFinalPrice(Float.parseFloat(cursor.getString(finalPriceIndex)));
        receipt.setDate(Long.parseLong(cursor.getString(dateIndex)));

        String[] stringIDs = FileManager.convertStringToArray(cursor.getString(purchasesIndex));
        cursor.close();

        purchasesIDs = new long[stringIDs.length];
        for (int i = 0; i < stringIDs.length; i++)
            purchasesIDs[i] = Long.parseLong(stringIDs[i]);
        return receipt;
    }
}

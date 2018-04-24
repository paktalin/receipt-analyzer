package com.paktalin.receiptanalyzer.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.activities.adapters.ReceiptsAdapter;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;
import com.paktalin.receiptanalyzer.data.Contracts.*;

/**
 * Created by Paktalin on 24/04/2018.
 */

public class ViewReceipts extends AppCompatActivity{
    private static final String TAG = ViewReceipts.class.getSimpleName();

    Receipt[] receipts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipts);

        extractReceipts();
        ReceiptsAdapter adapter = new ReceiptsAdapter(ViewReceipts.this, receipts);
        ((ListView)findViewById(R.id.all_receipts)).setAdapter(adapter);
    }

    private void extractReceipts() {
        DatabaseHelper dbHelper = new DatabaseHelper(ViewReceipts.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                ReceiptEntry.COLUMN_SUPERMARKET,
                ReceiptEntry.COLUMN_FINAL_PRICE,
                ReceiptEntry.COLUMN_DATE};

        Cursor cursor = db.query(ReceiptEntry.TABLE_NAME, projection, null, null, null, null, null);

        receipts = new Receipt[cursor.getCount()];
        int supermarketColumnIndex = cursor.getColumnIndex(ReceiptEntry.COLUMN_SUPERMARKET);
        int finalPriceColumnIndex = cursor.getColumnIndex(ReceiptEntry.COLUMN_FINAL_PRICE);
        //int dateColumnIndex = cursor.getColumnIndex(ReceiptEntry.COLUMN_DATE);

        int i = 0;
        while (cursor.moveToNext()) {
            receipts[i] = new Receipt();

            receipts[i].setSupermarket(cursor.getString(supermarketColumnIndex));
            receipts[i].setFinalPrice(cursor.getFloat(finalPriceColumnIndex));
            Log.d(TAG, receipts[i].getSupermarket());
            //receipt.setDate(cursor.getString(dateColumnIndex));
            i++;
        }
        cursor.close();
    }

}

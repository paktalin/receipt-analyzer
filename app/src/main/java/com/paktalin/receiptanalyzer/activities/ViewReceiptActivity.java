package com.paktalin.receiptanalyzer.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.activities.adapters.PurchasesAdapter;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;

import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.*;
import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.*;

/**
 * Created by Paktalin on 25/04/2018.
 */

public class ViewReceiptActivity extends AppCompatActivity {
    private static final String TAG = ViewReceiptActivity.class.getSimpleName();

    SQLiteDatabase db;
    Purchase[] purchases;
    PurchasesAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt);
        extractReceipt(getIntent().getLongExtra("id", 1));
        findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);

        adapter = new PurchasesAdapter(ViewReceiptActivity.this, purchases);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        Button buttonOk = findViewById(R.id.button_ok);
        buttonOk.setVisibility(View.VISIBLE);
        buttonOk.setOnClickListener(listener);

        Button buttonCancel = findViewById(R.id.button_cancel);
        buttonCancel.setVisibility(View.VISIBLE);
        buttonCancel.setOnClickListener(listener);
    }

    View.OnClickListener listener = v -> {
        Intent intent = new Intent(ViewReceiptActivity.this, MainActivity.class);
        intent.putExtra("position", 2);
        startActivity(intent);
    };

    private void extractReceipt(long id) {
        DatabaseHelper dbHelper = new DatabaseHelper(ViewReceiptActivity.this);
        db = dbHelper.getReadableDatabase();

        String[] projection = new String[]
                {_ID, COLUMN_SUPERMARKET, COLUMN_RETAILER,
                        COLUMN_ADDRESS, COLUMN_FINAL_PRICE,
                        COLUMN_FIRST_PURCHASE_ID, COLUMN_PURCHASES_LENGTH};

        Cursor cursor = db.query(TABLE_NAME_RECEIPT,
                        projection,
                        "_id= ?",
                        new String[] { String.valueOf(id) },
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int supermarketIndex = cursor.getColumnIndex(COLUMN_SUPERMARKET);
            int retailerIndex = cursor.getColumnIndex(COLUMN_RETAILER);
            int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
            int finalPriceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);
            int startIdIndex = cursor.getColumnIndex(COLUMN_FIRST_PURCHASE_ID);
            int lengthIndex = cursor.getColumnIndex(COLUMN_PURCHASES_LENGTH);

            ((TextView)findViewById(R.id.supermarket)).setText(cursor.getString(supermarketIndex));
            ((TextView)findViewById(R.id.retailer)).setText(cursor.getString(retailerIndex));
            ((TextView)findViewById(R.id.address)).setText(cursor.getString(addressIndex));
            ((EditText)findViewById(R.id.final_price)).setText(String.valueOf(cursor.getFloat(finalPriceIndex)));

            unpackPurchases(cursor.getLong(startIdIndex), cursor.getInt(lengthIndex));
        }
        cursor.close();
    }

    void unpackPurchases(long startId, int length) {

        String selection = _ID + ", " + COLUMN_TITLE + ", " + COLUMN_CATEGORY + ", " + COLUMN_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_PURCHASE + " WHERE " + _ID + " BETWEEN "
                + startId + " AND " + (startId + length - 1);
        Cursor cursor = db.rawQuery(query, null);

        int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
        int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
        int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);

        purchases = new Purchase[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            purchases[i] = new Purchase();
            purchases[i].setTitle(cursor.getString(titleIndex));
            purchases[i].setCategory(cursor.getString(categoryIndex));
            purchases[i].setPrice(cursor.getFloat(priceIndex));
            i++;
        }
        cursor.close();
    }
}

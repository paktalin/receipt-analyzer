package com.paktalin.receiptanalyzer.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.activities.adapters.PurchasesAdapter;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;

import java.util.ArrayList;
import java.util.Arrays;

import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.*;
import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.*;

/**
 * Created by Paktalin on 25/04/2018.
 */

public class ViewReceiptActivity extends AppCompatActivity {
    private static final String TAG = ViewReceiptActivity.class.getSimpleName();

    SQLiteDatabase db;

    long id;
    Purchase[] purchases;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt);
        purchases = new Purchase[5];

        this.id = getIntent().getLongExtra("id", 1);
        Receipt receipt = getReceipt();

        ((TextView)findViewById(R.id.supermarket_1)).setText(receipt.getSupermarket());
        ((TextView)findViewById(R.id.retailer)).setText(receipt.getRetailer());
        ((TextView)findViewById(R.id.address)).setText(receipt.getAddress());

        ViewSwitcher switcher = findViewById(R.id.my_switcher);
        ((TextView) switcher.findViewById(R.id.final_price_tv)).setText(String.valueOf(receipt.getFinalPrice()));

        PurchasesAdapter adapter = new PurchasesAdapter(ViewReceiptActivity.this, purchases);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }


    private Receipt getReceipt() {
        DatabaseHelper dbHelper = new DatabaseHelper(ViewReceiptActivity.this);
        db = dbHelper.getReadableDatabase();

        String[] projection = new String[]
                {_ID, COLUMN_SUPERMARKET, COLUMN_RETAILER,
                        COLUMN_ADDRESS, COLUMN_PURCHASES,
                        COLUMN_DATE, COLUMN_FINAL_PRICE,
                        COLUMN_FIRST_PURCHASE_ID, COLUMN_PURCHASES_LENGTH};

        Cursor cursor =
                db.query(TABLE_NAME_RECEIPT,
                        projection,
                        "_id= ?",
                        new String[] { String.valueOf(id) },
                        null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        int supermarketIndex = cursor.getColumnIndex(COLUMN_SUPERMARKET);
        int retailerIndex = cursor.getColumnIndex(COLUMN_RETAILER);
        int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
        int finalPriceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);
        int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
        int startIdIndex = cursor.getColumnIndex(COLUMN_FIRST_PURCHASE_ID);
        int lengthIndex = cursor.getColumnIndex(COLUMN_PURCHASES_LENGTH);

        Receipt receipt = new Receipt();
        receipt.setSupermarket(cursor.getString(supermarketIndex));
        receipt.setRetailer(cursor.getString(retailerIndex));
        receipt.setAddress(cursor.getString(addressIndex));
        receipt.setFinalPrice(Float.parseFloat(cursor.getString(finalPriceIndex)));
        receipt.setDate(Long.parseLong(cursor.getString(dateIndex)));

        unpackPurchases(cursor.getLong(startIdIndex), cursor.getInt(lengthIndex));
        cursor.close();
        return receipt;
    }

    void unpackPurchases(long startId, int length) {

        String selection = _ID + ", " + COLUMN_TITLE + ", " + COLUMN_CATEGORY + ", " + COLUMN_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_PURCHASE + " WHERE " + _ID + " BETWEEN "
                + startId + " AND " + (startId + length);
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
    }
}

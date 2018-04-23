package com.paktalin.receiptanalyzer.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.ReceiptExtractor;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;

import static com.paktalin.receiptanalyzer.DataKeeper.*;
import com.paktalin.receiptanalyzer.data.Contracts.*;

import java.util.ArrayList;


/**
 * Created by Paktalin on 12/04/2018.
 */

public class NewReceiptActivity extends AppCompatActivity {
    private static final String TAG = NewReceiptActivity.class.getSimpleName();

    Receipt receipt;
    SharedPreferences appData;
    private String supermarket, retailer, address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_receipt);
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            int rotation = getIntent().getIntExtra("rotation", 0);
            Uri imageUri = getIntent().getParcelableExtra("uri");
            Context context = NewReceiptActivity.this;
            Bitmap bitmap = null;
            try {
                bitmap = FileManager.decodeBitmapUri_Rotate(rotation, imageUri, context);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(context, "An error occered. Please, try again", Toast.LENGTH_SHORT);
                toast.show();
            }
            receipt = ReceiptExtractor.extract(context, bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.INVISIBLE);
            ArrayList<Purchase> purchases = null;
            TextView textViewSupermarket = findViewById(R.id.supermarket);
            TextView textViewRetailer = findViewById(R.id.retailer);
            TextView textViewAddress = findViewById(R.id.address);
            TextView textViewFinalPrice = findViewById(R.id.final_price);

            if (receipt != null) {
                supermarket = receipt.getSupermarket();
                retailer = receipt.getRetailer();
                address = receipt.getAddress();
                receipt.extractPurchases(NewReceiptActivity.this);
                purchases = receipt.getPurchases();
                for (Purchase p : purchases)
                    p.purchaseInfo();

                textViewSupermarket.setText(supermarket);
                textViewRetailer.setText(retailer);
                textViewAddress.setText(address);
                String finalPrice = String.valueOf(receipt.getFinalPrice());
                textViewFinalPrice.setText(finalPrice);
            } else {
                Toast toast = Toast.makeText(NewReceiptActivity.this,
                        "Sorry, we couldn't scan the receipt. Please, try again", Toast.LENGTH_SHORT);
                toast.show();
            }

            Button buttonOk = findViewById(R.id.button_ok);
            buttonOk.setVisibility(View.VISIBLE);
            buttonOk.setOnClickListener(buttonOkListener);

            Button buttonCancel = findViewById(R.id.button_cancel);
            buttonCancel.setVisibility(View.VISIBLE);
            buttonCancel.setOnClickListener(v -> {
                Intent mainActivityIntent = new Intent(NewReceiptActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
            });

            ListViewAdapter adapter = new ListViewAdapter(NewReceiptActivity.this, purchases);
            ListView listView = findViewById(R.id.list_view);
            listView.setAdapter(adapter);
        }
    }

    View.OnClickListener buttonOkListener = v -> {
        saveToDB();
        syncData(APP_PREFERENCES, supermarket);
        syncData(RETAILERS_PREFERENCES, retailer);
        syncData(ADDRESSES_PREFERENCES, address);
        Intent mainActivityIntent = new Intent(NewReceiptActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
    };

    private void saveToDB() {
        DatabaseHelper dbHelper = new DatabaseHelper(NewReceiptActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReceiptEntry.COLUMN_SUPERMARKET, receipt.getSupermarket());
        values.put(ReceiptEntry.COLUMN_RETAILER, receipt.getRetailer());
        values.put(ReceiptEntry.COLUMN_ADDRESS, receipt.getAddress());
        values.put(ReceiptEntry.COLUMN_FINAL_PRICE, receipt.getFinalPrice());
        long newRowId = db.insert(ReceiptEntry.TABLE_NAME, null, values);
        if (newRowId == -1)
            Log.e(TAG, "Couldn't insert the receipt into DB");
    }

    private void syncData(String preferences, String key) {
        appData = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        int counter = appData.getInt(key, 0) + 1;
        SharedPreferences.Editor editor = appData.edit();
        editor.putInt(key, counter);
        editor.apply();
    }
}

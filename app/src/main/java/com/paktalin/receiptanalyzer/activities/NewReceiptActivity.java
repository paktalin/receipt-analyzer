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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import com.paktalin.receiptanalyzer.data.Contracts.*;

import java.util.ArrayList;


/**
 * Created by Paktalin on 12/04/2018.
 */

public class NewReceiptActivity extends AppCompatActivity {
    private static final String TAG = NewReceiptActivity.class.getSimpleName();

    Receipt receipt;
    private String supermarket;
    ListViewAdapter adapter;
    ListView listView;
    EditText textViewFinalPrice;
    SQLiteDatabase db;

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
            ArrayList<Purchase> purchases = new ArrayList<>();
            TextView textViewSupermarket = findViewById(R.id.supermarket);
            TextView textViewRetailer = findViewById(R.id.retailer);
            TextView textViewAddress = findViewById(R.id.address);
            textViewFinalPrice = findViewById(R.id.final_price);

            if (receipt != null) {
                supermarket = receipt.getSupermarket();
                String retailer = receipt.getRetailer();
                String address = receipt.getAddress();
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
            (findViewById(R.id.kokku)).setVisibility(View.VISIBLE);

            Button buttonOk = findViewById(R.id.button_ok);
            buttonOk.setVisibility(View.VISIBLE);
            buttonOk.setOnClickListener(buttonOkListener);

            Button buttonCancel = findViewById(R.id.button_cancel);
            buttonCancel.setVisibility(View.VISIBLE);
            buttonCancel.setOnClickListener(v -> {
                Intent mainActivityIntent = new Intent(NewReceiptActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
            });

            adapter = new ListViewAdapter(NewReceiptActivity.this, purchases);
            listView = findViewById(R.id.list_view);
            listView.setAdapter(adapter);
        }
    }

    View.OnClickListener buttonOkListener = v -> {
        DatabaseHelper dbHelper = new DatabaseHelper(NewReceiptActivity.this);
        db = dbHelper.getWritableDatabase();

        if(saveReceipt()) {
            for (int i = 0; i < adapter.getCount(); i++)
                if (!savePurchases(i)) {
                    Toast toast = Toast.makeText(NewReceiptActivity.this, "Something went wrong!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
            updateSharedPreferences();
            Intent mainActivityIntent = new Intent(NewReceiptActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
        } else {
            Toast toast = Toast.makeText(NewReceiptActivity.this, "Something went wrong!", Toast.LENGTH_LONG);
            toast.show();
        }
    };

    private boolean saveReceipt() {
        ContentValues values = new ContentValues();
        values.put(ReceiptEntry.COLUMN_SUPERMARKET, receipt.getSupermarket());
        values.put(ReceiptEntry.COLUMN_RETAILER, receipt.getRetailer());
        values.put(ReceiptEntry.COLUMN_ADDRESS, receipt.getAddress());
        try {
            float finalPrice = Float.parseFloat(String.valueOf(textViewFinalPrice.getText()));
            values.put(ReceiptEntry.COLUMN_FINAL_PRICE, finalPrice);
        } catch (Exception e) {
            Toast toast = Toast.makeText(NewReceiptActivity.this, "Wrong format of the final price!", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        long newRowId = db.insert(ReceiptEntry.TABLE_NAME, null, values);
        return newRowId != -1;
    }

    private boolean savePurchases(int i) {
        Purchase p = (Purchase) listView.getItemAtPosition(i);
        ContentValues values = new ContentValues();
        values.put(PurchaseEntry.COLUMN_TITLE, p.getTitle());
        values.put(PurchaseEntry.COLUMN_CATEGORY, p.getCategory());
        values.put(PurchaseEntry.COLUMN_PRICE, p.getPrice());
        long newRowId = db.insert(PurchaseEntry.TABLE_NAME, null, values);
        return newRowId != -1;
    }

    private void updateSharedPreferences() {
        SharedPreferences appData = getSharedPreferences("app_data", Context.MODE_PRIVATE);
        int counter = appData.getInt(supermarket, 0) + 1;
        SharedPreferences.Editor editor = appData.edit();
        editor.putInt(supermarket, counter);
        editor.apply();
    }
}

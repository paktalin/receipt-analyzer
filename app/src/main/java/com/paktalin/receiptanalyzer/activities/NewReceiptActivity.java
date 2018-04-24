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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.ReceiptExtractor;
import com.paktalin.receiptanalyzer.activities.adapters.PurchasesAdapter;
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
    PurchasesAdapter adapter;
    ListView listView;
    EditText textViewFinalPrice;
    SQLiteDatabase db;
    long[] purchasesIDs;
    ArrayList<Purchase> purchases;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_receipt);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
                Toast toast = Toast.makeText(context, "An error occured. Please, try again", Toast.LENGTH_SHORT);
                toast.show();
            }
            receipt = ReceiptExtractor.extract(context, bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
            textViewFinalPrice = findViewById(R.id.final_price_1);

            if (receipt != null) {
                supermarket = receipt.getSupermarket();
                receipt.extractPurchases(NewReceiptActivity.this);
                purchases = receipt.getPurchases();
                for (Purchase p : purchases)
                    p.purchaseInfo();

                ((TextView)findViewById(R.id.supermarket_1)).setText(supermarket);
                ((TextView)findViewById(R.id.retailer)).setText(receipt.getRetailer());
                ((TextView)findViewById(R.id.address)).setText(receipt.getAddress());
                (findViewById(R.id.kokku)).setVisibility(View.VISIBLE);
                String finalPrice = String.valueOf(receipt.getFinalPrice());
                textViewFinalPrice.setText(finalPrice);

                adapter = new PurchasesAdapter(NewReceiptActivity.this, purchases);
                listView = findViewById(R.id.list_view);
                listView.setAdapter(adapter);

                Button buttonOk = findViewById(R.id.button_ok);
                buttonOk.setVisibility(View.VISIBLE);
                buttonOk.setOnClickListener(buttonOkListener);

                Button buttonCancel = findViewById(R.id.button_cancel);
                buttonCancel.setVisibility(View.VISIBLE);
                buttonCancel.setOnClickListener(v -> {
                    Intent mainActivityIntent = new Intent(NewReceiptActivity.this, MainActivity.class);
                    startActivity(mainActivityIntent);
                });
            } else {
                (Toast.makeText(NewReceiptActivity.this,
                        "Sorry, we couldn't scan the receipt. Please, try again", Toast.LENGTH_SHORT)).show();
            }
        }
    }

    View.OnClickListener buttonOkListener = v -> {
        DatabaseHelper dbHelper = new DatabaseHelper(NewReceiptActivity.this);
        db = dbHelper.getWritableDatabase();
        purchasesIDs = new long[purchases.size()];
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
            Toast toast = Toast.makeText(NewReceiptActivity.this, "The app's Database has changed. Update your app, please!", Toast.LENGTH_LONG);
            toast.show();
        }
    };

    private boolean saveReceipt() {
        ContentValues values = new ContentValues();
        values.put(ReceiptEntry.COLUMN_SUPERMARKET, receipt.getSupermarket());
        values.put(ReceiptEntry.COLUMN_RETAILER, receipt.getRetailer());
        values.put(ReceiptEntry.COLUMN_ADDRESS, receipt.getAddress());
        values.put(ReceiptEntry.COLUMN_PURCHASES, FileManager.convertArrayToString(purchasesIDs));
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
        purchasesIDs[i] = newRowId;
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

package com.paktalin.receiptanalyzer.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.DataKeeper;
import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.ReceiptExtractor;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.data.ReceiptContract;
import com.paktalin.receiptanalyzer.receipts.Receipt;

import java.io.FileNotFoundException;

import static com.paktalin.receiptanalyzer.DataKeeper.APP_PREFERENCES;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class NewReceiptActivity extends AppCompatActivity {
    private static final String TAG = NewReceiptActivity.class.getSimpleName();

    Receipt receipt;
    String supermarket;

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
            receipt = ReceiptExtractor.extract(context,
                    //decoded and rotated bitmap
                    FileManager.decodeBitmapUri_Rotate(rotation, imageUri, context));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ProgressBar progress = findViewById(R.id.progress_bar);
            progress.setVisibility(View.INVISIBLE);

            TextView textViewSupermarket = findViewById(R.id.supermarket);
            TextView textViewRetailer = findViewById(R.id.retailer);
            TextView textViewAddress = findViewById(R.id.address);
            TextView textViewFinalPrice = findViewById(R.id.final_price);

            supermarket = receipt.getSupermarket();
            textViewSupermarket.setText(supermarket);
            textViewRetailer.setText(receipt.getRetailer());
            textViewAddress.setText(receipt.getAddress());
            String finalPrice = String.valueOf(receipt.getFinalPrice());
            textViewFinalPrice.setText(finalPrice);

            Button buttonOk = findViewById(R.id.button_ok);
            buttonOk.setVisibility(View.VISIBLE);
            buttonOk.setOnClickListener(buttonOkListener);

            Button buttonCancel = findViewById(R.id.button_cancel);
            buttonCancel.setVisibility(View.VISIBLE);
            buttonCancel.setOnClickListener(v -> {
                Intent mainActivityIntent = new Intent(NewReceiptActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
            });
        }
    }

    View.OnClickListener buttonOkListener = v -> {
        saveToDB();
        syncData();
        Intent mainActivityIntent = new Intent(NewReceiptActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
    };

    private void saveToDB() {
        DatabaseHelper dbHelper = new DatabaseHelper(NewReceiptActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReceiptContract.ReceiptEntry.COLUMN_SUPERMARKET, receipt.getSupermarket());
        values.put(ReceiptContract.ReceiptEntry.COLUMN_RETAILER, receipt.getRetailer());
        values.put(ReceiptContract.ReceiptEntry.COLUMN_ADDRESS, receipt.getAddress());
        values.put(ReceiptContract.ReceiptEntry.COLUMN_FINAL_PRICE, receipt.getFinalPrice());
        long newRowId = db.insert(ReceiptContract.ReceiptEntry.TABLE_NAME, null, values);
        if (newRowId == -1)
            Log.e(TAG, "Couldn't insert the receipt into DB");
    }

    private void syncData() {
        SharedPreferences appData = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String key = DataKeeper.getKey(supermarket);
        int supermarketCounter = appData.getInt(key, 0) + 1;
        SharedPreferences.Editor editor = appData.edit();
        editor.putInt(key, supermarketCounter);
        editor.apply();
    }
}

package com.paktalin.receiptanalyzer.activities;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.ReceiptExtractor;
import com.paktalin.receiptanalyzer.receipts.Receipt;

import java.io.FileNotFoundException;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class NewReceiptActivity extends AppCompatActivity {
    private static final String TAG = NewReceiptActivity.class.getSimpleName();

    Bitmap bitmap;
    Receipt receipt;
    ProgressBar progress;
    TextView textViewSupermarket, textViewRetailer, textViewAddress, textViewFinalPrice;

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
            extractBitmap(rotation, imageUri);
            receipt = ReceiptExtractor.extract(NewReceiptActivity.this, bitmap);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = findViewById(R.id.progress_bar);
            progress.setIndeterminate(true);
            progress.setVisibility(View.VISIBLE);
            textViewSupermarket = findViewById(R.id.supermarket);
            textViewRetailer = findViewById(R.id.retailer);
            textViewAddress = findViewById(R.id.address);
            textViewFinalPrice = findViewById(R.id.final_price);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.setVisibility(View.INVISIBLE);
            textViewSupermarket.setText(receipt.getSupermarket());
            textViewRetailer.setText(receipt.getRetailer());
            textViewAddress.setText(receipt.getAddress());
            String finalPrice = String.valueOf(receipt.getFinalPrice());
            textViewFinalPrice.setText(finalPrice);
        }
    }

    private void extractBitmap(int rotation, Uri uri) {
        try {
            bitmap = FileManager.decodeBitmapUri(NewReceiptActivity.this, uri);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

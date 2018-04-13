package com.paktalin.receiptanalyzer.activities;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    Uri imageUri;
    Bitmap bitmap;
    Receipt receipt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_receipt);

        ProgressBar progress = findViewById(R.id.progress_bar);
        progress.setIndeterminate(true);

        TextView textViewSupermarket = findViewById(R.id.supermarket);
        TextView textViewRetailer = findViewById(R.id.retailer);
        TextView textViewAddress = findViewById(R.id.address);
        TextView textViewFinalPrice = findViewById(R.id.final_price);

        int rotation = getIntent().getIntExtra("rotation", 0);
        imageUri = getIntent().getParcelableExtra("uri");
        extractBitmap(rotation);

        Thread thread = new Thread(() -> {
            receipt = ReceiptExtractor.extract(NewReceiptActivity.this, bitmap);
        });
        thread.start();
        while(thread.isAlive());
        textViewSupermarket.setText(receipt.getSupermarket());
        textViewRetailer.setText(receipt.getRetailer());
        textViewAddress.setText(receipt.getAddress());
        String finalPrice = String.valueOf(receipt.getFinalPrice());
        textViewFinalPrice.setText(finalPrice);
    }

    private void extractBitmap(int rotation) {
        try {
            bitmap = FileManager.decodeBitmapUri(NewReceiptActivity.this, imageUri);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

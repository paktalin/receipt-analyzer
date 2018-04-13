package com.paktalin.receiptanalyzer.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
        TextView textViewSupermarket = findViewById(R.id.supermarket);
        TextView textViewRetailer = findViewById(R.id.retailer);
        imageUri = getIntent().getParcelableExtra("uri");
        try {
            bitmap = FileManager.decodeBitmapUri(NewReceiptActivity.this, imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(() -> {
            receipt = ReceiptExtractor.extract(NewReceiptActivity.this, bitmap);
        });
        thread.start();
        while(thread.isAlive());
        textViewSupermarket.setText(receipt.getSupermarket());
        textViewRetailer.setText(receipt.getRetailer());


    }
}

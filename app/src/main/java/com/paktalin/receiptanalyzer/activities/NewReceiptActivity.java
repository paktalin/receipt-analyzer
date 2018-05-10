package com.paktalin.receiptanalyzer.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.recognition.ReceiptRecognizer;
import com.paktalin.receiptanalyzer.activities.adapters.PurchasesAdapter;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;

import com.paktalin.receiptanalyzer.data.Contracts.*;

import static com.paktalin.receiptanalyzer.Supermarkets.*;


/**
 * Created by Paktalin on 12/04/2018.
 */

public class NewReceiptActivity extends AppCompatActivity {
    private static final String TAG = NewReceiptActivity.class.getSimpleName();

    Receipt receipt;
    PurchasesAdapter adapter;
    ListView listView;
    EditText editTextFinalPrice;
    SQLiteDatabase db;
    Purchase[] purchases;
    long firstPurchaseID;
    long currentDate;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
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
            try {
                bitmap = FileManager.decodeBitmapUri_Rotate(rotation, imageUri, context);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(context, "An error occured. Please, try again", Toast.LENGTH_SHORT);
                toast.show();
            }
            receipt = ReceiptRecognizer.extract(context, bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
            editTextFinalPrice = findViewById(R.id.final_price);

            if (receipt != null) {
                showReceipt();
            } else {
                createDialog();
            }
        }
    }

    private void showReceipt() {
        String supermarket = receipt.getSupermarket();
        receipt.extractPurchases(NewReceiptActivity.this);
        purchases = receipt.getPurchases();
        for (Purchase p : purchases)
            p.purchaseInfo();

        ((TextView) findViewById(R.id.supermarket)).setText(supermarket);
        ((TextView) findViewById(R.id.retailer)).setText(receipt.getRetailer());
        ((TextView) findViewById(R.id.address)).setText(receipt.getAddress());
        String finalPrice = String.valueOf(receipt.getFinalPrice());
        editTextFinalPrice.setText(finalPrice);

        adapter = new PurchasesAdapter(NewReceiptActivity.this, purchases);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        findViewById(R.id.euro_sign).setVisibility(View.VISIBLE);

        Button buttonOk = findViewById(R.id.button_ok);
        buttonOk.setVisibility(View.VISIBLE);
        buttonOk.setOnClickListener(buttonOkListener);

        Button buttonCancel = findViewById(R.id.button_cancel);
        buttonCancel.setVisibility(View.VISIBLE);
        buttonCancel.setOnClickListener(v -> {
            Intent mainActivityIntent = new Intent(NewReceiptActivity.this, MainActivity.class);
            mainActivityIntent.putExtra("position", 1);
            startActivity(mainActivityIntent);
        });
    }

    private void createDialog() {
        String[] items = new String[]{MAXIMA, RIMI, SELVER, PRISMA, KONSUM};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewReceiptActivity.this);
        builder.setTitle("We couldn't identify the supermarket. Could you help us?");
        builder.setCancelable(true);
        builder.setSingleChoiceItems(items, -1, (dialog, which) -> {
            receipt = ReceiptRecognizer.extract(NewReceiptActivity.this, items[which]);
            dialog.dismiss();
            showReceipt();
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }



    View.OnClickListener buttonOkListener = v -> {
        currentDate = System.currentTimeMillis();
        DatabaseHelper dbHelper = new DatabaseHelper(NewReceiptActivity.this);
        db = dbHelper.getWritableDatabase();
        for (int i = 0; i < adapter.getCount(); i++)
            if (!savePurchases(i)) {
                Toast toast = Toast.makeText(NewReceiptActivity.this, "Something went wrong!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        if (!saveReceipt()) {
            Toast toast = Toast.makeText(NewReceiptActivity.this, "The app's Database has changed. Update your app, please!", Toast.LENGTH_LONG);
            toast.show();
        }
        Intent mainActivityIntent = new Intent(NewReceiptActivity.this, MainActivity.class);
        mainActivityIntent.putExtra("position", 1);
        startActivity(mainActivityIntent);
    };

    private boolean saveReceipt() {
        ContentValues values = new ContentValues();
        values.put(ReceiptEntry.COLUMN_SUPERMARKET, receipt.getSupermarket());
        values.put(ReceiptEntry.COLUMN_RETAILER, receipt.getRetailer());
        values.put(ReceiptEntry.COLUMN_ADDRESS, receipt.getAddress());
        values.put(ReceiptEntry.COLUMN_DATE_RECEIPT, currentDate);
        values.put(ReceiptEntry.COLUMN_FIRST_PURCHASE_ID, firstPurchaseID);
        values.put(ReceiptEntry.COLUMN_PURCHASES_LENGTH, purchases.length);
        try {
            float finalPrice = Float.parseFloat(String.valueOf(editTextFinalPrice.getText()));
            values.put(ReceiptEntry.COLUMN_FINAL_PRICE, finalPrice);
        } catch (Exception e) {
            Toast toast = Toast.makeText(NewReceiptActivity.this, "Wrong mFormat of the final price!", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        long newRowId = db.insert(ReceiptEntry.TABLE_NAME_RECEIPT, null, values);
        return newRowId != -1;
    }

    private boolean savePurchases(int i) {
        Purchase p = (Purchase) listView.getItemAtPosition(i);
        ContentValues values = new ContentValues();
        values.put(PurchaseEntry.COLUMN_TITLE, p.getTitle());
        if (p.getCategory() == null)
            p.setCategory("Uncategorized");
        values.put(PurchaseEntry.COLUMN_CATEGORY, p.getCategory());
        values.put(PurchaseEntry.COLUMN_PRICE, p.getPrice());
        values.put(PurchaseEntry.COLUMN_DATE_PURCHASE, currentDate);
        long newRowId = db.insert(PurchaseEntry.TABLE_NAME_PURCHASE, null, values);
        if (i == 0)
            firstPurchaseID = newRowId;
        return newRowId != -1;
    }
}

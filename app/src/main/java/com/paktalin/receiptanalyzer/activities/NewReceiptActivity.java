package com.paktalin.receiptanalyzer.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paktalin.receiptanalyzer.managers.FileManager;
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
    long firstPurchaseID, currentDate;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            int rotation = getIntent().getIntExtra("rotation", 0);
            Uri imageUri = getIntent().getParcelableExtra("uri");
            try {
                bitmap = FileManager.decodeBitmapUri_Rotate(rotation, imageUri, NewReceiptActivity.this);
            } catch (OutOfMemoryError e) {
                return "An error occured. Please, try again";
            }
            if (bitmap == null)
                return "We couldn't load the image. Please, try again";
            else {
                receipt = ReceiptRecognizer.extract(NewReceiptActivity.this, bitmap);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String errorMessage) {
            findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
            if (errorMessage == null) {
                editTextFinalPrice = findViewById(R.id.final_price);
                if (receipt != null)
                    showReceipt();
                else
                    showDialogChooseSupermarket();
            } else {
                showToast(errorMessage);
                startMainActivity();
            }
        }
    }

    private void showReceipt() {
        extractReceiptData();
        setTextInTextViews();
        findViewById(R.id.euro_sign).setVisibility(View.VISIBLE);
        setPurchasesListView();
        setButtonOk();
        setButtonCancel();
    }

    private void extractReceiptData() {
        receipt.extractPurchases(NewReceiptActivity.this);
        purchases = receipt.getPurchases();
    }

    private void setTextInTextViews() {
        ((TextView) findViewById(R.id.supermarket)).setText(receipt.getSupermarket());
        ((TextView) findViewById(R.id.retailer)).setText(receipt.getRetailer());
        ((TextView) findViewById(R.id.address)).setText(receipt.getAddress());
        editTextFinalPrice.setText(String.valueOf(receipt.getFinalPrice()));
    }

    private void setPurchasesListView() {
        if (purchases.length != 0) {
            adapter = new PurchasesAdapter(NewReceiptActivity.this, purchases);
            listView = findViewById(R.id.list_view);
            listView.setAdapter(adapter);
        } else {
            showDialogNoPurchases();
        }
    }

    private void showDialogNoPurchases() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewReceiptActivity.this);
        builder.setTitle("Unfortunately, we couldn't read the purchases");
        builder.setMessage("Would you like to add them manually or delete the receipt?");
        builder.setPositiveButton("Add manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Delete receipt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startMainActivity();
            }
        });
        builder.create().show();
    }

    private void startMainActivity() {
        Intent intent = new Intent(NewReceiptActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(NewReceiptActivity.this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void setButtonOk() {
        Button buttonOk = findViewById(R.id.button_ok);
        buttonOk.setVisibility(View.VISIBLE);
        buttonOk.setOnClickListener(buttonOkListener);
    }

    private void setButtonCancel() {
        Button buttonCancel = findViewById(R.id.button_cancel);
        buttonCancel.setVisibility(View.VISIBLE);
        buttonCancel.setOnClickListener(v -> {
            startMainActivity();
        });
    }


    private void showDialogChooseSupermarket() {
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
            showToast("Unfortunately, we couldn't save purchases data.");
                return;
            }
        if (!saveReceipt())
            showToast("Unfortunately, we couldn't save receipt data.");
        startMainActivity();
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
            showToast("Wrong mFormat of the final price!");
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
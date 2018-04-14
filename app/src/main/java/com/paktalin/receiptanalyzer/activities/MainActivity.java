package com.paktalin.receiptanalyzer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.BuildConfig;
import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;

import java.io.File;
import java.util.Map;

import static com.paktalin.receiptanalyzer.DataKeeper.*;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    Uri imageUri;
    private static final int REQUEST_GET_FROM_CAMERA = 30;
    private static final int REQUEST_GET_FROM_GALLERY = 40;
    SharedPreferences appData;
    SharedPreferences retailersData;
    SharedPreferences addressesData;
    TextView textViewSupermarkets, textViewRetailers, textViewAddresses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewSupermarkets = findViewById(R.id.text_view_supermarkets);
        textViewRetailers = findViewById(R.id.text_view_retailers);
        textViewAddresses = findViewById(R.id.text_view_addresses);

        Button buttonNewReceipt = findViewById(R.id.button_new_receipt);
        FileManager.setUpAppDir(MainActivity.this);

        appData = this.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        (new SupermarketLoader()).execute();
        retailersData = this.getSharedPreferences(RETAILERS_PREFERENCES, Context.MODE_PRIVATE);
        (new RetailersLoader()).execute();
        addressesData = this.getSharedPreferences(ADDRESSES_PREFERENCES, Context.MODE_PRIVATE);
        (new AddressesLoader()).execute();

        buttonNewReceipt.setOnClickListener(v -> createDialog(MainActivity.this));
    }

    private void createDialog(Context context) {
        String buttonCamera = "From camera";
        String buttonGallery = "From gallery";

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setCancelable(true);

        dialog.setPositiveButton(buttonCamera, (dialog1, which) -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photo = new File(FileManager.getPictureDirPath(), "last.jpg");
            imageUri = FileProvider.getUriForFile(MainActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", photo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_GET_FROM_CAMERA);
        });

        dialog.setNeutralButton(buttonGallery, (dialog12, which) -> {
            Intent galleryIntent = (new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI));
            startActivityForResult(galleryIntent, REQUEST_GET_FROM_GALLERY);
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
            if (requestCode == REQUEST_GET_FROM_GALLERY)
                imageUri = data.getData();
            editIntent.putExtra("uri", imageUri);
            startActivity(editIntent);
        }
    }

    class SupermarketLoader extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return extractMap(appData.getAll());
        }

        @Override
        protected void onPostExecute(String strings) {
            textViewSupermarkets.setText(strings);
        }
    }

    class RetailersLoader extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return extractMap(retailersData.getAll());
        }

        @Override
        protected void onPostExecute(String strings) {
            textViewRetailers.setText(strings);
        }
    }

    class AddressesLoader extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return extractMap(addressesData.getAll());
        }

        @Override
        protected void onPostExecute(String strings) {
            textViewAddresses.setText(strings);
        }
    }

    private String extractMap(Map<String, ?> map) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry entry : map.entrySet()) {
            Log.d(TAG, "key: " + entry.getKey());
            builder
                    .append(entry.getKey())
                    .append(" = ")
                    .append(entry.getValue().toString())
                    .append("\n");
        }
        return builder.toString();
    }

}
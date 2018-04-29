package com.paktalin.receiptanalyzer.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.paktalin.receiptanalyzer.BuildConfig;
import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.*;
import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.*;

/**
 * Created by Paktalin on 26/04/2018.
 */

public class OverviewActivity extends AppCompatActivity{
    private static final String TAG = ViewReceiptActivity.class.getSimpleName();

    TreeMap<String, Integer> categories, supermarkets;
    SQLiteDatabase db;
    long[] periodsMillisec = {7776000000L, 2592000000L, 1209600000L, 604800000L};
    long currentTime, startingTime;
    float expenses = 0;
    Uri imageUri;
    private static final int REQUEST_GET_FROM_CAMERA = 30;
    private static final int REQUEST_GET_FROM_GALLERY = 40;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        DatabaseHelper helper = new DatabaseHelper(OverviewActivity.this);
        db = helper.getReadableDatabase();

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(periodListener);

        setCategories();
        calculateDataInPeriod();
        setPieChart();
        setBarChart();

        FileManager.setUpAppDir(OverviewActivity.this);
        findViewById(R.id.button_view_receipts).setOnClickListener(v -> {
            Intent intent = new Intent(OverviewActivity.this, AllReceiptsActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.button_new_receipt).setOnClickListener(v -> createDialog(OverviewActivity.this));
    }

    void createDialog(Context context) {
        String buttonCamera = "From camera";
        String buttonGallery = "From gallery";

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setCancelable(true);

        dialog.setPositiveButton(buttonCamera, (dialog1, which) -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photo = new File(FileManager.getPictureDirPath(), "last.jpg");
            imageUri = FileProvider.getUriForFile(OverviewActivity.this,
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
            Intent editIntent = new Intent(OverviewActivity.this, EditActivity.class);
            if (requestCode == REQUEST_GET_FROM_GALLERY)
                imageUri = data.getData();
            editIntent.putExtra("uri", imageUri);
            startActivity(editIntent);
        }
    }

    AdapterView.OnItemSelectedListener periodListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            currentTime = System.currentTimeMillis();
            startingTime = currentTime - periodsMillisec[position];
            calculateDataInPeriod();
            setCategories();
            setPieChart();
            setBarChart();
            String expensesStr = "You spent " + expenses + "€ ";
            ((TextView)findViewById(R.id.expenses)).setText(expensesStr);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private void calculateDataInPeriod() {
        String selection = COLUMN_SUPERMARKET + ", " + COLUMN_FINAL_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_RECEIPT + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + startingTime + " AND " + currentTime;
        Cursor cursor = db.rawQuery(query, null);

        int finalPriceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);
        int supermarketIndex = cursor.getColumnIndex(COLUMN_SUPERMARKET);

        supermarkets = new TreeMap<>();
        expenses = 0;
        while (cursor.moveToNext()) {
            expenses += cursor.getFloat(finalPriceIndex);
            String key = cursor.getString(supermarketIndex);
            if (supermarkets.containsKey(key))
                supermarkets.put(key, supermarkets.get(key) + 1);
            else
                supermarkets.put(key, 1);
        }
        cursor.close();
    }

    private void setCategories() {
        String query = "SELECT " + COLUMN_CATEGORY + " FROM " + TABLE_NAME_PURCHASE + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + startingTime + " AND " + currentTime;
        Cursor cursor = db.rawQuery(query, null);
        int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

        categories = new TreeMap<>();
        while (cursor.moveToNext()) {
            String key = cursor.getString(categoryIndex);
            if (categories.containsKey(key))
                categories.put(key, categories.get(key) + 1);
            else
                categories.put(key, 1);
        }
        cursor.close();
    }


    private void setPieChart() {
        PieChart pieChart = findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            String key = entry.getKey();
            float value = (float)entry.getValue();
            yValues.add(new PieEntry(value, key));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "Categories");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
    }

    private void setBarChart() {
        BarChartSupermarkets.createBarChart(findViewById(R.id.bar_chart), supermarkets);

        int height = 50 * supermarkets.size();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        params.addRule(RelativeLayout.BELOW, R.id.space1);
        findViewById(R.id.bar_chart_layout).setLayoutParams(params);

    }
}
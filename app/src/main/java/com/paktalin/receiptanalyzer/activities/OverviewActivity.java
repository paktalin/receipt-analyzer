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
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.paktalin.receiptanalyzer.BuildConfig;
import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.data.DataManager;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_DATE_RECEIPT;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_FINAL_PRICE;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.TABLE_NAME_RECEIPT;

/**
 * Created by Paktalin on 26/04/2018.
 */

public class OverviewActivity extends AppCompatActivity{
    private static final String TAG = ViewReceiptActivity.class.getSimpleName();

    SQLiteDatabase db;
    long[] periodsMillisec = {7776000000L, 2592000000L, 1209600000L, 604800000L};
    Uri imageUri;
    private static final int REQUEST_GET_FROM_GALLERY = 40;
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        DatabaseHelper helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(periodListener);
        spinner.setSelection(1);

        FileManager.setUpAppDir(OverviewActivity.this);
        findViewById(R.id.button_view_receipts).setOnClickListener(v -> {
            Intent intent = new Intent(OverviewActivity.this, AllReceiptsActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.button_new_receipt).setOnClickListener(v -> createDialog(OverviewActivity.this));
        findViewById(R.id.button_more).setOnClickListener(v -> {
            Intent intent = new Intent(OverviewActivity.this, DetailedExpensesActivity.class);
            startActivity(intent);
        });
    }

    void createDialog(Context context) {
        String buttonCamera = "From camera";
        String buttonGallery = "From gallery";

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(true);

        dialog.setPositiveButton(buttonCamera, (dialog1, which) -> {
            int REQUEST_GET_FROM_CAMERA = 30;
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
            long currentTime = System.currentTimeMillis();
            Object[] data = DataManager.extractData(db, currentTime - periodsMillisec[position], currentTime);
            //setPieChart((TreeMap<String, Integer>) data[1]);
            //setBarChart((TreeMap<String, Integer>) data[0]);
            setLineChart();
            String expensesStr = "You spent " + data[2] + "€ ";
            ((TextView)findViewById(R.id.expenses)).setText(expensesStr);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    /*private void setPieChart(TreeMap<String, Integer> categories) {
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

    private void setBarChart(TreeMap<String, Integer> supermarkets) {
        BarChartSupermarkets.createBarChart(findViewById(R.id.bar_chart), supermarkets);

        int height = 50 * supermarkets.size();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        params.addRule(RelativeLayout.BELOW, R.id.space1);
        findViewById(R.id.bar_chart_layout).setLayoutParams(params);
    }*/

    private LinkedHashMap<String, Float> calculateExpenses() {
        String[] projection = new String[]{COLUMN_FINAL_PRICE, COLUMN_DATE_RECEIPT};
        Cursor cursor = db.query(TABLE_NAME_RECEIPT, projection,
                null, null, null, null, null, null);

        Cursor cursorMin = db.query(TABLE_NAME_RECEIPT, new String[] { "min(" + COLUMN_DATE_RECEIPT + ")" }, null, null,
                null, null, null);

        int priceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);
        int dateIndex = cursor.getColumnIndex(COLUMN_DATE_RECEIPT);
        cursorMin.moveToNext();
        long startDate = cursorMin.getLong(0);
        cursorMin.close();

        LinkedHashMap<String, Float> days = new LinkedHashMap<>();
        while (cursor.moveToNext()) {
            String key = sdf.format(new Date(cursor.getLong(dateIndex)));
            Float value = cursor.getFloat(priceIndex);
            if (days.containsKey(key))
                days.put(key, value + days.get(key));
            else
                days.put(key, value);
        }
        cursor.close();
        return sort(days, startDate);
    }

    private LinkedHashMap<String, Float> sort(LinkedHashMap<String, Float> days, long startLong) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.setTimeInMillis(startLong);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        end.setTimeInMillis(System.currentTimeMillis());

        LinkedHashMap<String, Float> sorted = new LinkedHashMap<>();
        for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            String key = sdf.format(date);
            if (days.containsKey(sdf.format(date)))
                sorted.put(key, days.get(key));
            else
                sorted.put(key, 0f);
        }
        return sorted;
    }

    private void setLineChart() {

        LineChart lineChart = findViewById(R.id.line_chart);
        ArrayList<Entry> entries = new ArrayList<>();

        LinkedHashMap<String, Float> expenses = calculateExpenses();
        String[] labels = new String[expenses.size()];

        int i = 0;
        for (Map.Entry entry : expenses.entrySet()) {
            entries.add(new Entry(i, (Float)entry.getValue()));
            labels[i] = (String) entry.getKey();
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Labelll");
        LineData data = new LineData(dataSet);
        data.setValueFormatter(new IntFormatter());
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.getXAxis().setDrawGridLines(false);
        //lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setGranularity(1);
        //lineChart.getXAxis().setValueFormatter(new LabelFormatter(labels));
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setData(data);
    }

    public static class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mLabels[(int) value];
        }
    }

    public static class IntFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (value == 0)
                return "";
            return "" + value + "€";
        }
    }

}
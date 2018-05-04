package com.paktalin.receiptanalyzer.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

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
 * Created by Paktalin on 05/05/2018.
 */

public class DetailedExpensesActivity extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("d MMM");
    static ArrayList<Entry> entries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_expenses);
        setLineChart();
    }

    private void setLineChart() {

        LineChart lineChart = findViewById(R.id.line_chart);
        entries = new ArrayList<>();

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
        data.setValueFormatter(new PriceFormatter());
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setGranularity(1);
        lineChart.getXAxis().setValueFormatter(new LabelFormatter(labels));
        lineChart.getXAxis().setLabelRotationAngle(-45);
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
            if (entries.get((int)value).getY() == 0)
                return "";
            return mLabels[(int) value];
        }
    }

    public static class PriceFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (value == 0)
                return "";
            return "" + value + "€";
        }
    }

    private LinkedHashMap<String, Float> calculateExpenses() {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
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
}

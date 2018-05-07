package com.paktalin.receiptanalyzer.activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_DATE_RECEIPT;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_FINAL_PRICE;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_SUPERMARKET;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.TABLE_NAME_RECEIPT;

/**
 * Created by Paktalin on 07/05/2018.
 */

public class ChartManager {
    private static final String TAG = ChartManager.class.getSimpleName();

    public static void retrieveData() {

    }


    public static HorizontalBarChart setSupermarketsChart(HorizontalBarChart barChart, Context context, long from) {
        TreeMap<String, Float> supermarkets = getSupermarketsData(context, from);
        ArrayList<BarEntry> entries = new ArrayList<>();
        float[] values = new float[supermarkets.size()];
        String[] labels = new String[supermarkets.size()];
        int i = 0;
        for (Map.Entry entry : supermarkets.entrySet()) {
            values[i] = (float)entry.getValue();
            labels[i] = (String) entry.getKey();
            i++;
        }
        entries.add(new BarEntry(0, values));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setStackLabels(labels);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(false);
        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setValueFormatter(new YAxisFormatter());
        barChart.getAxisLeft().setAxisMinimum(1);

        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        return barChart;
    }

    static TreeMap<String, Float> getSupermarketsData(Context context, long from) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        TreeMap<String, Float> supermarkets = new TreeMap<>();
        String selection = COLUMN_SUPERMARKET + ", " + COLUMN_FINAL_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_RECEIPT + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + from + " AND " + System.currentTimeMillis();
        Cursor cursor = db.rawQuery(query, null);

        int finalPriceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);
        int supermarketIndex = cursor.getColumnIndex(COLUMN_SUPERMARKET);

        while (cursor.moveToNext()) {
            String key = cursor.getString(supermarketIndex);
            if (supermarkets.containsKey(key))
                supermarkets.put(key, supermarkets.get(key) + cursor.getFloat(finalPriceIndex));
            else
                supermarkets.put(key, cursor.getFloat(finalPriceIndex));
        }
        cursor.close();
        Log.d(TAG, String.valueOf(supermarkets));
        return supermarkets;
    }

    static class YAxisFormatter implements IAxisValueFormatter{
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return String.format("%.0f", value) + " €";
        }
    }
}

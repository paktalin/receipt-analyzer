package com.paktalin.receiptanalyzer.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.*;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.*;

/**
 * Created by Paktalin on 07/05/2018.
 */

public class ChartManager {
    private static final String TAG = ChartManager.class.getSimpleName();
    private SimpleDateFormat sdf = new SimpleDateFormat("d MMM");

    private int[] colors = new int[] {
            Color.parseColor("#996699"),
            Color.parseColor("#6161A8"),
            Color.parseColor("#1F1F54"),
            Color.parseColor("#006699"),
            Color.parseColor("#56A3A6"),
            Color.parseColor("#00BFFF"),
            Color.parseColor("#1E90FF")};

    private TreeMap<String, Float> supermarkets, categories;
    private LinkedHashMap<String, Float> expenses;
    private SQLiteDatabase db;
    private long to, from;
    private float overall;

    public void retrieveData (Context context, long period) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getReadableDatabase();
        to = System.currentTimeMillis();
        from = to - period;

        retrieveReceiptData();
        retrieveCategories();
    }

    public boolean emptyData() {
        return supermarkets.isEmpty();
    }

    private void retrieveReceiptData() {
        supermarkets = new TreeMap<>();
        expenses = new LinkedHashMap<>();
        overall = 0;

        String selection = COLUMN_SUPERMARKET + ", " + COLUMN_FINAL_PRICE + ", " + COLUMN_DATE_RECEIPT;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_RECEIPT + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + from + " AND " + to;
        Cursor cursor = db.rawQuery(query, null);
        createExpensesInSelectedPeriod();
        while (cursor.moveToNext()) {
            Float price = cursor.getFloat(cursor.getColumnIndex(COLUMN_FINAL_PRICE));
            addToOverall(price);
            addToSupermarkets(cursor, cursor.getColumnIndex(COLUMN_SUPERMARKET), cursor.getColumnIndex(COLUMN_FINAL_PRICE));
            addToExpenses(cursor, cursor.getColumnIndex(COLUMN_DATE_RECEIPT), price);
        }
        cursor.close();
    }

    private void addToSupermarkets(Cursor cursor, int supermarketIndex, float value) {
        String keySupermarkets = cursor.getString(supermarketIndex);
        if (supermarkets.containsKey(keySupermarkets))
            supermarkets.put(keySupermarkets, supermarkets.get(keySupermarkets) + value);
        else
            supermarkets.put(keySupermarkets, value);
    }

    private void addToOverall(float value) {
        overall += value;
    }

    private void addToExpenses(Cursor cursor, int dateIndex, float value) {
        String keyExpenses = sdf.format(new Date(cursor.getLong(dateIndex)));
        expenses.put(keyExpenses, value + expenses.get(keyExpenses));
    }

    private void createExpensesInSelectedPeriod() {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.setTimeInMillis(from);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        end.setTimeInMillis(to);

        for (Date date = start.getTime(); !date.after(end.getTime()); start.add(Calendar.DATE, 1), date = start.getTime()) {
            String key = sdf.format(date);
            expenses.put(key, 0f);
        }
    }

    private void retrieveCategories() {
        categories = new TreeMap<>();
        String selection = COLUMN_CATEGORY + ", " + COLUMN_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_PURCHASE + " WHERE " + COLUMN_DATE_PURCHASE + " BETWEEN "
                + from + " AND " + to;
        Cursor cursor = db.rawQuery(query, null);

        int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
        int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);

        while (cursor.moveToNext()) {
            String key = cursor.getString(categoryIndex);
            if (categories.containsKey(key))
                categories.put(key, categories.get(key) + cursor.getFloat(priceIndex));
            else
                categories.put(key, cursor.getFloat(priceIndex));
        }
        cursor.close();
    }

    public HorizontalBarChart setSupermarketsChart(HorizontalBarChart barChart) {
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
        dataSet.setColors(colors);

        dataSet.setDrawValues(false);
        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setValueFormatter(new YAxisFormatter());

        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        barChart.setScaleEnabled(false);
        return barChart;
    }

    static class YAxisFormatter implements IAxisValueFormatter{
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return String.format("%.0f", value) + " €";
        }
    }

    public PieChart setPieChart(PieChart pieChart) {
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categories.entrySet()) {
            String key = entry.getKey();
            float value = entry.getValue();
            yValues.add(new PieEntry(value, key));
        }
        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        pieChart.setDrawEntryLabels(false);
        pieChart.setExtraLeftOffset(50f);
        pieChart.setData(data);

        pieChart.setCenterText("Total spent\n" + String.format("%.2f", overall) + " €");
        pieChart.setHoleRadius(70f);
        pieChart.setHoleColor(Color.TRANSPARENT);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setXOffset(20f);
        legend.setYOffset(50f);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.CIRCLE);
        return pieChart;
    }

    public LineChart setLineChart(LineChart lineChart) {
        ArrayList<Entry> entries = new ArrayList<>();
        String[] labels = new String[expenses.size()];

        int i = 0;
        for (Map.Entry entry : expenses.entrySet()) {
            entries.add(new Entry(i, (Float)entry.getValue()));
            labels[i] = (String) entry.getKey();
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(2f);
        dataSet.setColor(Color.parseColor("#006699"));
        dataSet.setDrawValues(false);

        LineData data = new LineData(dataSet);
        data.setValueFormatter(new PriceFormatter());

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setValueFormatter(new YAxisFormatter());
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisLeft().setGridColor(Color.parseColor("#9EB9C6"));

        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setGranularity(1);
        lineChart.getXAxis().setValueFormatter(new LabelFormatter(labels));
        lineChart.getXAxis().setLabelRotationAngle(-45);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawAxisLine(false);

        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setData(data);
        return lineChart;
    }

    public class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mLabels[(int) value];
        }
    }

    public static class PriceFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (value == 0)
                return "";
            return String.format("%.2f", value) + "€";
        }
    }
}

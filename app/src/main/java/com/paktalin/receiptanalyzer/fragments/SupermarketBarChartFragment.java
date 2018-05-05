package com.paktalin.receiptanalyzer.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_DATE_RECEIPT;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_FINAL_PRICE;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_SUPERMARKET;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.TABLE_NAME_RECEIPT;

/**
 * Created by Paktalin on 05/05/2018.
 */

public class SupermarketBarChartFragment extends Fragment {
    TreeMap<String, Float> supermarkets;
    BarChart barChart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supermarket_barchart, container, false);
        long from = getArguments().getLong("from");
        setSupermarkets(from);
        barChart = view.findViewById(R.id.supermarket_bar_chart);
        setBarChart();

        return view;
    }

    private TreeMap<String, Float> setSupermarkets(long from) {
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        supermarkets = new TreeMap<>();
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
        return supermarkets;
    }

    void setBarChart() {
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

        BarDataSet dataSet = new BarDataSet(entries, "Expenses");
        dataSet.setStackLabels(labels);
        BarData data = new BarData(dataSet);
        barChart.setData(data);

    }
}

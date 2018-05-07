package com.paktalin.receiptanalyzer.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.activities.BarChartSupermarkets;
import com.paktalin.receiptanalyzer.activities.ChartManager;
import com.paktalin.receiptanalyzer.activities.EditActivity;
import com.paktalin.receiptanalyzer.activities.MainActivity;
import com.paktalin.receiptanalyzer.data.DataManager;
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

public class OverviewFragment extends Fragment {
    private static final String TAG = OverviewFragment.class.getSimpleName();


    SQLiteDatabase db;
    long[] periodsMillisec = {7776000000L, 2592000000L, 1209600000L, 604800000L};
    TreeMap<String, Float> supermarkets;
    HorizontalBarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        DatabaseHelper helper = new DatabaseHelper(getActivity());
        db = helper.getReadableDatabase();
        barChart = view.findViewById(R.id.supermarket_bar_chart);

        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(periodListener);
        spinner.setSelection(1);

        FileManager.setUpAppDir(getActivity());
        /*view.findViewById(R.id.button_more).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetailedExpensesActivity.class);
            startActivity(intent);
        });*/
        return view;
    }


    AdapterView.OnItemSelectedListener periodListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            long currentTime = System.currentTimeMillis();
            Object[] data = DataManager.extractData(db, currentTime - periodsMillisec[position]);

            barChart = ChartManager.setSupermarketsChart(barChart, getActivity(), currentTime - periodsMillisec[position]);
            setPieChart((TreeMap<String, Integer>) data[1]);
            /*setBarChart((TreeMap<String, Integer>) data[0]);*/
            /*String expenses = String.format("%.2f", (float)data[2]);
            String expensesStr = "You spent " + expenses + "€ ";
            ((TextView)getView().findViewById(R.id.expenses)).setText(expensesStr);*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };


    private void setPieChart(TreeMap<String, Integer> categories) {
        PieChart pieChart = getView().findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            String key = entry.getKey();
            float value = (float)entry.getValue();
            yValues.add(new PieEntry(value, key));
        }
        PieDataSet dataSet = new PieDataSet(yValues, "Categories");
        dataSet.setDrawValues(false);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        pieChart.setDrawEntryLabels(false);
        pieChart.setData(data);
    }

    /*private void setBarChart(TreeMap<String, Integer> supermarkets) {
        BarChartSupermarkets.createBarChart(getView().findViewById(R.id.bar_chart), supermarkets);

        int height = 50 * supermarkets.size();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        params.addRule(RelativeLayout.BELOW, R.id.space1);
        getView().findViewById(R.id.bar_chart_layout).setLayoutParams(params);
    }*/

}

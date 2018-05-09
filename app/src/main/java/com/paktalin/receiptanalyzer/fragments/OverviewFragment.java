package com.paktalin.receiptanalyzer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.ChartManager;

import java.text.SimpleDateFormat;


/**
 * Created by Paktalin on 05/05/2018.
 */

public class OverviewFragment extends Fragment {
    private static final String TAG = OverviewFragment.class.getSimpleName();

    HorizontalBarChart barChart;
    TextView supermarketsTV, productsTV;
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        FileManager.setUpAppDir(getActivity());

        barChart = view.findViewById(R.id.supermarket_bar_chart);
        pieChart = view.findViewById(R.id.pie_chart);
        supermarketsTV = view.findViewById(R.id.tv_supermarkets_period);
        productsTV = view.findViewById(R.id.tv_products_period);

        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(periodListener);
        return view;
    }

    AdapterView.OnItemSelectedListener periodListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SimpleDateFormat formatter = new SimpleDateFormat("d MMM");
            long[] periodsMillisec = {7776000000L, 2592000000L, 1209600000L, 604800000L};

            long from = System.currentTimeMillis() - periodsMillisec[position];
            String period = formatter.format(from) + " - " + formatter.format(System.currentTimeMillis());
            supermarketsTV.setText(period);
            productsTV.setText(period);

            barChart.clear();
            pieChart.clear();

            ChartManager manager = new ChartManager();
            manager.retrieveData(getActivity(), periodsMillisec[position]);
            if (manager.emptyData()) {
                pieChart.setVisibility(View.INVISIBLE);
                barChart.setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(getActivity(), "No data yet available. Please, add your first receipt", Toast.LENGTH_LONG);
                toast.show();
            }

            barChart = manager.setSupermarketsChart(barChart);
            pieChart = manager.setPieChart(pieChart);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            parent.setSelection(1);
        }
    };
}
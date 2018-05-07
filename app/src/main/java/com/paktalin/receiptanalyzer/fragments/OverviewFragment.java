package com.paktalin.receiptanalyzer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.paktalin.receiptanalyzer.FileManager;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.activities.ChartManager;


/**
 * Created by Paktalin on 05/05/2018.
 */

public class OverviewFragment extends Fragment {
    private static final String TAG = OverviewFragment.class.getSimpleName();


    long[] periodsMillisec = {7776000000L, 2592000000L, 1209600000L, 604800000L};
    HorizontalBarChart barChart;
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        barChart = view.findViewById(R.id.supermarket_bar_chart);
        pieChart = view.findViewById(R.id.pie_chart);

        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(periodListener);
        spinner.setSelection(1);

        FileManager.setUpAppDir(getActivity());
        return view;
    }

    AdapterView.OnItemSelectedListener periodListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ChartManager manager = new ChartManager();
            manager.retrieveData(getActivity(), periodsMillisec[position]);
            barChart = manager.setSupermarketsChart(barChart);
            pieChart = manager.setPieChart(pieChart);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };
}

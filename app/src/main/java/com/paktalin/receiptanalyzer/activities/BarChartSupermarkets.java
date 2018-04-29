package com.paktalin.receiptanalyzer.activities;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Paktalin on 29/04/2018.
 */

public class BarChartSupermarkets {

    public static BarChart createBarChart(BarChart barChart, TreeMap<String, Integer> supermarkets) {

        ArrayList<BarEntry> yValues = new ArrayList<>();

        String[] labels = new String[supermarkets.size()];
        int i = 0;
        for (Map.Entry<String, Integer> entry : supermarkets.entrySet()) {
            int value = entry.getValue();
            labels[i] = entry.getKey();
            yValues.add(new BarEntry(i, value));
            i++;
        }

        BarDataSet dataSet = new BarDataSet(yValues, "Supermarkets");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        data.setValueFormatter(new IntFormatter());

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setAxisMinimum(0);

        barChart.getXAxis().setValueFormatter(new LabelFormatter(labels));
        barChart.getXAxis().setGranularity(1);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        barChart.setData(data);
        return barChart;
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
            return "" + (int)value;
        }
    }
}

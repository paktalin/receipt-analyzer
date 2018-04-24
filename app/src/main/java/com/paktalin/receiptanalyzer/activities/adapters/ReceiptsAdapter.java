package com.paktalin.receiptanalyzer.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;

/**
 * Created by Paktalin on 24/04/2018.
 */

public class ReceiptsAdapter extends BaseAdapter {
    private static final String TAG = ReceiptsAdapter.class.getSimpleName();

    private Receipt[] receipts;
    private LayoutInflater inflater;


    public ReceiptsAdapter(Context context, Receipt[] receipts) {
        this.receipts = receipts;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return receipts.length;
    }

    @Override
    public Object getItem(int position) {
        return receipts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.receipt, null);
        }

        Receipt r = (Receipt) getItem(position);

        ((TextView)convertView.findViewById(R.id.supermarket_2)).setText(r.getSupermarket());
        //((TextView)convertView.findViewById(R.id.date)).setText(r.getDate());
        ((TextView)convertView.findViewById(R.id.final_price_2)).setText(String.valueOf(r.getFinalPrice()));
        return convertView;
    }
}

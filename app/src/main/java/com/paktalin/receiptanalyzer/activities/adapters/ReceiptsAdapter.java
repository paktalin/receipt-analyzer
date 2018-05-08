package com.paktalin.receiptanalyzer.activities.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;

import java.text.SimpleDateFormat;
import java.util.Date;


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

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_receipt, null);
        }

        Receipt r = (Receipt) getItem(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");

        ((TextView)convertView.findViewById(R.id.supermarket_2)).setText(r.getSupermarket());
        ((TextView)convertView.findViewById(R.id.date)).setText(sdf.format(new Date(r.getDate())));
        ((TextView)convertView.findViewById(R.id.final_price_2)).setText(r.getFinalPrice() + "€");

        return convertView;
    }


}

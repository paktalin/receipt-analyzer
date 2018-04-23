package com.paktalin.receiptanalyzer.activities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;

import java.util.ArrayList;

/**
 * Created by Paktalin on 21/04/2018.
 */

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Purchase> purchases;
    private LayoutInflater inflater;

    ListViewAdapter(Context context, ArrayList<Purchase> purchases) {
        this.purchases = purchases;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return purchases.size();
    }

    @Override
    public Object getItem(int position) {
        return purchases.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.purchase, null);
        }

        Purchase p = ((Purchase) getItem(position));
        String category = p.getCategory();
        float price = p.getPrice();

        EditText priceView = convertView.findViewById(R.id.price);
        EditText categoryView = convertView.findViewById(R.id.category);

        ((EditText) convertView.findViewById(R.id.title)).setText(p.getTitle());

        if (category == null) {
            categoryView.setHint("Category");
            categoryView.setHintTextColor(Color.MAGENTA);
        }
        categoryView.setText(p.getCategory());

        if(price == 0)
            priceView.setTextColor(Color.MAGENTA);
        priceView.setText(String.valueOf(price));

        return convertView;
    }
}

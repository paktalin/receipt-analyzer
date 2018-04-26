package com.paktalin.receiptanalyzer.activities.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;

/**
 * Created by Paktalin on 21/04/2018.
 */

public class PurchasesAdapter extends BaseAdapter {
    private static final String TAG = PurchasesAdapter.class.getSimpleName();

    private Purchase[] purchases;
    private LayoutInflater inflater;

    public PurchasesAdapter(Context context, Purchase[] purchases) {
        this.purchases = purchases;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return purchases.length;
    }

    @Override
    public Object getItem(int position) {
        return purchases[position];
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.purchase_item, null);
        }

        Purchase p = ((Purchase) getItem(position));
        String category = p.getCategory();
        float price = p.getPrice();

        final EditText titleView = convertView.findViewById(R.id.title);
        titleView.setTag(position);
        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int p = Integer.parseInt(titleView.getTag().toString());
                purchases[p].setTitle(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        titleView.setText(p.getTitle());

        final EditText categoryView = convertView.findViewById(R.id.category);
        categoryView.setTag(position);
        categoryView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int p = Integer.parseInt(categoryView.getTag().toString());
                purchases[p].setCategory(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (category == null) {
            categoryView.setHint("Category");
            categoryView.setHintTextColor(Color.MAGENTA);
        }
        else categoryView.setText(category);

        final EditText priceView = convertView.findViewById(R.id.price);
        priceView.setTag(position);
        priceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int p = Integer.parseInt(priceView.getTag().toString());
                float _price;
                try {
                    _price = Float.parseFloat(String.valueOf(s));
                    purchases[p].setPrice(_price);
                    priceView.setTextColor(Color.BLACK);
                } catch (Exception e) {
                    priceView.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(price == 0)
            priceView.setTextColor(Color.MAGENTA);
        priceView.setText(String.valueOf(price));

        return convertView;
    }
}

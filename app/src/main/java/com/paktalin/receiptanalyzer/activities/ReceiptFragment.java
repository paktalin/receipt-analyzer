package com.paktalin.receiptanalyzer.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paktalin.receiptanalyzer.R;

/**
 * Created by Paktalin on 25/04/2018.
 */

public class ReceiptFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.receipt_fragment, container, false);
    }
}

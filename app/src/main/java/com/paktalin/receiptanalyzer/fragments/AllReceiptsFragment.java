package com.paktalin.receiptanalyzer.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.activities.ViewReceiptActivity;
import com.paktalin.receiptanalyzer.activities.adapters.ReceiptsAdapter;
import com.paktalin.receiptanalyzer.data.Contracts;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;

/**
 * Created by Paktalin on 05/05/2018.
 */

public class AllReceiptsFragment extends Fragment {

    private static final String TAG = AllReceiptsFragment.class.getSimpleName();
    Receipt[] receipts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_receipts, container, false);

        extractReceipts();
        ReceiptsAdapter adapter = new ReceiptsAdapter(getActivity(), receipts);

        ListView listView = view.findViewById(R.id.all_receipts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, v, position, id) -> {
            Receipt receipt = (Receipt) listView.getItemAtPosition(position);

            Intent intent = new Intent(getActivity(), ViewReceiptActivity.class);
            intent.putExtra("id", receipt.getID());
            startActivity(intent);
        });

        return view;
    }

    private void extractReceipts() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                Contracts.ReceiptEntry.COLUMN_SUPERMARKET,
                Contracts.ReceiptEntry.COLUMN_FINAL_PRICE,
                Contracts.ReceiptEntry.COLUMN_DATE_RECEIPT,
                Contracts.ReceiptEntry._ID};

        Cursor cursor = db.query(Contracts.ReceiptEntry.TABLE_NAME_RECEIPT, projection, null, null, null, null, null);

        receipts = new Receipt[cursor.getCount()];
        int supermarketColumnIndex = cursor.getColumnIndex(Contracts.ReceiptEntry.COLUMN_SUPERMARKET);
        int finalPriceColumnIndex = cursor.getColumnIndex(Contracts.ReceiptEntry.COLUMN_FINAL_PRICE);
        int dateColumnIndex = cursor.getColumnIndex(Contracts.ReceiptEntry.COLUMN_DATE_RECEIPT);
        int idColumnIndex = cursor.getColumnIndex(Contracts.ReceiptEntry._ID);

        int i = 0;
        while (cursor.moveToNext()) {
            receipts[i] = new Receipt();

            receipts[i].setSupermarket(cursor.getString(supermarketColumnIndex));
            receipts[i].setFinalPrice(cursor.getFloat(finalPriceColumnIndex));
            receipts[i].setDate(cursor.getLong(dateColumnIndex));
            receipts[i].setID(cursor.getLong(idColumnIndex));
            i++;
        }
        cursor.close();
    }
}

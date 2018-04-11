package com.paktalin.receiptanalyzer.data;

import android.provider.BaseColumns;

/**
 * Created by Paktalin on 10/04/2018.
 */

public final class ReceiptContract {

    public static final class ReceiptEntry implements BaseColumns {
        public final static String TABLE_NAME = "receipts";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SUPERMARKET = "supermarket";
        public final static String COLUMN_RETAILER = "retailer";
        public final static String COLUMN_ADDRESS = "address";
        public final static String COLUMN_FINAL_PRICE = "final_price";
    }
}

package com.paktalin.receiptanalyzer.data;

import android.provider.BaseColumns;

/**
 * Created by Paktalin on 10/04/2018.
 */

public final class Contracts {

    public static final class ReceiptEntry implements BaseColumns {
        public final static String TABLE_NAME_RECEIPT = "receipts";

        public final static String COLUMN_SUPERMARKET = "supermarket";
        public final static String COLUMN_RETAILER = "retailer";
        public final static String COLUMN_ADDRESS = "address";
        public final static String COLUMN_FINAL_PRICE = "final_price";
        public final static String COLUMN_DATE_RECEIPT = "creation_date";
        public final static String COLUMN_FIRST_PURCHASE_ID = "first_purchase_id";
        public final static String COLUMN_PURCHASES_LENGTH = "purchases_number";
    }

    public static final class PurchaseEntry implements BaseColumns {
        public final static String TABLE_NAME_PURCHASE = "purchases";

        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_CATEGORY = "category";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_DATE_PURCHASE = "creation_date";
    }
}
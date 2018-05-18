package com.paktalin.receiptanalyzer.tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.paktalin.receiptanalyzer.activities.NewReceiptActivity;
import com.paktalin.receiptanalyzer.receipts_data.Purchase;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;
import com.paktalin.receiptanalyzer.recognition.ReceiptRecognizer;

/**
 * Created by Paktalin on 18/05/2018.
 */

class RecognitionTest {
    private static final String TAG = RecognitionTest.class.getSimpleName();

    private static Receipt receipt;
    private static Purchase[] purchases;

    static void testExtractReceiptFromBitmap(Bitmap bitmap, Context context) {
        Log.d(TAG, "start extracting");
        receipt = ReceiptRecognizer.extract(context, bitmap);
        Log.d(TAG, "extraction done!");
        if (receipt != null) {
            Log.d(TAG, "receipt isn't null");
            testReceiptData(context);
        }
        else Log.d(TAG, "receipt = null");
    }

    private static void testReceiptData(Context context) {
        logInitialLines();
        logFilteredLines();
        testExtractPurchases(context);
        logPurchases();
    }

    private static void logInitialLines() {
        Log.d(TAG, "Initial lines\n" + receipt.getInitialLines());
    }

    private static void logFilteredLines() {
        Log.d(TAG, "\nFiltered lines\n" + receipt.getLines());
    }

    private static void testExtractPurchases(Context context) {
        receipt.extractPurchases(context);
        purchases = receipt.getPurchases();
    }

    private static void logPurchases() {
        for (Purchase purchase : purchases) {
            purchase.logPurchase();
        }
    }
}

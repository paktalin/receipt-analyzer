package com.paktalin.receiptanalyzer.tests;

import android.util.Log;

import com.paktalin.receiptanalyzer.activities.NewReceiptActivity;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;

/**
 * Created by Paktalin on 18/05/2018.
 */

public class ReceiptTest {
    private static final String TAG = ReceiptTest.class.getSimpleName();
    private Receipt receipt;


    public void test(Receipt receipt) {
        this.receipt = receipt;
        logReceipt();
    }

    private void logReceipt() {
        logStartIndex();
        logEndIndex();
        logLines();
    }

    private void logLines() {
        log("All lines");
        for (String line : receipt.getLines())
            log(line);
    }

    private void logStartIndex() {
        log("Start index");
        log(receipt.getPurchasesStart());
    }

    private void logEndIndex() {
        log("End index");
        log(receipt.getPurchasesEnd());
    }


    private void log(String text) {
        Log.d(TAG, text);
    }
    private void log(int text) {
        Log.d(TAG, String.valueOf(text));
    }
}

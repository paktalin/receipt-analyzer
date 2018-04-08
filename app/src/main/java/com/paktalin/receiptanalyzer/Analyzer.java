package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextRecognizer;
import com.paktalin.receiptanalyzer.receipts.KonsumReceipt;
import com.paktalin.receiptanalyzer.receipts.MaximaReceipt;
import com.paktalin.receiptanalyzer.receipts.PrismaReceipt;
import com.paktalin.receiptanalyzer.receipts.Receipt;
import com.paktalin.receiptanalyzer.receipts.RimiReceipt;
import com.paktalin.receiptanalyzer.receipts.SelverReceipt;

import java.util.ArrayList;

/**
 * Created by Paktalin on 22-Mar-18.
 */

class Analyzer {
    private static final String TAG = Analyzer.class.getSimpleName();

    static String analyze(Context context, Bitmap bitmap) {
        ArrayList<String> lines = recognize(context, bitmap);
        String filteredString = StringFilter.filter(lines);
        String[] linesArray = filteredString.split("\n");
        Supermarket2 supermarket2 = new Supermarket2(linesArray, context);
        Receipt receipt = supermarket2.createReceipt();

        return receipt.getInfo();
    }

    private static ArrayList<String> recognize(Context context, Bitmap bitmap) {
        TextRecognizer detector = new TextRecognizer.Builder(context).build();
        if (detector.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            return LineSorter.toTextLines(detector.detect(frame));
        }else {
            Log.d(TAG, "Could not set up the detector!");
            return null;
        }
    }
}

package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextRecognizer;
import com.paktalin.receiptanalyzer.receipts.Receipt;

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
        SupermarketInfo info = new SupermarketInfo(context, linesArray);
        if(info.supermarket != null) {
            Receipt receipt = info.createReceipt();
            return receipt.getInfo();
        }
        return "Sorry, we couldn't identify the supermarket. Please, try to take picture again";
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

package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextRecognizer;
import com.paktalin.receiptanalyzer.receipts.Receipt;
import com.paktalin.receiptanalyzer.receipts.SelverReceipt;

import java.util.ArrayList;

/**
 * Created by Paktalin on 22-Mar-18.
 */

public class Analyzer implements Runnable {
    private static final String TAG = Analyzer.class.getSimpleName();
    private Context context;
    private Bitmap bitmap;
    private String filteredString;

    Analyzer(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public void run() {
        ArrayList<String> lines = recognize(context, bitmap);
        filteredString = StringFilter.filter(lines);
        String firstLine = StringManager.getFirstLine(filteredString);
        firstLine = StringManager.clean(firstLine);
        String store = StoreName.getStoreName(firstLine);
        if (store != null){
            Receipt receipt = createReceipt(store);
        }
        else
            Log.d(TAG, "Couldn't identify the supermarket");
    }

    private ArrayList<String> recognize(Context context, Bitmap bitmap) {
        TextRecognizer detector = new TextRecognizer.Builder(context).build();
        if (detector.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            ArrayList<String> lines = LineSorter.toTextLines(detector.detect(frame));
            return lines;
        }else {
            Log.d(TAG, "Could not set up the detector!");
            return null;
        }
    }

    private Receipt createReceipt(String store){
        Receipt receipt = null;
        switch (store){
            case "Selver":
                receipt = new SelverReceipt(filteredString);

                Log.d(TAG, receipt.getName());
                Log.d(TAG, receipt.getAdditionalName());
                Log.d(TAG, receipt.getAddress());
                break;
            case "Prisma":

        }
        return receipt;
    }
}

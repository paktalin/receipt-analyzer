package com.paktalin.receiptanalyzer;

import android.content.Context;
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
    private static String[] linesArray;

    static String analyze(Context context, Bitmap bitmap) {
        ArrayList<String> lines = recognize(context, bitmap);
        String filteredString = StringFilter.filter(lines);
        linesArray = filteredString.split("\n");
        String firstLine = linesArray[0].replace(" ", "");
        String store = StoreName.getStoreName(firstLine);
        if (store != null){
            Receipt receipt = createReceipt(store);
            return receipt.getInfo();
        }
        else {
            Log.d(TAG, "Couldn't identify the supermarket");
            return null;
        }
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

    private static Receipt createReceipt(String store){
        Receipt receipt = null;
        switch (store){
            case "Selver":
                receipt = new SelverReceipt(linesArray);
                break;
            case "Prisma":
                receipt = new PrismaReceipt(linesArray);
                break;
            case "Rimi":
                receipt = new RimiReceipt(linesArray);
                break;
            case "Konsum":
                receipt = new KonsumReceipt(linesArray);
                break;
            case "Maxima":
                receipt = new MaximaReceipt(linesArray);
                break;
        }
        return receipt;
    }
}

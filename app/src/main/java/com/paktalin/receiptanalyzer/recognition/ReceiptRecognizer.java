package com.paktalin.receiptanalyzer.recognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextRecognizer;
import com.paktalin.receiptanalyzer.SupermarketInfo;
import com.paktalin.receiptanalyzer.receipts_data.receipts.Receipt;

import java.util.ArrayList;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class ReceiptRecognizer {
    private static final String TAG = ReceiptRecognizer.class.getSimpleName();

    private static ArrayList<String> lines;
    private static Receipt receipt = null;

    public static Receipt extract(Context context, Bitmap bitmap) {
        lines = recognize(context, bitmap);
        receipt = ReceiptCreator.createReceipt(lines);
        if (receipt != null)
            receipt = SupermarketInfo.setInfo(receipt, context);
        return receipt;
    }



    private static ArrayList<String> recognize(Context context, Bitmap bitmap) {
        TextRecognizer detector = new TextRecognizer.Builder(context).build();
        if (detector.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder()
                    .setBitmap(bitmap)
                    .build();

            return LineSorter.toTextLines(detector.detect(frame));
        }else {
            Log.d(TAG, "Could not set up the detector!");
            return null;
        }
    }

    public static Receipt extract(Context context, String supermarket) {
        receipt = ReceiptCreator.createReceipt(lines, supermarket);
        if (receipt != null)
            receipt = SupermarketInfo.setInfo(receipt, context);
        return receipt;
    }
}

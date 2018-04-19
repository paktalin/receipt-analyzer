package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextRecognizer;
import com.paktalin.receiptanalyzer.receipts.Receipt;

import java.util.ArrayList;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class ReceiptExtractor {
    private static final String TAG = ReceiptExtractor.class.getSimpleName();

    public static Receipt extract(Context context, Bitmap bitmap) {
        if (bitmap != null) {
            ArrayList<String> lines = recognize(context, bitmap);
            Receipt receipt = ReceiptCreator.createReceipt(lines);
            receipt = SupermarketInfo.setInfo(receipt, context);
            return receipt;
        }
        return null;
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
}

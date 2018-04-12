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
import java.util.Arrays;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class ReceiptExtractor {
    private static final String TAG = ReceiptExtractor.class.getSimpleName();

    public static Receipt extract(Context context, Bitmap bitmap) {
        ArrayList<String> lines = recognize(context, bitmap);
        return createReceipt(lines);
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

    private static Receipt createReceipt(ArrayList<String> list) {
        String filteredString = StringFilter.filter(list);
        String[] linesArray = filteredString.split("\n");
        String firstLine = StringManager.clean(linesArray[0]);
        String supermarket = SupermarketName.getStoreName(firstLine);
        Log.d(TAG, "supermarket: " + Arrays.toString(linesArray));

        Receipt receipt = null;
        switch (supermarket) {
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
        if (receipt != null)
            receipt.setSupermarket(supermarket);
        return receipt;
    }
}

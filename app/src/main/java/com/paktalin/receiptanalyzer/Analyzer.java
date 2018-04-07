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
    private static String[] linesArray;
    static String supermarket;

    static String analyze(Context context, Bitmap bitmap) {
        ArrayList<String> lines = recognize(context, bitmap);
        String filteredString = StringFilter.filter(lines);
        linesArray = filteredString.split("\n");
        String firstLine = linesArray[0].replace(" ", "");
        Supermarket supermarket = new Supermarket(context, firstLine);
        supermarket.execute();
        /*supermarket = StoreName.getStoreName(firstLine);
        if (supermarket != null){
            Receipt receipt = createReceipt();
            return receipt.getInfo();
        }
        else {
            Log.d(TAG, "Couldn't identify the supermarket");
            return null;
        }*/
        return null;
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

    private static Receipt createReceipt(){
        Receipt receipt = null;
        switch (supermarket){
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

    /*static Cursor getDBTable(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        Cursor cursor = helper.query(supermarket, null);

        ArrayList<String>[] supermarketData = new ArrayList[3];

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                supermarketData[0].add(cursor.getString(1));

            } while (cursor.moveToNext());
        }
    }*/
}

package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

/**
 * Created by Paktalin on 12.03.2018.
 */

class Recognizer {
    private static final String TAG = Recognizer.class.getSimpleName();

    static void recognize(Context context) {
        Bitmap bitmap = FileManager.getBitmap();

        TextRecognizer detector = new TextRecognizer.Builder(context).build();

        if (detector.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            String string = LineOrganizer.getString(detector.detect(frame));
            string = StringFilter.filter(string);
            try {
                FileManager.saveTextFile(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileManager.saveTextFile("formatted.txt", string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Log.d(TAG, "Could not set up the detector!");
        }
    }
}
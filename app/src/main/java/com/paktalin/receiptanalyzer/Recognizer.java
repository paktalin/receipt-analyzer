package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
            SparseArray<TextBlock> textBlocks = detector.detect(frame);
            TextBlockSorter sorter = new TextBlockSorter(textBlocks);
        }else {
            Log.d(TAG, "Could not set up the detector!");
        }
    }
}
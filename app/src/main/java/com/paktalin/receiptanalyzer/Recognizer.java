package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

/**
 * Created by Paktalin on 12.03.2018.
 */

public class Recognizer {
    private static final String TAG = Recognizer.class.getSimpleName();

    static void recognize(Context context) {
        Bitmap bitmap = FileManager.getBitmap();

        TextRecognizer detector = new TextRecognizer.Builder(context).build();
        if (detector.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            Bitmap bitmapFrame = frame.getBitmap();
            FileManager.saveBitmap(bitmapFrame, "frame.jpg");

            SparseArray<TextBlock> textBlocks = null;
            if (frame != null) {
                textBlocks = detector.detect(frame);
            }
            String blocks = "";
            String lines = "";
            String words = "";
            if (textBlocks != null) {
                for (int index = 0; index < textBlocks.size(); index++) {
                    //extract scanned text blocks here
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks += tBlock.getValue() + "\n" + "\n";
                    for (Text line : tBlock.getComponents()) {
                        //extract scanned text lines here
                        lines += line.getValue() + "\n";
                        for (Text element : line.getComponents()) {
                            //extract scanned text words here
                            words += element.getValue() + ", ";
                        }
                    }
                }
            }
            try {
                FileManager.saveTextFile("blocks.txt", blocks);
                FileManager.saveTextFile("lines.txt", lines);
                FileManager.saveTextFile("words.txt", words);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Log.d(TAG, "Could not set up the detector!");
        }
    }
}
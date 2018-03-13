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
    private static int textBlocksNum;

    static void recognize(Context context) {
        Bitmap bitmap = FileManager.getBitmap();

        TextRecognizer detector = new TextRecognizer.Builder(context).build();

        if (detector.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlocks = detector.detect(frame);
            textBlocksNum = textBlocks.size();
            sortTextBlocks(textBlocks);
        }else {
            Log.d(TAG, "Could not set up the detector!");
        }
    }

    private static void sortTextBlocks(SparseArray<TextBlock> textBlocks) {
        TreeMap<Float, TextBlock> textBlockTreeMap = new TreeMap<>();
        TextBlock tBlock;
        Float top;
        Float bottom;
        String blocks = "";
        String lines = "";
        float[] topDiff = new float[textBlocksNum-1];
        float[] tops = new float[textBlocksNum];

        for (int index = 0; index < textBlocksNum; index++) {
            tBlock = textBlocks.valueAt(index);
            top = (new RectF(tBlock.getBoundingBox())).top;
            bottom = (new RectF(tBlock.getBoundingBox())).bottom;

            float height = bottom - top;
            Log.d(TAG, "height\n" + height + "\n");
            textBlockTreeMap.put(top, tBlock);
        }

        int i = 0;
        for(Map.Entry e : textBlockTreeMap.entrySet()){
            TextBlock textBlock = (TextBlock) e.getValue();
            blocks += textBlock.getValue() + "\n" + "\n";
            tops[i] = (float) e.getKey();
            i++;
            for (Text line : textBlock.getComponents()) {
                //extract scanned text lines here
                lines += line.getValue() + "\n";
            }
        }

        for(int j = 0; j < tops.length-1; j++) {
            topDiff[j] = tops[j+1] - tops[j];
        }
        Log.d(TAG, "topDiff: " + Arrays.toString(topDiff));

        try {
            FileManager.saveTextFile("blocks.txt", blocks);
            FileManager.saveTextFile("lines.txt", lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TreeMap<Float, TextBlock> getSortedTextBlocks(SparseArray<TextBlock> textBlocks) {
        TreeMap<Float, TextBlock> textBlockTreeMap = new TreeMap<>();
        TextBlock textBlock;
        Float top;

        for (int index = 0; index < textBlocksNum; index++) {
            textBlock = textBlocks.valueAt(index);
            RectF rect = new RectF(textBlock.getBoundingBox());
            top = rect.top;
            textBlockTreeMap.put(top, textBlock);
        }
        return textBlockTreeMap;
    }

    private void organizeLines(TreeMap<Float, TextBlock> treeMap) {
        float[] tops = new float[textBlocksNum];
        float[] topDiff = new float[textBlocksNum-1];
        Float textHeight = null;

        int i = 0;
        for (Map.Entry e : treeMap.entrySet()) {
            tops[i] = (float) e.getKey();
            i++;
            if (textHeight != null) {
                RectF rect = new RectF(((TextBlock)e.getValue()).getBoundingBox());
                float height = rect.bottom - (float)e.getKey();
                if(height < textHeight) {
                    textHeight = height;
                }
            }
        }
        for(int j = 0; j < tops.length-1; j++) {
            topDiff[j] = tops[j+1] - tops[j];
        }
        String blocks = "";
        String lines = "";

        for(Map.Entry e : treeMap.entrySet()){
            TextBlock textBlock = (TextBlock) e.getValue();
            blocks += textBlock.getValue() + "\n" + "\n";
            tops[i] = (float) e.getKey();
            i++;
            for (Text line : textBlock.getComponents()) {
                //extract scanned text lines here
                lines += line.getValue() + "\n";
            }
        }

    }


}
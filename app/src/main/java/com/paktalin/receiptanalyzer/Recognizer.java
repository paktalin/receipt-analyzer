package com.paktalin.receiptanalyzer;

import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

/**
 * Created by Paktalin on 12.03.2018.
 */

public class Recognizer {

    static void recognize(Bitmap bitmap, TextRecognizer detector) {
        DirectoryManager.saveBitmap(bitmap);

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = detector.detect(frame);
        String blocks = "";
        String lines = "";
        String words = "";
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
        DirectoryManager.saveTextFile("blocks.txt", blocks);
        DirectoryManager.saveTextFile("lines.txt", lines);
        DirectoryManager.saveTextFile("words.txt", words);

    }
}

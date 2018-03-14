package com.paktalin.receiptanalyzer;

import android.graphics.RectF;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;

/**
 * Created by Paktalin on 14.03.2018.
 */

public class MyTextBlock {
    int top, bottom, left , right, height;
    ArrayList<String> lines;

    MyTextBlock(TextBlock textBlock) {
        RectF rect = new RectF(textBlock.getBoundingBox());
        top = (int)rect.top;
        bottom = (int)rect.bottom;
        left = (int)rect.left;
        right = (int)rect.right;
        height = bottom - top;
        for (Text line : textBlock.getComponents()) {
            lines.add(line.getValue());
        }
    }

}

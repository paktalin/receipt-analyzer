package com.paktalin.receiptanalyzer;

import android.graphics.RectF;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;

/**
 * Created by Paktalin on 14.03.2018.
 */

class MyTextBlock {
    private int top, left, height;
    private ArrayList<String> lines;

    MyTextBlock(TextBlock textBlock) {
        RectF rect = new RectF(textBlock.getBoundingBox());
        lines = new ArrayList<>();
        top = (int)rect.top;
        left = (int)rect.left;
        height = (int) rect.bottom - top;
        for (Text line : textBlock.getComponents()) {
            lines.add(line.getValue());
        }
    }

    int getTop() {
        return top;
    }

    int getLeft() {
        return left;
    }

    int getHeight() {
        return height;
    }

    ArrayList<String> getLines() {
        return lines;
    }
}

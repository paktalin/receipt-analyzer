package com.paktalin.receiptanalyzer;

import android.graphics.RectF;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;

/**
 * Created by Paktalin on 14.03.2018.
 */

public class MyTextBlock {
    private int top;
    private int left;
    private int height;
    private ArrayList<String> lines;

    MyTextBlock(TextBlock textBlock) {
        RectF rect = new RectF(textBlock.getBoundingBox());
        top = (int)rect.top;
        int bottom = (int) rect.bottom;
        left = (int)rect.left;
        height = bottom - top;
        for (Text line : textBlock.getComponents()) {
            lines.add(line.getValue());
        }
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<String> getLines() {
        return lines;
    }
}

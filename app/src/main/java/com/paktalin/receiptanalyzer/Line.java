package com.paktalin.receiptanalyzer;

import android.graphics.RectF;
import android.util.Log;

import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

/**
 * Created by Paktalin on 15.03.2018.
 */

class Line {
    private static final String TAG = Line.class.getSimpleName();
    private int top, left, height;
    private String filling;

    Line (Text text) {
        filling = text.getValue();

        RectF rect = new RectF(text.getBoundingBox());
        top = (int)rect.top;
        left = (int)rect.left;
        height = (int)rect.height();
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

    String getFilling() {
        return filling;
    }

    static int getMeanTop(ArrayList<Line> lines) {
        if (lines.size() == 1) {
            return lines.get(0).top;
        } else {
            int sum = 0;
            for (Line line : lines) {
                sum += line.top;
            }
            return sum/lines.size();
        }
    }

    static String join(ArrayList<Line> lines) {
        if (lines.size() == 1){
            return lines.get(0).filling;
        } else{
            String string = "";
            while (lines.size() > 0) {
                int leftIndex = 0;
                int minLeft = lines.get(0).left;
                for (int i = 1; i < lines.size(); i++) {
                    int left = lines.get(i).left;
                    if(left < minLeft) {
                        minLeft = left;
                        leftIndex = i;
                    }
                }
                Line leftLine = lines.get(leftIndex);
                string += leftLine.filling + " ";
                lines.remove(leftLine);
            }
            return string;
        }
    }
}

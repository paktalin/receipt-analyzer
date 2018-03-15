package com.paktalin.receiptanalyzer;

import android.graphics.RectF;
import com.google.android.gms.vision.text.Text;

/**
 * Created by Paktalin on 15.03.2018.
 */

public class Line {
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

    void setFilling(String filling) {
        this.filling = filling;
    }

    void setTop(int top) {
        this.top = top;
    }

    void setLeft(int left) {
        this.left = left;
    }

    void setHeight(int height) {
        this.height = height;
    }
}

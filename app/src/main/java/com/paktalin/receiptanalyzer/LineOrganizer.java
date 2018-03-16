package com.paktalin.receiptanalyzer;

import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Paktalin on 14.03.2018.
 */

class LineOrganizer {
    private static final String TAG = LineOrganizer.class.getSimpleName();
    private static ArrayList<Line> lines;
    private static int height;

    static String getString(SparseArray<TextBlock> textBlocks) {
        lines = extractData(textBlocks);
        lines = sortLines();
        StringBuilder result = new StringBuilder();
        for(String stringLine: glueLines()) {
            result.append(stringLine).append("\n");
        }
        return result.toString();
    }

    static private ArrayList<Line> extractData(SparseArray<TextBlock> textBlocks) {
        TextBlock textBlock;
        ArrayList<Line> lines = new ArrayList<>();
        int size = textBlocks.size();
        for (int i = 0; i < size; i++) {
            textBlock = textBlocks.valueAt(i);
            for (Text line : textBlock.getComponents()) {
                lines.add(new Line(line));
            }
        }
        return lines;
    }

    static private ArrayList<Line> sortLines() {
        TreeMap<Float, Line> treeMap = new TreeMap<>();
        int sumHeights = 0;
        for(Line line : lines) {
            sumHeights += line.getHeight();
            treeMap.put(createKey(line.getTop(), line.getLeft()), line);
        }
        height = sumHeights/lines.size();
        lines.clear();
        for(Map.Entry entry : treeMap.entrySet()) {
            lines.add((Line)entry.getValue());
        }
        return lines;
    }

    static private ArrayList<String> glueLines() {
        ArrayList<Line> toOneLine = new ArrayList<>();
        ArrayList<String> stringLines = new ArrayList<>();
        toOneLine.add(lines.get(0));

        for(int i = 1; i < lines.size(); i++) {
            Line line = lines.get(i);
            if (close(Line.getMeanTop(toOneLine), line.getTop())) {
                toOneLine.add(line);
            } else{
                stringLines.add(Line.join(toOneLine));
                toOneLine.clear();
                toOneLine.add(line);
            }
        }
        return stringLines;
    }

    //generates a unique key gluing top and left coordinates
    static private Float createKey(int top, int left) {
        return Float.parseFloat(top + "." + left);
    }

    private static boolean close(int top1, int top2) {
        return (top2 - top1) < height/1.6;
    }

    static int[][] getCropCoordinates(SparseArray<TextBlock> textBlocks) {
        Rect rect = textBlocks.valueAt(0).getBoundingBox();
        int[] leftTop = {rect.left, rect.top};
        int[] rightTop = {rect.right, rect.top};
        int[] leftBottom = {rect.left, rect.bottom};
        int[] rightBottom = {rect.right, rect.bottom};

        for (int i = 1; i < textBlocks.size(); i++) {
            rect = textBlocks.valueAt(i).getBoundingBox();
            if ((rect.left < leftTop[0]) && (rect.top < leftTop[1])) {
                leftTop[0] = rect.left;
                leftTop[1] = rect.top;
            }
            if ((rect.right < rightTop[0]) && (rect.top < rightTop[1])) {
                rightTop[0] = rect.right;
                rightTop[1] = rect.top;
            }
            if ((rect.left < leftBottom[0]) && (rect.bottom < leftBottom[1])) {
                leftBottom[0] = rect.left;
                leftBottom[1] = rect.bottom;
            }
            if ((rect.right < rightBottom[0]) && (rect.bottom < rightBottom[1])) {
                rightBottom[0] = rect.right;
                rightBottom[1] = rect.bottom;
            }
        }
        return new int[][]{leftTop, rightTop, leftBottom, rightBottom};
    }
}
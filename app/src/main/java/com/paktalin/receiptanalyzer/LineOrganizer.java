package com.paktalin.receiptanalyzer;

import android.util.SparseArray;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Paktalin on 14.03.2018.
 * First step after recognition.
 * Here we organize the scanned data in lines like they were initially located in the receipt
 */

class LineOrganizer {
    private static final String TAG = LineOrganizer.class.getSimpleName();
    private static ArrayList<Line> lines;
    private static int height;

    static ArrayList<String> toTextLines(SparseArray<TextBlock> textBlocks) {
        lines = extractData(textBlocks);
        lines = sortLines();
        return gluedLines();
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

    static private ArrayList<String> gluedLines() {
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
}
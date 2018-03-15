package com.paktalin.receiptanalyzer;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Paktalin on 14.03.2018.
 */

class TextBlockSorter {
    private static ArrayList<Line> lines;

    static ArrayList<String> getStrings(SparseArray<TextBlock> textBlocks) {
        lines = extractData(textBlocks);
        lines = sortLines();
        return glue();
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
        TreeMap<Integer, Line> treeMap = new TreeMap<>();
        for(Line line : lines) {
            treeMap.put(line.getTop(), line);
        }
        lines.clear();
        for(Map.Entry entry : treeMap.entrySet()) {
            lines.add((Line)entry.getValue());
        }
        return lines;
    }

    static private int getMeanHeight() {
        int sum = 0;
        int number = lines.size();
        for(Line line : lines) {
            sum += line.getHeight();
        }
        return sum/number;
    }

    static private ArrayList<String> glue() {
        int height = getMeanHeight();
        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0; i < lines.size()-1; i++) {
            Line line1 = lines.get(i);
            Line line2 = lines.get(i+1);
            if ((line2.getTop() - line1.getTop()) < height/1.5) {
                if(line2.getLeft() > line1.getLeft()) {
                    line1.setFilling(line1.getFilling() + " " + line2.getFilling());
                } else {
                    line1.setFilling(line2.getFilling() + " " + line1.getFilling());
                    line1.setLeft(line2.getLeft());
                }
                line1.setHeight((line1.getHeight() + line2.getHeight())/2);
                line1.setTop((line1.getTop() + line2.getTop())/2);
                lines.set(i, line1);
                lines.remove(line2);
                i--;
            }
        }
        for (Line line : lines) {
            strings.add(line.getFilling());
        }
        return strings;
    }
}
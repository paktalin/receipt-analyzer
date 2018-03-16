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

class LineOrganizer {
    private static final String TAG = LineOrganizer.class.getSimpleName();
    private static ArrayList<Line> lines;
    private static int height;


    static String getString(SparseArray<TextBlock> textBlocks) {
        lines = extractData(textBlocks);
        lines = sortLines();
        for(Line line : lines){
            Log.d(TAG, line.getFilling());
        }
        StringBuilder result = new StringBuilder();
        glueLines();
        for(Line line: lines) {
            result.append(line.getFilling()).append("\n");
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
        for(Line line : lines) {
            treeMap.put(createKey(line.getTop(), line.getLeft()), line);
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

    static private void glueLines() {
        height = getMeanHeight();
        ArrayList<String> toOneLine = new ArrayList<>();
        for(int i = 0; i < lines.size()-1; i++) {
            Line line1 = lines.get(i);
            Line line2 = lines.get(i+1);

            if (close(line1.getTop(), line2.getTop())) {
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
            } else{
                //join(toOneLine);
                toOneLine.clear();
            }
        }
    }

    //generates a unique key gluing top and left coordinates
    static private Float createKey(int top, int left) {
        return Float.parseFloat(top + "." + left);
    }

    private static boolean close(int top1, int top2) {
        return (top2 - top1) < height/1.6;
    }

    /*private static ArrayList<String> fromLeftToRight() {

    }*/
}
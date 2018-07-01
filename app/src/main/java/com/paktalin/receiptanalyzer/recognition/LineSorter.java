package com.paktalin.receiptanalyzer.recognition;

import android.util.SparseArray;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Paktalin on 14.03.2018.
 * First step after recognition.
 * Here we organize the scanned data in lines like they were initially located in the receipt
 */

class LineSorter {
    private static final String TAG = LineSorter.class.getSimpleName();
    private static ArrayList<Line> lines;
    private static int height;

    static ArrayList<String> toTextLines(SparseArray<TextBlock> textBlocks) {
        lines = toLineArrayList(textBlocks);
        lines = sortLines();
        if (lines != null)
            return glueLines();
        else return null;
    }

    static private ArrayList<Line> toLineArrayList(SparseArray<TextBlock> textBlocks) {
        ArrayList<Line> lines = new ArrayList<>();
        int size = textBlocks.size();
        for (int i = 0; i < size; i++) {
            TextBlock textBlock = textBlocks.valueAt(i);
            for (Text line : textBlock.getComponents()) {
                lines.add(new Line(line));
            }
        }
        return lines;
    }

    /*static private ArrayList<Line> toLineArrayList(List<FirebaseVisionCloudText.Block> blocks) {
        ArrayList<Line> lines = new ArrayList<>();
        for (FirebaseVisionCloudText.Block block : blocks) {
            for (Text line : block.getComponents()) {
                lines.add(new Line(line));
            }
        }
        return lines;
    }*/

    static private ArrayList<Line> sortLines() {
        TreeMap<Float, Line> treeMap = new TreeMap<>();
        int sumHeights = 0;
        for(Line line : lines) {
            sumHeights += line.getHeight();
            treeMap.put(createKey(line.getTop(), line.getLeft()), line);
        }
        if (lines.size() != 0) {
            height = sumHeights/lines.size();
            lines.clear();
            for(Map.Entry entry : treeMap.entrySet()) {
                lines.add((Line)entry.getValue());
            }
            return lines;
        } else return null;
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
}
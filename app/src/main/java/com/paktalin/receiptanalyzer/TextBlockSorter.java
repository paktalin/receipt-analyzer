package com.paktalin.receiptanalyzer;

import android.graphics.RectF;
import android.util.SparseArray;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Created by Paktalin on 14.03.2018.
 */

public class TextBlockSorter {
    private int size;
    private TreeMap<int[], TextBlock> textBlockTree;
    int textHeight;

    public TextBlockSorter(SparseArray<TextBlock> textBlocks) {
        textBlockTree = toTreeMap(textBlocks);
        size = textBlocks.size();
    }

    private TreeMap<int[], TextBlock> toTreeMap(SparseArray<TextBlock> textBlocks) {
        TreeMap<int[], TextBlock> textBlockTree = new TreeMap<>();
        TextBlock textBlock;
        int[] borders = new int[4];
        for (int i = 0; i < size; i++) {
            textBlock = textBlocks.valueAt(i);
            RectF rect = new RectF(textBlock.getBoundingBox());
            borders[0] = (int)rect.top;
            borders[1] = (int)rect.bottom;
            borders[2] = (int)rect.left;
            borders[3] = (int)rect.right;

            textBlockTree.put(borders, textBlock);
        }
        return textBlockTree;
    }

    private int getMinHeight(int height, int minHeight) {
        if((minHeight != -1) && (minHeight < height)) {
            return minHeight;
        } else {
            return height;
        }
    }

    private TreeMap<int[], ArrayList<Text>> getLines() {
        int minHeight = -1;
        TreeMap<int[], ArrayList<Text>> lines = new TreeMap<>();
        for(Map.Entry entry : textBlockTree.entrySet()) {
            int[] key = (int[])entry.getKey();
            int top = key[0];
            minHeight = getMinHeight(key[1]-top, minHeight);
            ArrayList<Text> textList = (ArrayList<Text>) ((TextBlock)entry.getValue()).getComponents();
            int[] borders = {top, key[2], key[3]};
            lines.put(borders, textList);
        }
        textHeight = minHeight;
        return lines;
    }

    private boolean toClue(int top1, int top2) {
        int distance = top2 - top1;
        if (distance < textHeight / 2) {
            return true;
        } else {
            return false;
        }
    }

    private void clueLines() {
        TreeMap<int[], ArrayList<Text>> lines = getLines();
        Stack<String> linesStack = new Stack<>();
        for(int i = 0; i < size - 1; i++) {
            if(toClue(tops[i+1], tops[i])) {
                String line1 = linesStack.pop();
                String line2 = lines[i+1];
                linesStack.push(line1 + " " + line2)
            }
        }
    }
}

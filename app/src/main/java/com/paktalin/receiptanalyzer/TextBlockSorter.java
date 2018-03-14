package com.paktalin.receiptanalyzer;

import android.util.SparseArray;

import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;

/**
 * Created by Paktalin on 14.03.2018.
 */

public class TextBlockSorter {
    private int size;
    private MyTextBlock[] myTextBlocks;

    public TextBlockSorter(SparseArray<TextBlock> textBlocks) {
        myTextBlocks = extractData(textBlocks);
        size = textBlocks.size();
    }

    private MyTextBlock[] extractData(SparseArray<TextBlock> textBlocks) {
        MyTextBlock[] myTextBlocks = new MyTextBlock[size];
        for (int i = 0; i < size; i++) {
            myTextBlocks[i] = new MyTextBlock(textBlocks.valueAt(i));
        }
        return myTextBlocks;
    }

    private int getMinHeight() {
        int minHeight = -1;
        if(size != 0) {
            minHeight = myTextBlocks[0].getHeight();
        }
        for (int i = 1; i < size; i++) {
            int height = myTextBlocks[i].getHeight();
            if(height < minHeight) {
                minHeight = height;
            }
        }
        return minHeight;
    }

    private void glueLines() {
        int height = getMinHeight();
        for(int i = 0; i < size-1; i++) {
            MyTextBlock block1 = myTextBlocks[i];
            MyTextBlock block2 = myTextBlocks[i+1];

            if((block2.getTop() - block1.getTop()) < height/2) {
                if(block2.getLeft() > block1.getLeft()) {
                    glue(block1.getLines(), block2.getLines());
                } else {
                    glue(block2.getLines(), block1.getLines());
                }
            }
        }
    }

    private void glue(ArrayList<String> leftList, ArrayList<String> rightList) {
        /*TODO count number of lines in each block
        TODO consider gluing lines at different heights*/
    }
}
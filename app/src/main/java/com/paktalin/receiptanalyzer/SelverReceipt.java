package com.paktalin.receiptanalyzer;

import android.util.Log;

import static com.paktalin.receiptanalyzer.StringManager.similar;

/**
 * Created by Paktalin on 21-Mar-18.
 */

class SelverReceipt extends Receipt {
    private static final String TAG = SelverReceipt.class.getSimpleName();
    private String input;
    private int selverIndex;
    private int startLine;

    SelverReceipt(String input) {
        name = "Selver";
        this.input = input;
        setAdditionalName();
        address = StringArrays.getSelverAddress(selverIndex);
        removeUnneccessary();
        Log.d(TAG, startLine + "");
    }

    private void setAdditionalName() {
        String string = StringManager.getFirstLine(input);
        string = StringManager.clean(string);

        String[] array = StringArrays.getSelverNames();
        for (int i = 0; i < array.length; i++) {
            String anArray = array[i];
            if (StringManager.similarity(string, anArray) < 0.05){
                additionalName = anArray;
                selverIndex = i;
                break;
            }
        }
    }

    private void removeUnneccessary() {
        String[] lines = input.split("\n");
        if (similar(lines[7], "nimetuskogushindsumma"))
            startLine = 8;
    }
}

package com.paktalin.receiptanalyzer;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class SelverReceipt extends Receipt {
    private static final String TAG = SelverReceipt.class.getSimpleName();
    private int selverIndex;

    SelverReceipt(String input) {
        name = "Selver";
        setAdditionalName(input);
        address = StringArrays.getSelverAddress(selverIndex);
    }

    private void setAdditionalName(String input) {
        String[] array = StringArrays.getSelverNames();
        for (int i = 0; i < array.length; i++) {
            String anArray = array[i];
            if (StringManager.similarity(input, anArray) < 0.05){
                additionalName = anArray;
                selverIndex = i;
                break;
            }
        }
    }

}

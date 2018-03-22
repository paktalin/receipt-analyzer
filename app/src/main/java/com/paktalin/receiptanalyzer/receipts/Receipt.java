package com.paktalin.receiptanalyzer.receipts;

import com.paktalin.receiptanalyzer.StringArrays;
import com.paktalin.receiptanalyzer.StringManager;

import java.util.Objects;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class Receipt {
    String input;
    String name = null;
    String additionalName = null;
    String address = null;
    int startLine;
    int supermarketIndex;

    public String getName() {
        return name;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public String getAddress() {
        return address;
    }

    void setAdditionalName(String[] array) {
        String string = StringManager.getFirstLine(input);
        string = StringManager.clean(string);

        for (int i = 0; i < array.length; i++) {
            String anArray = array[i];
            if (StringManager.similarity(string, anArray) < 0.05){
                if(name.equals("Selver"))
                    additionalName = StringArrays.getSelverAdditionalName(i);
                else
                    additionalName = anArray;
                supermarketIndex = i;
                break;
            }
        }
    }
}

package com.paktalin.receiptanalyzer.receipts;

import com.paktalin.receiptanalyzer.StringArrays;
import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class Receipt {
    String name = null;
    private String additionalName = null;
    String address = null;
    int startLine;
    int supermarketIndex;
    String[] lines;
    final String
            SELVER = "Selver",
            PRISMA = "Prisma",
            MAXIMA = "Maxima",
            RIMI = "Rimi",
            KONSUM = "Konsum";


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
        String string = lines[0];
        string = StringManager.clean(string);

        for (int i = 0; i < array.length; i++) {
            String anArray = array[i];
            if (StringManager.similarity(string, anArray) < 0.05){
                if(name.equals(SELVER))
                    additionalName = StringArrays.getSelverAdditionalName(i);
                else if(name.equals(PRISMA))
                    additionalName = StringArrays.getPrismaAdditionalName(i);
                supermarketIndex = i;
                break;
            }
        }
    }

    void setLines(String input) {
        lines = input.split("\n");
    }
}

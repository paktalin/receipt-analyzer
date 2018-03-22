package com.paktalin.receiptanalyzer.receipts;

import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class Receipt {
    String name = null;
    String additionalName = null;
    String address = null;
    int startLine;
    int supermarketIndex;
    private String[] lines;
    final String
            SELVER = "Selver",
            PRISMA = "Prisma",
            MAXIMA = "Maxima",
            RIMI = "Rimi",
            KONSUM = "Konsum";


    Receipt(String[] lines) {
        this.lines = lines;
    }

    public String getName() {
        return name;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public String getAddress() {
        return address;
    }

    void setSupermarketIndex(String[] array) {
        String string = lines[0];
        string = StringManager.clean(string);
        for (int i = 0; i < array.length; i++) {
            String anArray = array[i];
            if (StringManager.similarity(string, anArray) < 0.05)
                supermarketIndex = i;
        }

    }

    /**
     * @param startString the starting string of this particular supermarket
     * @param number the number of line where the line is supposed to be
     */
    int startLine(String startString, int number) {
        if (StringManager.similar(lines[number - 1], startString))
            return number;
        return -1;
    }
}

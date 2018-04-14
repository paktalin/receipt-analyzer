package com.paktalin.receiptanalyzer.receipts;

import com.paktalin.receiptanalyzer.StringManager;

/**
 * Created by Paktalin on 21-Mar-18.
 */

public class PrismaReceipt extends Receipt {
    private static final String TAG = PrismaReceipt.class.getSimpleName();

    public PrismaReceipt(String[] lines){
        super(lines);
        purchasesStart = startLine("arvekviitung", 2) + 1;
        purchasesEnd = endLine("kokku", true);
        calculateFinalPrice();
    }

    @Override
    public String cutRetailersLine(String line) {
        return line.substring(18, line.length() - 6);
    }

    @Override
    void calculateFinalPrice() {
        String flag = "kokku";
        for (String line : lines) {
            try {
                String cut = line.substring(0, flag.length());
                if (StringManager.similar(cut, flag)) {
                    finalPrice = StringManager.extractFloat(line, flag.length());
                    break;
                }
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
    }
}

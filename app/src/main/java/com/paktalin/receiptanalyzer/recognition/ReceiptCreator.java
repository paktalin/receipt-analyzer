package com.paktalin.receiptanalyzer.recognition;

import com.paktalin.receiptanalyzer.StringFilter;
import com.paktalin.receiptanalyzer.StringManager;
import com.paktalin.receiptanalyzer.receipts_data.receipts.*;

import java.util.ArrayList;

import static com.paktalin.receiptanalyzer.StringManager.identical;
import static com.paktalin.receiptanalyzer.StringManager.similar;
import static com.paktalin.receiptanalyzer.Supermarkets.*;

/**
 * Created by Paktalin on 21-Mar-18.
 */

class ReceiptCreator {
    private static final String TAG = ReceiptCreator.class.getSimpleName();
    static private String name = null;
    private static String input;

    private final static String rimiString = "rimieestifoodasregnr10263574";
    private final static String maximaString = "maximaeestiouregnr10765896";
    private final static String konsumString = "harjutarbijateuhistu";

    static Receipt createReceipt(ArrayList<String> list) {

        Object[] object = StringFilter.filter(list);

        String[] filteredStrings = (String[]) object[0];
        ArrayList<String> initialLines = (ArrayList<String>) object[1];
        String firstLine = StringManager.clean(filteredStrings[0]);
        String supermarket = getStoreName(firstLine);

        Receipt receipt;
        switch (supermarket) {
            case SELVER:
                receipt = new SelverReceipt(filteredStrings);
                break;
            case PRISMA:
                receipt = new PrismaReceipt(filteredStrings);
                break;
            case RIMI:
                receipt = new RimiReceipt(filteredStrings);
                break;
            case KONSUM:
                receipt = new KonsumReceipt(filteredStrings);
                break;
            case MAXIMA:
                receipt = new MaximaReceipt(filteredStrings);
                break;
            default:
                receipt = null;
        }
        if (receipt != null) {
            receipt.setSupermarket(supermarket);
            receipt.setInitialLines(initialLines);
        }
        return receipt;
    }

    /**
     * The method is based on the length of the input string.
     * The length of supermarkets' first line vary like this:
     *
     * Maxima   26
     * Rimi     28
     * Konsum   20
     * Prisma   29-35
     * Selver   10-20
     *
     * Let the recognition error be 15%,
     * so the length of supermarkets' fist line should be in such limits:
     *
     * Maxima   22-30
     * Rimi     24-32
     * Konsum   17-23
     * Prisma   24-40
     * Selver   8-23
     */
    private static String getStoreName(String input) {
        ReceiptCreator.input = input;
        int length = input.length();

        if (length > 23) {
            if(!checkFor(rimiString, "Rimi"))
                if(!checkFor(maximaString, "Maxima"))
                    checkForPrisma();
        } else if (length > 21) {
            if(!checkFor(maximaString, "Maxima"))
                if(!checkFor(konsumString, "Konsum"))
                    checkForSelver();
        } else if(length > 16) {
            if(!checkFor(konsumString, "Konsum"))
                checkForSelver();
        } else if(length > 7)
            checkForSelver();
        return name;
    }

    private static boolean checkFor(String supermarketString, String name) {
        if (identical(input, supermarketString)) {
            ReceiptCreator.name = name;
            return true;
        } else
            return false;
    }
    private static void checkForPrisma() {
        String prismaString = "prismaperemarketas";
        String inputCut = input.substring(0, 18);
        if (identical(inputCut, prismaString))
            name = "Prisma";
    }
    private static void checkForSelver() {
        String selverString = "selver";
        int length = input.length();
        String inputCut = input.substring(length - 6, length);
        if (similar(inputCut, selverString))
            name = "Selver";
    }
}
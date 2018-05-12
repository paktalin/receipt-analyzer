package com.paktalin.receiptanalyzer.recognition;

import android.util.Log;

import com.paktalin.receiptanalyzer.StringFilter;
import com.paktalin.receiptanalyzer.managers.StringManager;
import com.paktalin.receiptanalyzer.receipts_data.receipts.*;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import static com.paktalin.receiptanalyzer.managers.StringManager.MAKE_EQUAL;
import static com.paktalin.receiptanalyzer.managers.StringManager.NO_CUT;
import static com.paktalin.receiptanalyzer.managers.StringManager.identical;
import static com.paktalin.receiptanalyzer.managers.StringManager.similar;
import static com.paktalin.receiptanalyzer.Supermarkets.*;

/**
 * Created by Paktalin on 21-Mar-18.
 */

class ReceiptCreator {
    private static final String TAG = ReceiptCreator.class.getSimpleName();
    static private String supermarket = null;
    private static String input;

    private final static String rimiString = "rimieestifoodasregnr10263574";
    private final static String maximaString = "maximaeestiouregnr10765896";
    private final static String konsumString = "harjutarbijateuhistu";

    private static String[] filteredLines;
    private static ArrayList<String> initialLines, list;
    private static Receipt receipt = null;

    static Receipt createReceipt(ArrayList<String> list) {
        ReceiptCreator.list = list;
        setFilteredAndInitialLines();
        String firstLine = StringManager.clean(filteredLines[0]);
        setStoreName(firstLine);
        initializeReceiptWithSupermarket();
        if (receipt != null) {
            receipt.setSupermarket(supermarket);
            receipt.setInitialLines(initialLines);
        }
        return receipt;
    }

    private static void setFilteredAndInitialLines() {
        Object[] object = StringFilter.filter(list);
        if (object != null) {
            filteredLines = (String[]) object[0];
            initialLines = (ArrayList<String>) object[1];
        }
    }

    private static void initializeReceiptWithSupermarket() {
        if (supermarket != null) {
            switch (supermarket) {
                case SELVER:
                    receipt = new SelverReceipt(filteredLines);
                    break;
                case PRISMA:
                    receipt = new PrismaReceipt(filteredLines);
                    break;
                case RIMI:
                    receipt = new RimiReceipt(filteredLines);
                    break;
                case KONSUM:
                    receipt = new KonsumReceipt(filteredLines);
                    break;
                case MAXIMA:
                    receipt = new MaximaReceipt(filteredLines);
                    break;
                default:
                    receipt = null;
            }
        }
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
    private static void setStoreName(String input) {
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
    }

    private static boolean checkFor(String string, String supermarket) {
        if (identical(input, string, StringManager.MAKE_EQUAL)) {
            ReceiptCreator.supermarket = supermarket;
            return true;
        }
        return false;
    }
    private static void checkForPrisma() {
        String prismaString = "prismaperemarketas";
        String inputCut = input.substring(0, 18);
        if (identical(inputCut, prismaString, MAKE_EQUAL))
            supermarket = "Prisma";
    }
    private static void checkForSelver() {
        String selverString = "selver";
        int length = input.length();
        String inputCut = input.substring(length - 6, length);
        if (similar(inputCut, selverString, NO_CUT))
            supermarket = "Selver";
    }

    static Receipt createReceipt(ArrayList<String> list, String supermarket) {
        ReceiptCreator.list = list;
        ReceiptCreator.supermarket = supermarket;
        setFilteredAndInitialLines();
        initializeReceiptWithSupermarket();
        receipt.setSupermarket(supermarket);
        receipt.setInitialLines(initialLines);
        Log.d(TAG, String.valueOf(initialLines));
        return receipt;
    }
}
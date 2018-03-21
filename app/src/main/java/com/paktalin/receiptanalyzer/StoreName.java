package com.paktalin.receiptanalyzer;

/**
 * Created by Paktalin on 21-Mar-18.
 */

class StoreName {
    private static final String TAG = StoreName.class.getSimpleName();
    private static final double VERY_SIMILAR = 0.08;
    static private String name = null;
    private static String input;

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
     * Let the recognition error to be 15%,
     * so the length of supermarkets' fist line should be in such limits:
     *
     * Maxima   22-30
     * Rimi     24-32
     * Konsum   17-23
     * Prisma   24-40
     * Selver   8-23
     */
    static String getStoreName(String input) {
        StoreName.input = input;
        int length = input.length();

        if (length > 23) {
            checkFor("rimieestifoodasregnr10263574", "Rimi",
                    "maximaeestiouregnr10765896", "Maxima");
            if (name == null)
                checkForPrisma();
        } else if(length > 21) {
            checkFor("maximaeestiouregnr10765896", "Maxima",
                    "harjutarbijateuhistu", "Konsum");
            if(name == null)
                checkForSelver();
        } else if(length > 16) {
            checkFor("harjutarbijateuhistu", "Konsum");
            if (name == null)
                checkForSelver();
        } else if(length > 7)
            checkForSelver();
        return name;
    }

    private static void checkFor(String string, String name) {
        if (StringManager.similarity(input, string) < VERY_SIMILAR)
            StoreName.name = name;
    }

    private static void checkFor(String string1, String name1, String string2, String name2) {
        checkFor(string1, name1);
        if (name == null)
            checkFor(string2, name2);
    }

    private static void checkForPrisma() {
        String prismaFirstLine = "prismaperemarketas";
        String inputCut = input.substring(0, 18);
        if (StringManager.similarity(inputCut, prismaFirstLine) < VERY_SIMILAR)
            name = "Prisma";
    }

    private static void checkForSelver() {
        String selverFirstLine = "selver";
        int length = input.length();
        String inputCut = input.substring(length - 6, length);
        if (StringManager.similarity(inputCut, selverFirstLine) < VERY_SIMILAR)
            name = "Selver";
    }
}
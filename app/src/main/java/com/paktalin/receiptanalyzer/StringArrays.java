package com.paktalin.receiptanalyzer;

/**
 * Created by Paktalin on 20-Mar-18.
 */

class StringArrays {

    static String[] getFirstGroupFirstLines() {
        return new String[]{
                "maximaeestiouregnr10765896",
                "rimieestifoodasregnr10263574",
                "harjutarbijateuhistu"
        };
    }

    static String getFirstGroupNameByIndex(int index) {
        String[] array = {
                "Maxima",
                "Rimi",
                "Konsum"
        };
        return array[index];
    }

    static String[] getSecondGroupFirstLines() {
        return new String[]{
                "prismaperemarketas",
                "selver"};
    }

    static String getSecondGroupNameByIndex(int index) {
        String[] array = {
                "Prisma",
                "Selver"
        };
        return array[index];
    }
}

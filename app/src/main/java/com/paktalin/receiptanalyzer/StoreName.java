package com.paktalin.receiptanalyzer;


/**
 * Created by Paktalin on 10.03.2018.
 */

class StoreName {
    private static final String TAG = StoreName.class.getSimpleName();
    private static String[] supermarkets = {"maxima", "rimi", "selver"};

    static String getSupermarket(String string) {
        for (String supermarket : supermarkets)
            if (string.contains(supermarket))
                return supermarket;
        String code = StringFilter.getRegCode(string);
        return getNameByCode(code);
    }

    private static String getNameByCode(String code) {
        String[][] codes = {
                {"10765896", "10263574", "10379733"},
                supermarkets};
        for(int i = 0; i < codes[0].length; i ++) {
            String storeCode = codes[0][i];
            if (code.equals(storeCode))
                return codes[1][i];
        }
        return "Couldn't find the name registered";
    }
}

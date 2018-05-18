package com.paktalin.receiptanalyzer.receipts_data.receipts;


/**
 * Created by Paktalin on 22-Mar-18.
 */

public class MaximaReceipt extends Receipt {
    private static final String TAG = MaximaReceipt.class.getSimpleName();

    public MaximaReceipt(String[] lines) {
        super(lines);
        purchasesStart = 5;
        purchasesEnd = endLine(new String[] {"ilmakmta", "kmtakmkmga"}, false);

        setPrice(new String[]{"makstudpangakaart", "kokkumaksta", "summa"});
    }
}

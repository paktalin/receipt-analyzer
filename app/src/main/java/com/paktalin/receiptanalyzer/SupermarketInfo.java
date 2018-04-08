package com.paktalin.receiptanalyzer;

import android.content.Context;

import com.paktalin.receiptanalyzer.receipts.KonsumReceipt;
import com.paktalin.receiptanalyzer.receipts.MaximaReceipt;
import com.paktalin.receiptanalyzer.receipts.PrismaReceipt;
import com.paktalin.receiptanalyzer.receipts.Receipt;
import com.paktalin.receiptanalyzer.receipts.RimiReceipt;
import com.paktalin.receiptanalyzer.receipts.SelverReceipt;

/**
 * Created by Paktalin on 08/04/2018.
 */

class SupermarketInfo {

    private String supermarket;
    private String[] lines;
    private int index = -1;
    private Context context;
    private Receipt receipt = null;
    String firstLine;

    SupermarketInfo(Context context, String[] lines) {
        this.context = context;
        this.lines = lines;
        firstLine = StringManager.clean(lines[0]);
        supermarket = StoreName.getStoreName(firstLine);
    }

    Receipt createReceipt() {
        switch (supermarket){
            case "Selver":
                receipt = new SelverReceipt(lines);
                break;
            case "Prisma":
                receipt = new PrismaReceipt(lines);
                break;
            case "Rimi":
                receipt = new RimiReceipt(lines);
                break;
            case "Konsum":
                receipt = new KonsumReceipt(lines);
                break;
            case "Maxima":
                receipt = new MaximaReceipt(lines);
                break;
        }
        setIndex(receipt.cutFirstLine(firstLine));
        String retailer = getRetailer();
        String address = getAddress();
        receipt.setName(supermarket);
        receipt.setRetailer(retailer);
        receipt.setAddress(address);
        return receipt;
    }

    private void setIndex(String firstLine) {
        String firstLinesPath = supermarket + "/first_lines";
        String[] firstLines = FileManager.getStringFromTextFile(context, firstLinesPath);

        for (int i = 0; i < firstLines.length; i++) {
            String fL = firstLines[i];
            if (StringManager.identical(firstLine, fL)){
                index = i;
            }
        }
    }

    private String getRetailer() {
        String retailersPath = supermarket + "/retailers";
        String[] retailers = FileManager.getStringFromTextFile(context, retailersPath);
        return retailers[index];
    }

    private String getAddress() {
        String addressesPath = supermarket + "/addresses";
        String[] addresses = FileManager.getStringFromTextFile(context, addressesPath);
        return addresses[index];
    }
}

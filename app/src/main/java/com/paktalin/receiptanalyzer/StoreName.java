package com.paktalin.receiptanalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Paktalin on 10.03.2018.
 */

class StoreName {

    String regCode;
    String initialName; //the name read from the receipt
    private String regName; //the name corresponding to the code

    public String getStoreName() {
        if(!isRightFormat()) {
            correct();
        }
        if(isRegistered()) {
            if(areCorresponding()) {
                return regName;
            }
            return initialName;
        }
        return initialName;
    }

    private boolean isRightFormat() {
        try {
            Double.parseDouble(regCode);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void correct() {
        //correct wrong recognitions
    }

    private boolean areCorresponding() {
        //not full
        return regName.equals(initialName);
    }

    //if a company with such code is registered, sets regName and returns true
    // otherwise returns false
    private boolean isRegistered() {
        Document document;
        try {
            document = Jsoup.connect("https://www.neti.ee/visiitkaart/" + regCode)
                    .ignoreHttpErrors(true)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        String webPageText = document.text();
        Pattern findName = Pattern.compile("Visiitkaart: (.*?) Näita");
        Matcher matcher = findName.matcher(webPageText);
        if (matcher.find()) {
            regName = matcher.group(1);
            return true;
        }
        return false;
    }
}

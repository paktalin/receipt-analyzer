package com.paktalin.receiptanalyzer;

/**
 * Created by Paktalin on 21-Mar-18.
 */

abstract class Receipt {
    String name = null;
    String additionalName = null;
    String address = null;

    public String getName() {
        return name;
    }

    String getAdditionalName() {
        return additionalName;
    }

    String getAddress() {
        return address;
    }
}

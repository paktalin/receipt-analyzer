package com.paktalin.receiptanalyzer;

/**
 * Created by Paktalin on 21-Mar-18.
 */

abstract class Receipt {
    private String name;
    private String address;

    void setName(String name) {
        this.name = name;
    }

    void setAddress(String address) {
        this.address = address;
    }

    abstract String[] getTitles();
    abstract String getAddressByIndex(int index);
}

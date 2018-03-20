package com.paktalin.receiptanalyzer.string_similarity.interfaces;

import java.io.Serializable;

/**
 * Created by Paktalin on 19-Mar-18.
 */

public interface StringSimilarity extends Serializable {
    /**
     * Compute and return a measure of similarity between 2 strings.
     * @param s1
     * @param s2
     * @return similarity (0 means both strings are completely different)
     */
    double similarity(String s1, String s2);
}
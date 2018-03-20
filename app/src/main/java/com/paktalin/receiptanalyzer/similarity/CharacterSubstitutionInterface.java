package com.paktalin.receiptanalyzer.similarity;

/**
 * Created by Paktalin on 15.03.2018.
 */

public interface CharacterSubstitutionInterface {
    /**
     * Indicate the cost of substitution c1 and c2.
     * @param c1 The first character of the substitution.
     * @param c2 The second character of the substitution.
     * @return The cost in the range [0, 1].
     */
    double cost(char c1, char c2);
}
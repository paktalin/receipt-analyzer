package com.paktalin.receiptanalyzer;

import com.paktalin.receiptanalyzer.similarity.CharacterSubstitutionInterface;

/**
 * Created by Paktalin on 15.03.2018.
 */

class SearchForSimilarities {

    private CharacterSubstitutionInterface substitute = new CharacterSubstitutionInterface() {
        /**in case of Tall1nn:
         * 1 is c1
         * i is c2
         * */

        @Override
        public double cost(char c1, char c2) {
            double possible = 0.5;
            double impossible = 1;
            if((c1 == '|' && c2 == 'l') ||
                    (c1 == '1' && c2 == 'i') ||
                    (c1 == '1' && c2 == 'l') ||
                    (c1 == 'i' && c2 == 'l') ||
                    (c1 == 'l' && c2 == 'I') ||
                    (c1 == '1' && c2 == 'T') ||
                    (c1 == 'I' && c2 == 'T') ||
                    (c1 == 'o' && c2 == '0') ||
                    (c1 == 'c' && c2 == 'o') ||
                    (c1 == 'i' && c2 == 'j') ||
                    (c1 == 'm' && c2 == 'n') ||
                    (c1 == '0' && c2 == 'o'))
                return possible;
            else
                return impossible;
        }
    };
}

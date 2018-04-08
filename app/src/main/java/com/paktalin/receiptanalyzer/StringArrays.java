package com.paktalin.receiptanalyzer;

/**
 * Created by Paktalin on 20-Mar-18.
 */

public class StringArrays {

    public static String getRimiAddress(int index){
        String[] array = new String[] {
                "Randvere tee 9, Haabneeme, 74001 Harju maakond",
                "Mustakivi tee 13, 13619 Tallinn",
                "Haabersti 1, Tallinn",
                "Sõpruse pst 174",
                "Sõpruse pst 201/203, Tallinn",
                "Suur-Sõjamäe 4, 11415",
                "Ahtri tn 9, 10151",
                "Pärnu mnt 556a, Saue vald, Laagri",
                "Tallinna mnt 41, Viljandi",
                "A.H.Tammsaare tee 104a",
                "Haljala tee 4",
                "Fama tänav 10, Narva",
                "Rebase 10, Tartu",
                "Ringtee 75, Tartu",
                "Papiniidu 8, Pärnu",

                "Tammsaare tee 55, Tallinn",
                "Suur-Sepa 18, Pärnu",
                "Sepa 21, Tartu",
                "Merivälja tee 24, Tallinn",
                "Tööstuse 103, Tallinn",
                "Jaama 32, Haapsalu",
                "Klooga maanteet 10B, Tabasalu",
                "Lilleoru tee 4, Laiaküla",
                "Jüri 85, Võru",
                "Turu 2, Tartu",
                "Telliskivi 61, Tallinn",
                "Tallinna 88, Kuressaare ",
                "Narva maantee 1, Tallinn",
                "Paavli 10, Tallinn",
                "Põhja pst 17, Tallinn",
                "Aia 7, Tallinn",
                "Ehitajate tee 107, Tallinn",
        };
        return array[index];
    }

    public static String[] getRimiAdditionalNames(){
        return new String[]{
                //hyper
                "Viimsi",
                "Lasnamäe Centrumi",
                "Haabersti",
                "Sõpruse",
                "Magistrali",
                "Ülemiste",
                "Nautica",
                "Laagri",
                "Viljandi",
                "Mustamäe keskuse",
                "Rakvere",
                "Narva Fama",
                "Tartu Rebase",
                "Tartu Lõunakeskuse",
                "Pärnu",

                //super
                "Tondi",
                "Pärnu Turu",
                "Sepa",
                "Pirita",
                "Tööstuse",
                "Haapsalu",
                "Tabasalu",
                "Pärnamäe",
                "Võru",
                "Tartu Tasku",
                "Telliskivi",
                "Kuressaare",
                "Postimaja",
                "Sõle",
                "Põhja",
                "Kaubahalli",
                "Nurmenuku",
                "",
        };
    }

    public static String[] getMaximaAddresses(){
        return new String[]{
                "",
        };
    }
}

package com.paktalin.receiptanalyzer;

/**
 * Created by Paktalin on 20-Mar-18.
 */

public class StringArrays {

    public static String[] getSelverFirstStrings() {
        return new String[]{
                "arsenali",
                "baltijaamaturu",
                "baltijaama",
                "jarve",
                "kadaka",
                "kakumae",
                "kotka",
                "karberi",
                "laagri",
                "laanemere",
                "marienthali",
                "merimetsa",
                "mustakivi",
                "pelgulinna",
                "pirita",
                "punane",
                "paaskula",
                "sepapaja",
                "tondi",
                "torupilli",
                "tahesaju",
                "aardla",
                "anne",
                "jaamamoisa",
                "ringtee",
                "sobra",
                "vahi",
                "veeriku",
                "astri",
                "centrumi",
                "hiiumaa",
                "hiiumaara",
                "jogeva",
                "johvi",
                "keila",
                "kohtlajarve",
                "krooni",
                "maardu",
                "mai",
                "mannimae",
                "paide",
                "peetri",
                "poltsamaa",
                "polva",
                "rannarootsi",
                "rapla",
                "saare",
                "saku",
                "suurejoe",
                "valga",
                "viimsi",
                "vilja",
                "ulejoe",
        };
    }

    public static String getSelverAddress(int index) {
        String[] array = {
                "Erika tn 14, Tallinn",
                "Kopli 1, Tallinn",
                "Toompuiestee 37, Tallinn",
                "Pärnu maantee 238, Tallinn",
                "Kadaka tee 56a, Tallinn",
                "Rannamõisa tee 6, Tallinn",
                "Kotka 12, Tallinn",
                "K. Kärberi 20/20a, Tallinn",
                "Pärnu mnt 554, Tallinn",
                "Läänemere tee 28, Tallinn",
                "Mustamäe tee 16, Tallinn",
                "Paldiski mnt 56, Tallinn",
                "Mustakivi 3a, Tallinn",
                "Sõle 51, Tallinn",
                "Rummu tee 4, Tallinn",
                "Punane 46, Tallinn",
                "Pärnu mnt 534b, Tallinn",
                "Sepapaja 2, Tallinn",
                "Tammsaare tee 62, Tallinn",
                "Vesivärava 37, Tallinn",
                "Tähesaju tee 1, Tallinn",
                "Võru tn 77, Tartu",
                "Kalda tee 43, Tartu",
                "Jaama 74, Tartu linn",
                "Aardla 114, Tartu",
                "Sõbra 41, Tartu",
                "Vahi 62, Tartu",
                "Vitamiini 1, Tartu",
                "Tallinna mnt 41, Narva",
                "Tallinna mnt 22, Viljandi",
                "Rehemäe, Linnumäe küla, Pühalepa vald, Hiiumaa",
                "Rehemäe, Linnumäe küla, Pühalepa vald, Hiiumaa",
                "Kesk 3a/4, Jõgeva",
                "Narva mnt. 8, Jõhvi",
                "Piiri tn 12, Keila",
                "Järveküla tee 68, Kohtla-Järve",
                "F. G. Adoffi 11, Rakvere",
                "Nurga tn 3, Maardu",
                "Papiniidu 42, Pärnu",
                "Riia mnt 35, Viljandi",
                "Aiavilja tn 4, Paide",
                "Veesaare tee 2, Peetri alevik",
                "Jõgeva mnt 1a, Põltsamaa",
                "Jaama 12, Põlva",
                "Rannarootsi tee 1, Uuemõisa",
                "Tallinna maantee 4, Rapla",
                "Tallinna tn 67, Kuressaare",
                "Üksnurme tee 2, Saku alevik, Harjumaa",
                "Suur-Jõe 57, Pärnu",
                "Raja tn 5, Valga",
                "Sõpruse tee 15, 74001, Haabneeme, Viimsi vald",
                "Vilja tn 6, Võru",
                "Tallinna mnt 93a/ Roheline 80, Pärnu"};
        return array[index];
    }

    public static String getSelverAdditionalName(int index) {
        String[] array = {
                "Arsenali Selver",
                "Balti Jaama Turu Selver",
                "Balti jaama Selver",
                "Järve Selver",
                "Kadaka Selver",
                "Kakumäe Selver",
                "Kotka Selver",
                "Kärberi Selver",
                "Laagri Selver",
                "Läänemere Selver",
                "Marienthali Selver",
                "Merimetsa Selver",
                "Mustakivi Selver",
                "Pelgulinna Selver",
                "Pirita Selver",
                "Punane Selver",
                "Pääsküla Selver",
                "Sepapaja Selver",
                "Tondi Selver",
                "Torupilli Selver",
                "Tähesaju Selver",
                "Aardla Selver",
                "Anne Selver",
                "Jaamamõisa Selver",
                "Ringtee Selver",
                "Sõbra Selver",
                "Vahi Selver",
                "Veeriku Selver",
                "Astri Selver",
                "Centrumi Selver",
                "Hiiumaa Selver",
                "Hiiumaa rändpood",
                "Jõgeva Selver",
                "Jõhvi Selver",
                "Keila Selver",
                "Kohtla-Järve Selver",
                "Krooni Selver",
                "Maardu Selver",
                "Mai Selver",
                "Männimäe Selver",
                "Paide Selver",
                "Peetri Selver",
                "Põltsamaa Selver",
                "Põlva Selver",
                "Rannarootsi Selver",
                "Rapla Selver",
                "Saare Selver",
                "Saku Selver",
                "Suurejõe Selver",
                "Valga Selver",
                "Viimsi Selver",
                "Vilja Selver",
                "Ülejõe Selver"};
        return array[index];
    }

    public static String[] getPrismaFirstStrings(){
        return new String[]{
                "sikupilliprisma",
                "roccaalmareprisma",
                "kristiineprisma",
                "mustamaeprisma",
                "lasnamaeprisma",
                "sobraprisma",
                "annelinnaprisma",
                "narvaprisma"};
    }

    public static String getPrismaAddress(int index) {
        String[] array = {
                "Tartu mnt 87, 10112, Tallinn",
                "Paldiski mnt 102, 13522, Tallinn",
                "Endla 45, 10615, Tallinn",
                "A.H.Tammsaare tee 116, 12918, Tallinn",
                "Mustakivi tee 17, 13912, Tallinn",
                "Sõbra 58, 50106, Tartu",
                "Nõlvaku 2, 50708, Tartu",
                "Kangelaste prospekt 29, 20607, Narva"};
        return array[index];
    }

    public static String getPrismaAdditionalName(int index) {
        String[] array = {
                "Sikupilli Prisma",
                "Rocca Al Mare Prisma",
                "Kristiine Prisma",
                "Mustamäe Prisma",
                "Lasnamäe Prisma",
                "Sõbra Prisma",
                "Annelinna Prisma\n",
                "Narva Prisma"};
        return array[index];
    }

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

package com.paktalin.receiptanalyzer;

/**
 * Created by Paktalin on 20-Mar-18.
 */

class StringArrays {

    static String[] getFirstGroupFirstLines() {
        return new String[]{
                "maximaeestiouregnr10765896",
                "rimieestifoodasregnr10263574",
                "harjutarbijateuhistu"
        };
    }

    static String getFirstGroupNameByIndex(int index) {
        String[] array = {
                "Maxima",
                "Rimi",
                "Konsum"
        };
        return array[index];
    }

    static String[] getSecondGroupFirstLines() {
        return new String[]{
                "prismaperemarketas",
                "selver"};
    }

    static String getSecondGroupNameByIndex(int index) {
        String[] array = {
                "Prisma",
                "Selver"
        };
        return array[index];
    }

    static String[] getSelverNames() {
        return new String[]{
                "arsenaliselver",
                "baltijaamaturuselver",
                "baltijaamaselver",
                "jarveselver",
                "kadakaselver",
                "kakumaeselver",
                "kotkaselver",
                "karberikelver",
                "laagriselver",
                "laanemereselver",
                "marienthaliselver",
                "merimetsaselver",
                "mustakiviselver",
                "pelgulinnaselver",
                "piritaselver",
                "punaneselver",
                "paaskulaselver",
                "sepapajaselver",
                "tondiselver",
                "torupilliselver",
                "tahesajuselver",
                "aardlaselver",
                "anneselver",
                "jaamamoisaselver",
                "ringteeselver",
                "sobraselver",
                "vahiselver",
                "veerikuselver",
                "astriselver",
                "centrumiselver",
                "hiiumaaselver",
                "hiiumaarändpood",
                "jogevaselver",
                "johviselver",
                "keilaselver",
                "kohtlajarveselver",
                "krooniselver",
                "maarduselver",
                "maiselver",
                "mannimaeselver",
                "paideselver",
                "peetriselver",
                "poltsamaaselver",
                "rannarootsiselver",
                "raplaselver",
                "saareselver",
                "sakuselver",
                "suurejoeselver",
                "valgaselver",
                "viimsiselver",
                "viljaselver",
                "ulejoeselver",
        };
    }

    static String getSelverAddress(int index) {
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
                "Tallinna mnt 93a/ Roheline 80, Pärnu",
        };
        return array[index];
    }
}

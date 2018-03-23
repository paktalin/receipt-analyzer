package com.paktalin.receiptanalyzer;

import java.util.TreeMap;

/**
 * Created by Paktalin on 23-Mar-18.
 */

public class Tags {

    public TreeMap<String, String[]> tags() {
        TreeMap<String, String[]> tags = new TreeMap<>();

        tags.put("bread", bread());
        tags.put("milk", milk());
        tags.put("fruits", fruits());
        tags.put("vegetables", vegetables());
        tags.put("sweets", sweets());
        tags.put("meat", meat());
        tags.put("mushrooms", mushrooms());
        tags.put("others", others());

        return tags;
    }

    private String[] bread() {
        return new String[]{
                "lei",
                "sai",
                "piruka",
                "keerd",
                "struud",
                "pontsik",
                "tasku",
                "rull"
        };
    }

    private String[] milk() {
        return new String[]{
                "piim",
                "jogurt",
                "juust",
                "voi",
                "keefir",
                "kohupiim",
                "kreem",
                "kohuke",
        };
    }

    private String[] fruits() {
        return new String[]{
                "banaan",
                "oun",
                "apelsin",
        };
    }

    private String[] vegetables() {
        return new String[]{
                "kur",
                "tomat",
                "kartul",
                "koogivil",
                "kapsa",
                "sibul",
        };
    }

    private String[] sweets() {
        return new String[]{
                "sokolaad",
                "kupsi",
                "piimasok",
                "kompvek",
        };
    }

    private String[] meat() {
        return new String[]{
                "sea",
                "liha",
                "veise",
                "kana"
        };
    }

    private String[] mushrooms() {
        return new String[] {
                "sampinjon"
        };
    }

    private String[] others() {
        return new String[]{
                "taruvai",
                "lamp"
        };
    }
}

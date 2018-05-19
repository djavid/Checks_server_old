package com.djavid.checkserver.util;

public class LogoUtil {

    public static String getLogo(String title) {

        switch (title) {
            case "ООО Вкусвилл":
                return "https://www.shopolog.ru/s/img/brands/1c/0a/800x400_1c0a267ef3d3944c3b9f1a1cf7211ba1___jpg____4_4e4fda72.png";
            default:
                return "";
        }

    }
}

package com.djavid.checkserver.util;

public class LogoUtil {

    public static String getLogo(String title) {

        switch (title) {
            case "Вкусвилл":
                return "https://www.shopolog.ru/s/img/brands/1c/0a/800x400_1c0a267ef3d3944c3b9f1a1cf7211ba1___jpg____4_4e4fda72.png";
            case "Бургер рус":
                return "https://upload.wikimedia.org/wikipedia/ru/thumb/3/3a/Burger_King_Logo.svg/145px-Burger_King_Logo.svg.png";
//            case "Ям! Ресторантс Интернэшнл Раша Си Ай Эс":
//                return "";
//            case "Т и К Продукты":
//                return "";
//            case "Юникло (Рус)":
//                return "";
//            case "Вардекс":
//                return "";
//            case "Планета увлечений":
//                return "";
//            case "АДИДАС":
//                return "";
//            case "":
//                return "";
//            case "":
//                return "";
//            case "":
//                return "";
            default:
                return "";
        }

    }
}

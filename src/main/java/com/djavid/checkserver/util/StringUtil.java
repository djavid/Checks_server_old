package com.djavid.checkserver.util;

import java.util.regex.Pattern;

public class StringUtil {

    public static String formatShopTitle(String input) {
        String s = input;

        s = Pattern.compile("(ООО|ЗАО|ооо|зао)").matcher(s).replaceAll("");
        s = Pattern.compile("['\"_]").matcher(s).replaceAll("");
        s = Pattern.compile("\\s+").matcher(s).replaceAll(" ");
        s = s.trim();

        return s;
    }

}

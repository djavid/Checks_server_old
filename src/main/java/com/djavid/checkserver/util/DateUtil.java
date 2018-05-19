package com.djavid.checkserver.util;

import com.djavid.checkserver.model.entity.Receipt;
import org.joda.time.DateTime;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    public static Double getTotal(List<Receipt> receipts, SumInterval type) {

        Double total = 0d;
        DateTime currentDate = new DateTime();

        for (Receipt it: receipts) {
            DateTime date = DateTime.parse(it.getDateTime());

            boolean add = false;
            switch (type) {
                case TOTAL:
                    add = true;
                    break;
                case DAY:
                    if (date.isAfter(currentDate.withTimeAtStartOfDay()))
                        add = true;
                    break;
                case WEEK:
                    if (date.isAfter(currentDate.withTimeAtStartOfDay().withDayOfWeek(1)))
                        add = true;
                    break;
                case MONTH:
                    if (date.isAfter(currentDate.withTimeAtStartOfDay().withDayOfMonth(1)))
                        add = true;
                    break;
                case LAST_DAY:
                    if (date.isAfter(currentDate.minusHours(24)))
                        add = true;
                    break;
                case LAST_WEEK:
                    if (date.isAfter(currentDate.minusWeeks(1)))
                        add = true;
                    break;
                case LAST_MONTH:
                    if (date.isAfter(currentDate.minusMonths(1)))
                        add = true;
                    break;
            }

            if (add) total += it.getTotalSum();
        }

        return total;
    }

    @Nullable
    public static DateTime parseDate(String s) {

        Pattern pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})T(\\d{2})(\\d{2})(\\d{2})?");
        Matcher matcher = pattern.matcher(s);

        String formatted = s;
        if (matcher.matches()) {
            formatted = matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3) + "T" +
                    matcher.group(4) + ":" + matcher.group(5);
            if (matcher.group(6) != null)
                formatted += ":" + matcher.group(6);
        }

        try {
            return DateTime.parse(formatted);
        } catch (Exception e) {
            return null;
        }
    }

}

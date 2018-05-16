package com.djavid.checkserver.util;

import com.djavid.checkserver.model.entity.Receipt;
import org.joda.time.DateTime;

import java.util.List;

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

}

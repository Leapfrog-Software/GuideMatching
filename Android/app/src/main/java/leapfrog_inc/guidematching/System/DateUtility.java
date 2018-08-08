package leapfrog_inc.guidematching.System;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtility {

    public static String dateToString(Date date, String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        return dateFormat.format(date);
    }

    public static Date stringToDate(String string, String format) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
            return dateFormat.parse(string);
        } catch (Exception e) {
            return null;
        }
    }
}

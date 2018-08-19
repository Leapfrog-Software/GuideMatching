package leapfrog_inc.guidematching.System;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static Calendar latestSunday(Calendar targetDate) {

        Calendar current = targetDate;

        for (int i = 0; i < 7; i++) {
            if (current.get(Calendar.DAY_OF_WEEK) == 1) {
                return current;
            }
            current.add(Calendar.DATE, -1);
        }
        return null;
    }

    public static Calendar copyDate(Calendar calendar) {

        Calendar copyCalendar = Calendar.getInstance();
        copyCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        copyCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        copyCalendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
        return copyCalendar;
    }

    public static boolean isSameMonth(Calendar date1, Calendar date2) {

        if ((date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR))
                && (date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH))) {
            return true;
        }
        return false;
    }

    public static boolean isSameDay(Calendar date1, Calendar date2) {

        if ((date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR))
                && (date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH))
                && (date1.get(Calendar.DATE) == date2.get(Calendar.DATE))) {
            return true;
        }
        return false;
    }

    public static boolean isPastDay(Calendar date1, Calendar date2) {

        int year1 = date1.get(Calendar.YEAR);
        int month1 = date1.get(Calendar.MONTH);
        int day1 = date1.get(Calendar.DATE);

        int year2 = date2.get(Calendar.YEAR);
        int month2 = date2.get(Calendar.MONTH);
        int day2 = date2.get(Calendar.DATE);

        if (year1 < year2) {
            return true;
        } else if (year1 > year2) {
            return false;
        }
        if (month1 < month2) {
            return true;
        } else if (month1 > month2) {
            return false;
        }
        if (day1 < day2) {
            return true;
        } else if (day1 > day2) {
            return false;
        }
        return false;
    }

    public static String toMonthYearText(Calendar calendar) {

        String[] monthAry = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String month = monthAry[calendar.get(Calendar.MONTH)];
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return month + "." + year;
    }

    public static String toDayMonthYearText(Calendar calendar) {

        String day = String.valueOf(calendar.get(Calendar.DATE));
        String[] monthAry = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String month = monthAry[calendar.get(Calendar.MONTH)];
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return month + " " + day + " " + year;
    }

    public static String output(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        String str = String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(date);
        Log.d("calendar", str);
        return str;
    }
}

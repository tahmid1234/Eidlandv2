package com.eidland.auxilium.voice.only.helper;

import java.util.Calendar;
import java.util.Locale;

public class DateFormater {
    public static String getDate(String timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timeInMillis));
        String date = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
        date = date + " " + calendar.get(Calendar.HOUR);
        date = date + ":" + calendar.get(Calendar.MINUTE);
        date = date + " " + calendar.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.ENGLISH);
        date = date + " " + calendar.get(Calendar.DAY_OF_MONTH);
        date = date + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
        date = date + " " + calendar.get(Calendar.YEAR);
        return date;
    }
    public static String getSessionDate(String timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timeInMillis));
        String date = calendar.get(Calendar.HOUR)+"";
        date = date + ":" + calendar.get(Calendar.MINUTE);
        date = date + " " + calendar.getDisplayName(Calendar.AM_PM, Calendar.LONG, Locale.ENGLISH);
        date = date + ", " + calendar.get(Calendar.DAY_OF_MONTH) + "th";
        date = date + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
        return date;
    }
}

package at.stefanirndorfer.maintainfc.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

public class CalendarUtils {
    public static Calendar assembleCalendar(int year, int month, int day, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month, day, hour, minute);
        Timber.d("calender is set to: " + new Date(c.getTimeInMillis()).toString());
        return c;
    }

    public static String getPrintableCalendar(Calendar c) {
        Date date = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(date);
    }
}

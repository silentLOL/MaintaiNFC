package at.stefanirndorfer.maintainfc.util;

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
}

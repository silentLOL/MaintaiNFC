package at.stefanirndorfer.maintainfc.viewmodel;

import java.util.Calendar;

import androidx.lifecycle.ViewModel;
import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;
import at.stefanirndorfer.maintainfc.util.CalendarUtils;

public class DateTimeViewModel extends ViewModel {
    private int year;
    private int month; // don't forget --> month + 1 if toString()
    private int day;
    private int hour;
    private int minute;

    public SingleLiveEvent<Boolean> triggerDataEvaluation = new SingleLiveEvent<>();

    public void triggerDataEvaluation() {
        triggerDataEvaluation.setValue(true);
    }


    /////////////////////////
    // getters and setters
    /////////////////////////
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setDateData(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    public void setTimeData(int hour, int minute) {
        setHour(hour);
        setMinute(minute);
    }

    public void initDateTimeData() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        setYear(c.get(Calendar.YEAR));
        setMonth(c.get(Calendar.MONTH));
        setDay(c.get(Calendar.DAY_OF_MONTH));
        setHour(c.get(Calendar.HOUR_OF_DAY));
        setMinute(c.get(Calendar.MINUTE));
    }

    public Calendar getCalendar() {
        return CalendarUtils.assembleCalendar(year, month, day, hour, minute);
    }
}

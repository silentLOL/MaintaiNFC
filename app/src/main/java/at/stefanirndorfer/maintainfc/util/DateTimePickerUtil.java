package at.stefanirndorfer.maintainfc.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import java.util.Calendar;
import java.util.Objects;

import at.stefanirndorfer.maintainfc.viewmodel.DateTimeViewModel;
import timber.log.Timber;

public class DateTimePickerUtil {

    public DateTimePickerUtil() {
    }

    public static void setDateTime(Context context, DateTimeViewModel setDateTimeViewModel) {
        Timber.d("user about to set date and time");
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(context), (view, mYear, mMonth, mDay) -> {
            setDateTimeViewModel.setDateData(mYear, mMonth, mDay);
            showTimePicker(context, setDateTimeViewModel);
        },
                // the initial values
                setDateTimeViewModel.getYear(),
                setDateTimeViewModel.getMonth(),
                setDateTimeViewModel.getDay());

        datePickerDialog.show();
    }

    private static void showTimePicker(Context context, DateTimeViewModel setDateTimeViewModel) {
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (view, hourOfDay, minute) -> {
                    setDateTimeViewModel.setTimeData(hourOfDay, minute);
                    setDateTimeViewModel.triggerDataEvaluation();
                },
                // initial time values
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

}

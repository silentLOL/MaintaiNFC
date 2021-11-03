package at.stefanirndorfer.maintainfc.viewmodel;

import java.util.Calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.stefanirndorfer.maintainfc.model.DateTimeEvaluation;
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.model.NextDateTimeEvaluation;
import at.stefanirndorfer.maintainfc.util.CalendarUtils;
import timber.log.Timber;

/**
 * central data class for reading and writing data
 * also contains the validity checks for the input date
 */
public class ResultsViewModel extends ViewModel {
    public MutableLiveData<String> dateAndTime;
    public MutableLiveData<String> nextDateAndTime;
    public MutableLiveData<Calendar> dateAndTimeCalendar;
    public MutableLiveData<Calendar> nextDateAndTimeCalendar;

    public ResultsViewModel() {
        dateAndTime = new MutableLiveData<>();
        nextDateAndTime = new MutableLiveData<>();
        dateAndTimeCalendar = new MutableLiveData<>();
        nextDateAndTimeCalendar = new MutableLiveData<>();
    }

    public void setDateAndTimeCalendar(Calendar dateAndTimeCalendar) {
        dateAndTime.setValue(CalendarUtils.getPrintableCalendar(dateAndTimeCalendar));
        this.dateAndTimeCalendar.setValue(dateAndTimeCalendar);
    }

    public void setNextDateAndTimeCalendar(Calendar calendar) {
        nextDateAndTime.setValue(CalendarUtils.getPrintableCalendar(calendar));
        this.nextDateAndTimeCalendar.setValue(calendar);
    }

    /**
     * wraps the data into a MaintenanceData object
     *
     * @returns the object of null if a mandatory field is missing
     */
    public MaintenanceData getMaintenanceData() {
        if (!isDataComplete()) {
            Timber.d("The required data are not given");
            return null;
        }

        return new MaintenanceData(dateAndTimeCalendar.getValue().getTimeInMillis(),
                                   nextDateAndTimeCalendar.getValue().getTimeInMillis());
    }

    /**
     * comment is not checked since it's not a mandatory field
     *
     * @return true if data are complete
     */
    private boolean isDataComplete() {
        return dateAndTimeCalendar.getValue() != null
               && nextDateAndTimeCalendar.getValue() != null;
    }

    /**
     * todo: check if there is more to validate
     *
     * @param calInMillis
     * @return
     */
    public DateTimeEvaluation validateDateTimeInput(Calendar calInMillis) {
        if (calInMillis == null) {
            return DateTimeEvaluation.EMPTY;
        }
        return DateTimeEvaluation.OK;
    }

    public NextDateTimeEvaluation validateNextDateTimeInput(Calendar calInMillis) {
        if (calInMillis == null) {
            return NextDateTimeEvaluation.EMPTY;
        }
        if (dateAndTimeCalendar.getValue() == null) {
            return NextDateTimeEvaluation.THIS_DATE_TIME_MUST_BE_SET_FIRST;
        }
        if (calInMillis.getTimeInMillis() <= dateAndTimeCalendar.getValue().getTimeInMillis()) {
            return NextDateTimeEvaluation.NEXT_MUST_BE_AFTER_THIS_TIME;
        }
        return NextDateTimeEvaluation.OK;
    }

    /**
     * this deletes all the data
     */
    public void clearData() {
        dateAndTime.setValue("");
        nextDateAndTime.setValue("");
        dateAndTimeCalendar.setValue(null);
        nextDateAndTimeCalendar.setValue(null);
    }

    public void setMaintenanceData(MaintenanceData maintenanceData) {
        Calendar.getInstance().setTimeInMillis(maintenanceData.getTimestamp());
        dateAndTime.setValue(CalendarUtils.getStringFromMillis(maintenanceData.getTimestamp()));
        nextDateAndTime.setValue(CalendarUtils.getStringFromMillis(maintenanceData.getNextTimestamp()));
    }
}
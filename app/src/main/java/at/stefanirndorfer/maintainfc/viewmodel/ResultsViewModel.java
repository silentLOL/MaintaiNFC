package at.stefanirndorfer.maintainfc.viewmodel;

import java.util.Calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.stefanirndorfer.maintainfc.model.DateTimeEvaluation;
import at.stefanirndorfer.maintainfc.model.EmployeeIdEvaluation;
import at.stefanirndorfer.maintainfc.util.CalendarUtils;

/**
 * central data class for reading and writing data
 * also contains the validity checks for the input date
 */
public class ResultsViewModel extends ViewModel {
    public MutableLiveData<String> employeeId;
    public MutableLiveData<String> dateAndTime;
    public MutableLiveData<String> nextDateAndTime;
    public MutableLiveData<String> comment;
    public MutableLiveData<Calendar> dateAndTimeCalendar;

    public ResultsViewModel() {
        employeeId = new MutableLiveData<>();
        dateAndTime = new MutableLiveData<>();
        nextDateAndTime = new MutableLiveData<>();
        comment = new MutableLiveData<>();
        dateAndTimeCalendar = new MutableLiveData<>();
    }

    public void setDateAndTimeCalendar(Calendar dateAndTimeCalendar) {
        dateAndTime.setValue(CalendarUtils.getPrintableCalendar(dateAndTimeCalendar));
        this.dateAndTimeCalendar.setValue(dateAndTimeCalendar);
    }

    public EmployeeIdEvaluation validateEmployeeIdInput() {
        if (employeeId.getValue() == null) {
            return EmployeeIdEvaluation.EMPTY;
        }
        return EmployeeIdEvaluation.OK;
    }


    /**
     * todo: check if there is more to validate
     *
     * @param calInMillis
     * @return
     */
    public DateTimeEvaluation validateDateTimeInput(Calendar calInMillis) {
        return DateTimeEvaluation.OK;
    }
}
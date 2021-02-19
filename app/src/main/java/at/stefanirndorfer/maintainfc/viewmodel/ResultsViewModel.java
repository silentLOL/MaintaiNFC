package at.stefanirndorfer.maintainfc.viewmodel;

import java.util.Calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.stefanirndorfer.maintainfc.model.CommentEvaluation;
import at.stefanirndorfer.maintainfc.model.DateTimeEvaluation;
import at.stefanirndorfer.maintainfc.model.EmployeeIdEvaluation;
import at.stefanirndorfer.maintainfc.model.NextDateTimeEvaluation;
import at.stefanirndorfer.maintainfc.util.CalendarUtils;
import at.stefanirndorfer.maintainfc.util.Constants;
import timber.log.Timber;

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
    public MutableLiveData<Calendar> nextDateAndTimeCalendar;

    public ResultsViewModel() {
        employeeId = new MutableLiveData<>();
        dateAndTime = new MutableLiveData<>();
        nextDateAndTime = new MutableLiveData<>();
        dateAndTimeCalendar = new MutableLiveData<>();
        nextDateAndTimeCalendar = new MutableLiveData<>();
        comment = new MutableLiveData<>();
    }

    public void setDateAndTimeCalendar(Calendar dateAndTimeCalendar) {
        dateAndTime.setValue(CalendarUtils.getPrintableCalendar(dateAndTimeCalendar));
        this.dateAndTimeCalendar.setValue(dateAndTimeCalendar);
    }

    public void setNextDateAndTimeCalendar(Calendar calendar) {
        nextDateAndTime.setValue(CalendarUtils.getPrintableCalendar(calendar));
        this.nextDateAndTimeCalendar.setValue(calendar);
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
     * TODO: Do we have to check if there could be x-site scripting possible?
     *
     * @param comment
     * @return
     */
    public CommentEvaluation validateCommentInput(String comment) {
        if (comment.length() > Constants.MAX_COMMENT_LENGTH) {
            Timber.d("Comment input exceeds the max length");
            return CommentEvaluation.TOO_LONG;
        }
        return CommentEvaluation.OK;
    }
}
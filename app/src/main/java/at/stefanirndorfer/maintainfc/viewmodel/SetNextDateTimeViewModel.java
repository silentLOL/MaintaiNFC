package at.stefanirndorfer.maintainfc.viewmodel;

import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;
import timber.log.Timber;

public class SetNextDateTimeViewModel extends DateTimeViewModel {

    private final long MONTH_IN_MILLIS = 2628000000L;

    public SingleLiveEvent<Boolean> isNextButtonAvailable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> nextButtonPressed = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> setNextDateTimeButtonPressed = new SingleLiveEvent<>();
    public SingleLiveEvent<Long> setNextPredefinedDateTimePressed = new SingleLiveEvent<>();

    public SetNextDateTimeViewModel() {
    }

    public void onNextButtonClicked() {
        Timber.d("on next button click");
        nextButtonPressed.setValue(true);
    }

    public void onSetNextDateTimeButtonPressed() {
        Timber.d("set date time button click");
        setNextDateTimeButtonPressed.setValue(true);
    }

    public void onSetNextDateInSixMonths() {
        setNextPredefinedDateTimePressed.setValue(MONTH_IN_MILLIS * 6);
    }

    public void onSetNextDateInTwoYears() {
        setNextPredefinedDateTimePressed.setValue(MONTH_IN_MILLIS * 24);
    }

    public void onSetNextDateInOneYear() {
        setNextPredefinedDateTimePressed.setValue(MONTH_IN_MILLIS * 12);
    }
}
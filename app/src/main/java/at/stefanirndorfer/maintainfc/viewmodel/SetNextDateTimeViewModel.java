package at.stefanirndorfer.maintainfc.viewmodel;

import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;
import timber.log.Timber;

public class SetNextDateTimeViewModel extends DateTimeViewModel {
    public SingleLiveEvent<Boolean> isNextButtonAvailable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> nextButtonPressed = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> setNextDateTimeButtonPressed = new SingleLiveEvent<>();

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
}
package at.stefanirndorfer.maintainfc.viewmodel;

import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;
import timber.log.Timber;

public class SetDateTimeViewModel extends DateTimeViewModel {

    public SingleLiveEvent<Boolean> isNextButtonAvailable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> nextButtonPressed = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> setDateTimeButtonPressed = new SingleLiveEvent<>();

    public SetDateTimeViewModel() {
    }

    public void onNextButtonClicked() {
        Timber.d("on next button click");
        nextButtonPressed.setValue(true);
    }

    public void onSetDateTimeButtonPressed() {
        Timber.d("set date time button click");
        setDateTimeButtonPressed.setValue(true);
    }


}
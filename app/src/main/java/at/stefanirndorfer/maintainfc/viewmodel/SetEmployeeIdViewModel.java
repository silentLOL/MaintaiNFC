package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.lifecycle.ViewModel;
import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;
import timber.log.Timber;

public class SetEmployeeIdViewModel extends ViewModel {

    public SingleLiveEvent<String> employeeId = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> isNextButtonAvailable = new SingleLiveEvent<>();
    public SingleLiveEvent<Integer> nextButtonPressed = new SingleLiveEvent<>();

    public SetEmployeeIdViewModel() {
    }

    public void onNextButtonClicked() {
        Timber.d("on next button click");
        nextButtonPressed.setValue(Integer.parseInt(employeeId.getValue()));
    }
}
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


    public void onEmployeeIdInputReceived(Integer employeeId) {
        if (employeeId != null) {
            Timber.d("enabling next button");
            isNextButtonAvailable.setValue(true);
        } else {
            Timber.d("disabling next button");
            isNextButtonAvailable.setValue(false);
        }
    }

    public void onNextButtonClicked() {
        Timber.d("on next button click");
        if (employeeId.getValue() != null && Integer.parseInt(employeeId.getValue()) > 0) {
            nextButtonPressed.setValue(Integer.parseInt(employeeId.getValue()));
        }
    }
}
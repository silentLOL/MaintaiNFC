package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

public class SetEmployeeIdViewModel extends ViewModel {

    public MutableLiveData<String> employeeId = new MutableLiveData<>();
    public MutableLiveData<Boolean> isNextButtonAvailable = new MutableLiveData<>();
    public MutableLiveData<Integer> nextButtonPressed = new MutableLiveData<>();

    public SetEmployeeIdViewModel() {
    }


    public void onEmployeeIdInputReceived(Integer employeeId) {
        if (employeeId != null && employeeId > 0) {
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
package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResultsViewModel extends ViewModel {
    public MutableLiveData<String> employeeId;
    public MutableLiveData<String> dateAndTime;
    public MutableLiveData<String> nextDateAndTime;
    public MutableLiveData<String> comment;

    public ResultsViewModel() {
        employeeId = new MutableLiveData<>();
        dateAndTime = new MutableLiveData<>();
        nextDateAndTime = new MutableLiveData<>();
        comment = new MutableLiveData<>();
    }
}
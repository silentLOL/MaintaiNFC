package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;


public class MainScreenViewModel extends ViewModel {

    public MutableLiveData<Boolean> goToRead = new MutableLiveData<>();
    public MutableLiveData<Boolean> goToWrite = new MutableLiveData<>();

    public MainScreenViewModel() {
    }

    public void onReadButtonClick() {
        Timber.d("read pressed. Instance: %s", this);
        goToRead.setValue(true);
    }

    public void onWriteButtonClick() {
        Timber.d("write pressed. Instance: %s", this);
        goToWrite.setValue(true);
    }
}
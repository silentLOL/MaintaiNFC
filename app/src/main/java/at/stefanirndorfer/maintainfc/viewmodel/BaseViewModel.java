package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel extends ViewModel {

    BaseObservable observable;

    public BaseViewModel() {
    }

    abstract void createObservable();

    public void setObservable(BaseObservable observable) {
        this.observable = observable;
    }

    public BaseObservable getObservable() {
        return observable;
    }
}

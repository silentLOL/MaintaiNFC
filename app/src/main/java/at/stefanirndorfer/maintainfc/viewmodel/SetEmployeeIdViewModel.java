package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.lifecycle.ViewModel;

public class SetEmployeeIdViewModel extends ViewModel {

    private SetEmployeeIdViewModelListener listener;

    public void setListener(SetEmployeeIdViewModelListener listener) {
        this.listener = listener;
    }

    public interface SetEmployeeIdViewModelListener {

    }
}
package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.lifecycle.ViewModel;

public class SetNextDateTimeViewModel extends ViewModel {
    private SetNextDateTimeViewModelListener listener;

    public void setListener(SetNextDateTimeViewModelListener listener) {
        this.listener = listener;
    }

    public interface SetNextDateTimeViewModelListener {
    }
}
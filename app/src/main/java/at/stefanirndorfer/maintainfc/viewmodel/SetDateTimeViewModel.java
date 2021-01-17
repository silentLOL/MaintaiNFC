package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.lifecycle.ViewModel;

public class SetDateTimeViewModel extends ViewModel {
    private SetDateTimeViewModelListener listener;

    public void setListener(SetDateTimeViewModelListener listener){
        this.listener = listener;
    }
    public interface SetDateTimeViewModelListener {
    }
}
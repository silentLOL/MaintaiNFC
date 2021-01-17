package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.lifecycle.ViewModel;

public class WriteToTagViewModel extends ViewModel {

    private WriteToTagViewModelListener listener;

    public void setListener(WriteToTagViewModelListener listener){
        this.listener = listener;
    }
    public interface WriteToTagViewModelListener{

    }
}
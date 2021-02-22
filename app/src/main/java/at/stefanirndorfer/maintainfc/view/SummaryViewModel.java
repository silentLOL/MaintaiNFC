package at.stefanirndorfer.maintainfc.view;

import androidx.lifecycle.ViewModel;
import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;

public class SummaryViewModel extends ViewModel {
    public SingleLiveEvent<Boolean> nextButtonClicked = new SingleLiveEvent<>();

    public void onNextButtonClicked() {
        nextButtonClicked.setValue(true);
    }
}
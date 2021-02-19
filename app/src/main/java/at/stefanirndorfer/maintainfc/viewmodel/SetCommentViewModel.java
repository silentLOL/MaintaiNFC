package at.stefanirndorfer.maintainfc.viewmodel;

import androidx.lifecycle.ViewModel;

import at.stefanirndorfer.maintainfc.model.SingleLiveEvent;
import timber.log.Timber;

public class SetCommentViewModel extends ViewModel {

  public SingleLiveEvent<String> comment = new SingleLiveEvent<>();
  public SingleLiveEvent<Boolean> isNextButtonAvailable = new SingleLiveEvent<>();
  public SingleLiveEvent<Integer> nextButtonPressed = new SingleLiveEvent<>();

  public SetCommentViewModel() {
  }

  public void onNextButtonClicked() {
    Timber.d("on next button click");
    nextButtonPressed.setValue(Integer.parseInt(comment.getValue()));
  }
}

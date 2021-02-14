package at.stefanirndorfer.maintainfc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.databinding.MainScreenFragmentBinding;
import at.stefanirndorfer.maintainfc.viewmodel.MainScreenViewModel;
import timber.log.Timber;

public class MainScreenFragment extends BaseFragment {

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Override
    ViewDataBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.main_screen_fragment, container, false);
    }

    @Override
    void onCreateSetupViewModel(ViewDataBinding binding) {
        MainScreenViewModel mainScreenViewModel = new ViewModelProvider(this).get(MainScreenViewModel.class);
        ((MainScreenFragmentBinding) binding).setViewModel(mainScreenViewModel);
        subscribeOnLiveData(mainScreenViewModel);
    }

    private void subscribeOnLiveData(MainScreenViewModel mainScreenViewModel) {
        Timber.d("%s is subscribing to live data", this);
        mainScreenViewModel.goToRead.setValue(null);
        mainScreenViewModel.goToWrite.setValue(null);
        if (!mainScreenViewModel.goToWrite.hasObservers()) { //
            mainScreenViewModel.goToWrite.observe(this, isGoingToWrite -> {
                if (isGoingToWrite != null && isGoingToWrite) {
                    Timber.d("we will navigate to write %s", this);
                    navigateToWriteToTagFragment();
                }
            });
        }
        if (!mainScreenViewModel.goToRead.hasObservers()) {
            mainScreenViewModel.goToRead.observe(this, isGoingToRead -> {
                if (isGoingToRead != null && isGoingToRead) {
                    Timber.d("we will navigate to read %s", this);
                    navigateToReadFragment();
                }
            });
        }
    }

    @Override
    void setNFCReadingAllowed() {
        navigationListener.isNFCReadingAllowed(true); /* we want to read while in foreground */
    }

    @Override
    void setShowHomeButton() {
        navigationListener.hideHomeButton();
    }

    @Override
    void setResultsFragmentVisibility() {
        navigationListener.setResultsFragmentVisibility(View.GONE);
    }

    void navigateToReadFragment() {
        Timber.d("go to read. Instance: %s", this);
        navigationListener.navigateToReadFromTagFragment();
    }

    void navigateToWriteToTagFragment() {
        Timber.d("go to write. Instance: %s", this);
        navigationListener.navigateToSetEmployeeIdFragment();
    }

    void navigateToFormatTagFragment() {
        navigationListener.navigateToFormatTagFragment();
    }

}
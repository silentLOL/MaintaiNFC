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
import at.stefanirndorfer.maintainfc.databinding.SummaryFragmentBinding;
import timber.log.Timber;

public class SummaryFragment extends BaseFragment {


    public static SummaryFragment newInstance() {
        return new SummaryFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume()");
    }

    @Override
    ViewDataBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.summary_fragment, container, false);
    }

    @Override
    void onCreateSetupViewModel(ViewDataBinding binding) {
        SummaryViewModel summaryViewModel = new ViewModelProvider(requireActivity()).get(SummaryViewModel.class);
        ((SummaryFragmentBinding) binding).setViewModel(summaryViewModel);

        summaryViewModel.nextButtonClicked.observe(this, data -> {
            Timber.d("next button clicked");
            navigateForward();
        });
    }

    @Override
    void setNFCReadingAllowed() {
        navigationListener.isNFCReadingAllowed(false);
    }

    @Override
    void setShowHomeButton() {
        navigationListener.showHomeButton();
    }

    @Override
    void setResultsFragmentVisibility() {
        navigationListener.setResultsFragmentVisibility(View.VISIBLE);
    }

    public void navigateForward() {
        navigationListener.navigateToMain();
    }

}
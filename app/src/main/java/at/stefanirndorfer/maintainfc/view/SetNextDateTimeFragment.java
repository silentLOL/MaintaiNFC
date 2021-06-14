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
import at.stefanirndorfer.maintainfc.databinding.SetNextDateTimeFragmentBinding;
import at.stefanirndorfer.maintainfc.model.NextDateTimeEvaluation;
import at.stefanirndorfer.maintainfc.util.DateTimePickerUtil;
import at.stefanirndorfer.maintainfc.viewmodel.ResultsViewModel;
import at.stefanirndorfer.maintainfc.viewmodel.SetNextDateTimeViewModel;
import timber.log.Timber;

public class SetNextDateTimeFragment extends BaseFragment {


    public static SetNextDateTimeFragment newInstance() {
        return new SetNextDateTimeFragment();
    }


    @Override
    ViewDataBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.set_next_date_time_fragment, container, false);
    }

    @Override
    void onCreateSetupViewModel(ViewDataBinding binding) {
        SetNextDateTimeViewModel setNextDateTimeViewModel = new ViewModelProvider(requireActivity()).get(SetNextDateTimeViewModel.class);
        ((SetNextDateTimeFragmentBinding) binding).setViewModel(setNextDateTimeViewModel);

        setNextDateTimeViewModel.initDateTimeData();

        setNextDateTimeViewModel.setNextDateTimeButtonPressed.observe(this, pressed -> {
            if (pressed) {
                DateTimePickerUtil.setDateTime(this.getContext(), setNextDateTimeViewModel);
            }
        });

        setNextDateTimeViewModel.triggerDataEvaluation.observe(this, evaluate -> {
            ResultsViewModel model = new ViewModelProvider(requireActivity()).get(ResultsViewModel.class);
            boolean isValidInput = model.validateNextDateTimeInput(setNextDateTimeViewModel.getCalendar()) == NextDateTimeEvaluation.OK;
            if (isValidInput) {
                setNextDateTimeViewModel.isNextButtonAvailable.setValue(true);
                model.setNextDateAndTimeCalendar(setNextDateTimeViewModel.getCalendar());
            }
            // todo: define trouble shooting
        });

        setNextDateTimeViewModel.nextButtonPressed.observe(this, pressed -> {
            Timber.d("Next button pressed");
            navigateForward();
        });
    }

    @Override
    void setNFCReadingAllowed() {
        navigationListener.isNFCReadingAllowed(false); /* we only want to write while this fragment is in foreground */
    }

    @Override
    void setShowHomeButton() {
        navigationListener.showHomeButton();
    }

    @Override
    void setResultsFragmentVisibility() {
        navigationListener.setResultsFragmentVisibility(View.VISIBLE);
    }

    @Override
    void setToolbarTitle() {
        getActivity().setTitle(R.string.next_date_toolbar_title);
    }


    public void navigateForward() {
        navigationListener.navigateToSetCommentFragment();
    }
}
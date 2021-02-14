package at.stefanirndorfer.maintainfc.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.databinding.SetDateTimeFragmentBinding;
import at.stefanirndorfer.maintainfc.model.DateTimeEvaluation;
import at.stefanirndorfer.maintainfc.viewmodel.ResultsViewModel;
import at.stefanirndorfer.maintainfc.viewmodel.SetDateTimeViewModel;
import timber.log.Timber;

public class SetDateTimeFragment extends BaseFragment {

    public static SetDateTimeFragment newInstance() {
        return new SetDateTimeFragment();
    }

    @Override
    ViewDataBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.set_date_time_fragment, container, false);
    }

    @Override
    void onCreateSetupViewModel(ViewDataBinding binding) {
        SetDateTimeViewModel setDateTimeViewModel = new ViewModelProvider(requireActivity()).get(SetDateTimeViewModel.class);
        ((SetDateTimeFragmentBinding) binding).setViewModel(setDateTimeViewModel);

        setDateTimeViewModel.initDateTimeData();

        setDateTimeViewModel.setDateTimeButtonPressed.observe(this, pressed -> {
            if (pressed) {
                setDateTime(setDateTimeViewModel);
            }
        });

        setDateTimeViewModel.triggerDataEvaluation.observe(this, evaluate -> {
            ResultsViewModel model = new ViewModelProvider(requireActivity()).get(ResultsViewModel.class);
            boolean isValidInput = model.validateDateTimeInput(setDateTimeViewModel.getCalendar()) == DateTimeEvaluation.OK;
            if (isValidInput) {
                setDateTimeViewModel.isNextButtonAvailable.setValue(true);
                model.setDateAndTimeCalendar(setDateTimeViewModel.getCalendar());
            }
        });

        setDateTimeViewModel.nextButtonPressed.observe(this, pressed -> {
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

    public void setDateTime(SetDateTimeViewModel setDateTimeViewModel) {
        Timber.d("user about to set date and time");
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(this.getContext()), (view, mYear, mMonth, mDay) -> {
            setDateTimeViewModel.setDateData(mYear, mMonth, mDay);
            showTimePicker(setDateTimeViewModel);
        },
                // the initial values
                setDateTimeViewModel.getYear(),
                setDateTimeViewModel.getMonth(),
                setDateTimeViewModel.getDay());

        datePickerDialog.show();
    }

    private void showTimePicker(SetDateTimeViewModel setDateTimeViewModel) {
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this.getContext(),
                (view, hourOfDay, minute) -> {
                    setDateTimeViewModel.setTimeData(hourOfDay, minute);
                    setDateTimeViewModel.triggerDataEvaluation();
                },
                // initial time values
                setDateTimeViewModel.getHour(),
                setDateTimeViewModel.getMinute(),
                true);

        timePickerDialog.show();
    }

    public void navigateForward() {
        navigationListener.navigateToSetNextDateTimeFragment();
    }
}
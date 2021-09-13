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
import at.stefanirndorfer.maintainfc.databinding.SetEmployeeIdFragmentBinding;
import at.stefanirndorfer.maintainfc.model.EmployeeIdEvaluation;
import at.stefanirndorfer.maintainfc.viewmodel.ResultsViewModel;
import at.stefanirndorfer.maintainfc.viewmodel.SetEmployeeIdViewModel;
import timber.log.Timber;

public class SetEmployeeIdFragment extends BaseFragment {

    public static SetEmployeeIdFragment newInstance() {
        return new SetEmployeeIdFragment();
    }


    @Override
    ViewDataBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.set_employee_id_fragment, container, false);
    }

    @Override
    void onCreateSetupViewModel(ViewDataBinding binding) {
        SetEmployeeIdViewModel setEmployeeIdViewModel = new ViewModelProvider(requireActivity()).get(SetEmployeeIdViewModel.class);
        ((SetEmployeeIdFragmentBinding) binding).setViewModel(setEmployeeIdViewModel);

        setEmployeeIdViewModel.employeeId.observe(this, data -> {
            Timber.d("employeeId is set to: %s", data);
            ResultsViewModel model = new ViewModelProvider(requireActivity()).get(ResultsViewModel.class);
            model.employeeId.setValue(data);
            if (model.validateEmployeeIdInput() == EmployeeIdEvaluation.OK) {
                setEmployeeIdViewModel.isNextButtonAvailable.setValue(true);
            }
            if (model.validateEmployeeIdInput() == EmployeeIdEvaluation.EMPTY) {
                setEmployeeIdViewModel.isNextButtonAvailable.setValue(false);
            }
        });

        setEmployeeIdViewModel.nextButtonPressed.observe(this, employeeId -> {
            Timber.d("next button is pressed, moving forward with employeeId: %s", employeeId);
            navigateForward();
        });
    }


    @Override
    void setNFCReadingAllowed() {
        navigationListener.isNFCReadingAllowed(false); /* we only want to write while this fragment is in foreground */
    }

    @Override
    void setShowHomeButton() {
        Timber.d("%s is showing home button", this);
        navigationListener.showHomeButton();
    }

    @Override
    void setResultsFragmentVisibility() {
        navigationListener.setResultsFragmentVisibility(View.VISIBLE);
    }

    @Override
    void setToolbarTitle() {
        getActivity().setTitle(R.string.employee_id_toolbar_title);
    }

    public void navigateForward() {
        navigationListener.navigateToSetNextDateTimeFragment();
    }

}
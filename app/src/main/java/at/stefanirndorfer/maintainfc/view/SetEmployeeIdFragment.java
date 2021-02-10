package at.stefanirndorfer.maintainfc.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.databinding.SetEmployeeIdFragmentBinding;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.viewmodel.SetEmployeeIdViewModel;
import timber.log.Timber;

public class SetEmployeeIdFragment extends BaseFragment {

    //https://www.journaldev.com/22561/android-mvvm-livedata-data-binding
    public static SetEmployeeIdFragment newInstance() {
        return new SetEmployeeIdFragment();
    }


    @Override
    View onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_employee_id_fragment, container, false);

        SetEmployeeIdFragmentBinding binding = DataBindingUtil.setContentView(getActivity(), R.layout.set_employee_id_fragment);

        SetEmployeeIdViewModel setEmployeeIdViewModel = new ViewModelProvider(this).get(SetEmployeeIdViewModel.class);
        binding.setViewModel(setEmployeeIdViewModel);
        binding.setLifecycleOwner(this);

        setEmployeeIdViewModel.employeeId.observe(this, data -> {
            Timber.d("employeeId is set to: %s", data);
            Integer input = 0;
            if (!TextUtils.isEmpty(data)) {
                input = Integer.parseInt(data);
            }
            setEmployeeIdViewModel.onEmployeeIdInputReceived(input);
        });

        setEmployeeIdViewModel.nextButtonPressed.observe(this, employeeId -> {
            Timber.d("next button is pressed, moving forward with employeeId: %s", employeeId);
            navigateForward(new MaintenanceData(employeeId, null, null, null));
        });

        return view;
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
    public void onResume() {
        super.onResume();
        Timber.d("onResume");
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            navigationListener = (NavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NavigationListener");
        }
    }

    public void navigateForward(MaintenanceData data) {
        navigationListener.navigateToSetDateTimeFragment(data);
    }

}
package at.stefanirndorfer.maintainfc.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.viewmodel.SetEmployeeIdViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SetEmployeeIdFragment extends Fragment implements SetEmployeeIdViewModel.SetEmployeeIdViewModelListener {

    private SetEmployeeIdViewModel mViewModel;
    private NavigationListener navigationListener;
    @BindView(R.id.employee_id_next_bt)
    Button nextButton;
    @BindView(R.id.employee_id_et)
    EditText employeeIdInput;
    private int input;

    public static SetEmployeeIdFragment newInstance() {
        return new SetEmployeeIdFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navigationListener.showHomeButton();
        navigationListener.isNFCReadingAllowed(false); /* we only want to write while this fragment is in foreground */
        View view = inflater.inflate(R.layout.set_employee_id_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");
        employeeIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processInput(s);
            }
        });
    }

    private void processInput(Editable s) {
        if (s.length() > 0) {
            nextButton.setEnabled(true);
            input = Integer.parseInt(String.valueOf(s));
        } else {
            nextButton.setEnabled(false);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SetEmployeeIdViewModel.class);
        mViewModel.setListener(this);
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

    @OnClick(R.id.employee_id_next_bt)
    public void navigateForward() {
        navigationListener.navigateToSetDateTimeFragment(new MaintenanceData(input, null, null, null));
    }

}
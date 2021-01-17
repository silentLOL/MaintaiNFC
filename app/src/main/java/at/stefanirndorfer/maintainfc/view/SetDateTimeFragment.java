package at.stefanirndorfer.maintainfc.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.viewmodel.SetDateTimeViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SetDateTimeFragment extends Fragment implements SetDateTimeViewModel.SetDateTimeViewModelListener {

    private static final String EMPLOYEE_ID_KEY = "EMPLOYEE_ID_KEY";
    private SetDateTimeViewModel mViewModel;
    private NavigationListener navigationListener;
    @BindView(R.id.employee_id_result)
    TextView employeeIdTextView;

    @BindView(R.id.date_time_next_bt)
    Button nextButton;

    @BindView(R.id.set_date_time_bt)
    Button setDateTimeButton;

    @BindView(R.id.date_time_result_container)
    LinearLayout dateTimeResultContainer;

    @BindView(R.id.date_result)
    TextView dateResult;

    @BindView(R.id.time_result)
    TextView timeResult;

    private int empId;
    private long dateTimeInMillis;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    public static SetDateTimeFragment newInstance(int employeeId) {
        SetDateTimeFragment setDateTimeFragment = new SetDateTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EMPLOYEE_ID_KEY, employeeId);
        setDateTimeFragment.setArguments(bundle);
        return setDateTimeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navigationListener.showHomeButton();
        navigationListener.isNFCReadingAllowed(false); /* we only want to write while this fragment is in foreground */
        View view = inflater.inflate(R.layout.set_date_time_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SetDateTimeViewModel.class);
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

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");
        empId = getArguments().getInt(EMPLOYEE_ID_KEY);
        employeeIdTextView.setText(String.valueOf(empId));
        nextButton.setEnabled(false);
    }

    @OnClick(R.id.set_date_time_bt)
    public void setDateTime() {
        Timber.d("user about to set date and time");
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.getContext(), (view, mYear, mMonth, mDay) -> {
            dateResult.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
            this.mDay = mDay;
            this.mMonth = mMonth;
            this.mYear = mYear;
            showTimePicker();
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this.getContext(),
                (view, hourOfDay, minute) -> {
                    timeResult.setText(hourOfDay + ":" + minute);
                    this.mHour = hourOfDay;
                    this.mMinute = minute;
                    setResultsVisilbleAndEnableNavigation();
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void setResultsVisilbleAndEnableNavigation() {
        dateTimeResultContainer.setVisibility(View.VISIBLE);
        nextButton.setEnabled(true);
    }


    @OnClick(R.id.date_time_next_bt)
    public void navigateForward() {
        Calendar calendar = assembleCalendar();
        dateTimeInMillis = calendar.getTimeInMillis();
        navigationListener.navigateToSetCommentFragment(dateTimeInMillis, empId);
    }

    private Calendar assembleCalendar() {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(mYear, mMonth, mDay, mHour, mMinute);
        Timber.d("calender is set to: " + new Date(c.getTimeInMillis()).toString());
        return c;
    }
}
package at.stefanirndorfer.maintainfc.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.util.CalendarUtils;
import at.stefanirndorfer.maintainfc.viewmodel.SetDateTimeViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SetDateTimeFragment extends Fragment implements SetDateTimeViewModel.SetDateTimeViewModelListener {

    private static final String MAINTENANCE_DATA_KEY = "MAINTENANCE_DATA_KEY";
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

    private long dateTimeInMillis;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private MaintenanceData maintenanceData;

    public static SetDateTimeFragment newInstance(MaintenanceData maintenanceData) {
        SetDateTimeFragment setDateTimeFragment = new SetDateTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MAINTENANCE_DATA_KEY, maintenanceData);
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
        maintenanceData = getArguments().getParcelable(MAINTENANCE_DATA_KEY);
        employeeIdTextView.setText(String.valueOf(maintenanceData.getEmployeeId()));
        nextButton.setEnabled(false);
    }

    @OnClick(R.id.set_date_time_bt)
    public void setDateTime() {
        Timber.d("user about to set date and time");
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.getContext(), (view, mYear, mMonth, mDay) -> {
            dateResult.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
            this.day = mDay;
            this.month = mMonth;
            this.year = mYear;
            showTimePicker();
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this.getContext(),
                (view, hourOfDay, minute) -> {
                    timeResult.setText(hourOfDay + ":" + minute);
                    this.hour = hourOfDay;
                    this.minute = minute;
                    setResultsVisibleAndEnableNavigation();
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void setResultsVisibleAndEnableNavigation() {
        dateTimeResultContainer.setVisibility(View.VISIBLE);
        nextButton.setEnabled(true);
    }

    @OnClick(R.id.date_time_next_bt)
    public void navigateForward() {
        Calendar calendar = CalendarUtils.assembleCalendar(year, month, day, hour, minute);
        dateTimeInMillis = calendar.getTimeInMillis();
        maintenanceData.setTimestamp(dateTimeInMillis);
        navigationListener.navigateToSetNextDateTimeFragment(maintenanceData);
    }
}
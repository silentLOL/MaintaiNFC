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
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.util.CalendarUtils;
import at.stefanirndorfer.maintainfc.viewmodel.SetNextDateTimeViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SetNextDateTimeFragment extends Fragment implements SetNextDateTimeViewModel.SetNextDateTimeViewModelListener {

    private static final String MAINTENANCE_DATA_KEY = "MAINTENANCE_DATA_KEY";
    private SetNextDateTimeViewModel mViewModel;
    private NavigationListener navigationListener;

    @BindView(R.id.employee_id_result)
    TextView employeeIdTextView;

    @BindView(R.id.this_date_time_result)
    TextView thisDateTimeResult;

    @BindView(R.id.next_date_time_next_bt)
    Button nextButton;

    @BindView(R.id.set_next_date_time_bt)
    Button setNextDateTimeButton;

    @BindView(R.id.next_date_time_result_container)
    LinearLayout nextDateTimeResultContainer;

    @BindView(R.id.date_result)
    TextView nextDateResult;

    @BindView(R.id.time_result)
    TextView nextTimeResult;

    private long dateTimeInMillis;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private MaintenanceData maintenanceData;

    public static SetNextDateTimeFragment newInstance(MaintenanceData maintenanceData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MAINTENANCE_DATA_KEY, maintenanceData);
        SetNextDateTimeFragment setNextDateTimeFragment = new SetNextDateTimeFragment();
        setNextDateTimeFragment.setArguments(bundle);
        return setNextDateTimeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navigationListener.showHomeButton();
        navigationListener.isNFCReadingAllowed(false); /* we only want to write while this fragment is in foreground */
        View view = inflater.inflate(R.layout.set_next_date_time_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SetNextDateTimeViewModel.class);
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
        thisDateTimeResult.setText(new Date(maintenanceData.getTimestamp()).toString());
        nextButton.setEnabled(false);
    }

    @OnClick(R.id.set_next_date_time_bt)
    public void setNextDateTime(){
        Timber.d("user about to set next date and time");
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.getContext(), (view, mYear, mMonth, mDay) -> {
            nextDateResult.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
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
                    nextTimeResult.setText(hourOfDay + ":" + minute);
                    this.hour = hourOfDay;
                    this.minute = minute;
                    setResultsVisibleAndEnableNavigation();
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void setResultsVisibleAndEnableNavigation() {
        nextDateTimeResultContainer.setVisibility(View.VISIBLE);
        nextButton.setEnabled(true);
    }

    @OnClick(R.id.next_date_time_next_bt)
    public void navigateForward() {
        Calendar calendar = CalendarUtils.assembleCalendar(year, month, day, hour, minute);
        dateTimeInMillis = calendar.getTimeInMillis();
        maintenanceData.setNextTimestamp(dateTimeInMillis);
        navigationListener.navigateToSetCommentFragment(maintenanceData);
    }


}
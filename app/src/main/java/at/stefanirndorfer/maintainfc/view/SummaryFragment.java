package at.stefanirndorfer.maintainfc.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.nfc.FormatException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.viewmodel.WriteToTagViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SummaryFragment extends Fragment {

    private static final String MILLIS_KEY = "MILLIS";
    private static final String EMP_ID_KEY = "EMP_ID_KEY";
    private static final String COMMENT_KEY = "COMMENT_KEY";
    private SummaryViewModel mViewModel;
    private NavigationListener navigationListener;
    private long millis;
    private String comment;
    private int empId;

    @BindView(R.id.employee_id_result)
    TextView employeeTextView;

    @BindView(R.id.date_and_time_result)
    TextView dateTimeTextView;

    @BindView(R.id.comment_result)
    TextView commentResult;

    @BindView(R.id.summary_next_bt)
    Button summaryNextButton;




    public static SummaryFragment newInstance(Bundle arguments) {
        SummaryFragment summaryFragment = new SummaryFragment();
        summaryFragment.setArguments(arguments);
        return summaryFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navigationListener.showHomeButton();
        navigationListener.isNFCReadingAllowed(false); /* we only want to write while this fragment is in foreground */
        View view = inflater.inflate(R.layout.summary_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        // TODO: Use the ViewModel
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
        Timber.d("onResume()");
        millis = getArguments().getLong(MILLIS_KEY);
        comment = getArguments().getString(COMMENT_KEY);
        empId = getArguments().getInt(EMP_ID_KEY);
        employeeTextView.setText(String.valueOf(empId));
        commentResult.setText(comment);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = new Date(millis);
        dateTimeTextView.setText(date.toString());
    }

    @OnClick(R.id.summary_next_bt)
    public void navigateForward(){
        navigationListener.navigateToMain();
    }

}
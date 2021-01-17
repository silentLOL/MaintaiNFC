package at.stefanirndorfer.maintainfc.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetCommentFragment extends Fragment {

    private static final String MILLIS_KEY = "MILLIS";
    private static final String EMP_ID_KEY = "EMP_ID_KEY";

    private SetCommentViewModel viewModel;
    private NavigationListener navigationListener;

    @BindView(R.id.employee_id_result)
    TextView employeeIdTextView;

    @BindView(R.id.comment_next_bt)
    Button nextButton;

    @BindView(R.id.add_comment_tv)
    TextView addCommentTextView;

    @BindView(R.id.date_and_time_result)
    TextView dateAndTimeResult;
    private String comment;
    private long millis;
    private int empId;

    public static SetCommentFragment newInstance(long millis, int empId) {
        SetCommentFragment setCommentFragment = new SetCommentFragment();
        Bundle args = new Bundle();
        args.putLong(MILLIS_KEY, millis);
        args.putInt(EMP_ID_KEY, empId);
        setCommentFragment.setArguments(args);
        return setCommentFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navigationListener.showHomeButton();
        navigationListener.isNFCReadingAllowed(false); /* we only want to write while this fragment is in foreground */
        View view = inflater.inflate(R.layout.set_comment_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SetCommentViewModel.class);
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
        empId = getArguments().getInt(EMP_ID_KEY);
        employeeIdTextView.setText(String.valueOf(empId));
        millis = getArguments().getLong(MILLIS_KEY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = new Date(millis);
        dateAndTimeResult.setText(date.toString());
        addTextWatcherToCommentInput();
    }

    private void addTextWatcherToCommentInput() {
        addCommentTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //todo check for max length
                comment = s.toString();
            }
        });
    }

    @OnClick(R.id.comment_next_bt)
    public void navigateForward(){
        navigationListener.navigateToWriteToTagFragment(millis, empId, comment);
    }
}
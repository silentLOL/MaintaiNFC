package at.stefanirndorfer.maintainfc.view;

import android.content.Context;
import android.nfc.FormatException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.viewmodel.WriteToTagViewModel;
import timber.log.Timber;

public class WriteToTagFragment extends Fragment implements WriteToTagViewModel.WriteToTagViewModelListener {

    private static final String MILLIS_KEY = "MILLIS";
    private static final String EMP_ID_KEY = "EMP_ID_KEY";
    private static final String COMMENT_KEY = "COMMENT_KEY";
    private WriteToTagViewModel viewModel;
    private NavigationListener navigationListener;
    private long millis;
    private String comment;
    private int empId;

    public static WriteToTagFragment newInstance(long millis, int empId, String comment) {
        WriteToTagFragment writeToTagFragment = new WriteToTagFragment();
        Bundle args = new Bundle();
        args.putLong(MILLIS_KEY, millis);
        args.putInt(EMP_ID_KEY, empId);
        args.putString(COMMENT_KEY, comment);
        writeToTagFragment.setArguments(args);
        return writeToTagFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navigationListener.showHomeButton();
        navigationListener.isNFCReadingAllowed(false); /* we only want to write while this fragment is in foreground */
        return inflater.inflate(R.layout.write_to_tag_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WriteToTagViewModel.class);
        viewModel.setListener(this);
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

        try {
            viewModel.write(millis,empId,comment,navigationListener.getTag());
        } catch (IOException | FormatException e) {
            Timber.e(e);
        }
    }

    @Override
    public void successfullyWrittenToTag(boolean isSuccess) {

    }
}
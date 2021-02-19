package at.stefanirndorfer.maintainfc.view;

import android.content.Context;
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
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.viewmodel.WriteToTagViewModel;
import timber.log.Timber;

public class WriteToTagFragment extends Fragment implements WriteToTagViewModel.WriteToTagViewModelListener {

    private static final String MAINT_DATA_KEY = "MAINT_DATA";
    private WriteToTagViewModel viewModel;
    private NavigationListener navigationListener;
    private MaintenanceData maintenanceData;

    public static WriteToTagFragment newInstance() {
        return new WriteToTagFragment();
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
        maintenanceData = getArguments().getParcelable(MAINT_DATA_KEY);

        try {
            viewModel.write(maintenanceData, navigationListener.getNFCTag());
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    @Override
    public void successfullyWrittenToTag(boolean isSuccess) {
        if (isSuccess) {
            Timber.d("write success");
            navigationListener.navigateToSummaryFragment(getArguments());
        } else {

        }
    }
}
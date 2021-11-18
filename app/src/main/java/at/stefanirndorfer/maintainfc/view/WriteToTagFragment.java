package at.stefanirndorfer.maintainfc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.st.st25sdk.NFCTag;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.databinding.WriteToTagFragmentBinding;
import at.stefanirndorfer.maintainfc.model.MaintenanceData;
import at.stefanirndorfer.maintainfc.model.WriteToTagResult;
import at.stefanirndorfer.maintainfc.viewmodel.ResultsViewModel;
import at.stefanirndorfer.maintainfc.viewmodel.WriteToTagViewModel;
import timber.log.Timber;

import static android.widget.Toast.LENGTH_LONG;

public class WriteToTagFragment extends BaseFragment {


    public static WriteToTagFragment newInstance() {
        return new WriteToTagFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume()");

    }

    @Override
    ViewDataBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.write_to_tag_fragment, container, false);
    }

    @Override
    void onCreateSetupViewModel(ViewDataBinding binding) {
        WriteToTagViewModel writeToTagViewModel = new ViewModelProvider(requireActivity()).get(WriteToTagViewModel.class);
        ((WriteToTagFragmentBinding) binding).setViewModel(writeToTagViewModel);
        writeToTagViewModel.writingResult.observe(this, result -> {
            Timber.d("result updated: %s", result.name());
            if (result.equals(WriteToTagResult.FAIL)) {
                //todo better error-handling
                Toast.makeText(this.getContext(), getString(R.string.write_to_tag_error), LENGTH_LONG).show();
                return;
            }
            if (result.equals(WriteToTagResult.INVALID_TAG_TYPE)) {
                Toast.makeText(this.getContext(), getString(R.string.invalid_tag_type_error), LENGTH_LONG).show();
                return;
            }
            if (result.equals(WriteToTagResult.FAIL_INVALID_PASSWORD)) {
                Toast.makeText(this.getContext(), getString(R.string.invalid_password_type_error), LENGTH_LONG).show();
                return;
            }
            if (result.equals(WriteToTagResult.SUCCESS) || result.equals(WriteToTagResult.FORMATTED)) {
                navigateForward();
            }
        });
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
    void setResultsFragmentVisibility() {
        navigationListener.setResultsFragmentVisibility(View.GONE);
    }

    @Override
    void setToolbarTitle() {
        getActivity().setTitle(R.string.write_toolbar_title);
    }

    public void navigateForward() {
        Timber.d("write success -- we are navigating forward");
        navigationListener.navigateToSummaryFragment();
    }

    public void setNFCTagToWriteOn(NFCTag myTag) {
        Timber.d("finally received our tag");
        ResultsViewModel model = new ViewModelProvider(requireActivity()).get(ResultsViewModel.class);
        WriteToTagViewModel writeToTagViewModel = new ViewModelProvider(requireActivity()).get(WriteToTagViewModel.class);
        MaintenanceData maintenanceData = model.getMaintenanceData();
        if (maintenanceData == null) {
            Toast.makeText(this.getContext(), getString(R.string.data_incomplete_error_msg), LENGTH_LONG).show();
            throw new IllegalArgumentException("Data area incomplete");
        }
        new Thread(() -> {
            try {
                writeToTagViewModel.write(maintenanceData, myTag);
            } catch (IOException e) {
                Timber.e(e);
            }
        }).start();
    }
}
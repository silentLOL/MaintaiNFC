package at.stefanirndorfer.maintainfc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.st.st25sdk.NFCTag;
import com.st.st25sdk.ndef.NDEFMsg;
import com.st.st25sdk.ndef.NDEFRecord;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.databinding.ReadFromTagFragmentBinding;
import at.stefanirndorfer.maintainfc.model.ReadFromTagResult;
import at.stefanirndorfer.maintainfc.viewmodel.ReadFromTagViewModel;
import at.stefanirndorfer.maintainfc.viewmodel.ResultsViewModel;
import timber.log.Timber;

import static android.widget.Toast.LENGTH_LONG;

public class ReadFromTagFragment extends BaseFragment {

    public static final String RAW_MESSAGE_KEY = "RAW_MESSAGE_KEY";

    public static ReadFromTagFragment newInstance(NDEFMsg msg) {
        ReadFromTagFragment fragment = new ReadFromTagFragment();
        Bundle args = new Bundle();
        args.putSerializable(RAW_MESSAGE_KEY, msg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    ViewDataBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.read_from_tag_fragment, container, false);
    }

    @Override
    void onCreateSetupViewModel(ViewDataBinding binding) {
        ReadFromTagViewModel readFromTagViewModel = new ViewModelProvider(requireActivity()).get(ReadFromTagViewModel.class);
        ((ReadFromTagFragmentBinding) binding).setViewModel(readFromTagViewModel);
        showDataFromArgsIfGiven(readFromTagViewModel);
        //NFCTag nfcTag = navigationListener.getNFCTag();
        //getTranslationFromViewModel(nfcTag, readFromTagViewModel);
        ResultsViewModel model = new ViewModelProvider(requireActivity()).get(ResultsViewModel.class);

        // lets clear the data at the beginning
        model.clearData();
        readFromTagViewModel.isNextButtonAvailable.setValue(false);

        // indicates the outcome of the reading operation
        readFromTagViewModel.readingResult.observe(this, data -> {
            if (data.equals(ReadFromTagResult.FAIL)) {
                Toast.makeText(this.getContext(), R.string.failed_to_read_tag_msg, LENGTH_LONG).show();
                readFromTagViewModel.isNextButtonAvailable.setValue(false);
            }
            if (data.equals(ReadFromTagResult.EMPTY)) {
                Toast.makeText(this.getContext(), R.string.tag_is_empty__msg, LENGTH_LONG).show();
                readFromTagViewModel.isNextButtonAvailable.setValue(true);
            }
            if (data.equals(ReadFromTagResult.SUCCESS)) {
                readFromTagViewModel.isNextButtonAvailable.setValue(true);
            }
        });

        readFromTagViewModel.resultData.observe(this, maintenanceData -> {
            Timber.d("received maintenance data");
            model.setMaintenanceData(maintenanceData);
            navigationListener.setResultsFragmentVisibility(View.VISIBLE);
        });

        readFromTagViewModel.okButtonClicked.observe(this, clicked -> {
            Timber.d("Forward navigation clicked");
            model.clearData();
            readFromTagViewModel.isNextButtonAvailable.setValue(false);
            navigationListener.navigateToMain();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showDataFromArgsIfGiven(ReadFromTagViewModel readFromTagViewModel) {
        assert getArguments() != null;
        if (getArguments().getSerializable(RAW_MESSAGE_KEY) == null) {
            Timber.d("no rawMessage set yet");
            return;
        }
        getTranslationFromViewModel(getArguments().getSerializable(RAW_MESSAGE_KEY), readFromTagViewModel);
    }

    private void getTranslationFromViewModel(Serializable msg, ReadFromTagViewModel readFromTagViewModel) {
        Timber.d("setting tag to viewModel");
        readFromTagViewModel.processNewMessage(msg);
    }

    @Override
    void setNFCReadingAllowed() {
        navigationListener.isNFCReadingAllowed(true); /* we want to read while in foreground */
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
        getActivity().setTitle(R.string.read_tag_toolbar_title);
    }


    public void setTagData(byte[] bytes) {
        ReadFromTagViewModel readFromTagViewModel = new ViewModelProvider(requireActivity()).get(ReadFromTagViewModel.class);
        getTranslationFromViewModel(bytes, readFromTagViewModel);

    }
}
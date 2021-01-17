package at.stefanirndorfer.maintainfc.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.viewmodel.ReadFromTagViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ReadFromTagFragment extends Fragment implements ReadFromTagViewModel.ReadFromTagFragmentViewModelListener {

    public static final String RAW_MESSAGE_KEY = "RAW_MESSAGE_KEY";
    private ReadFromTagViewModel viewModel;
    private NavigationListener navigationListener;
    private Parcelable[] rawMessage;
    @BindView
            (R.id.nfc_content_tv)
    TextView nfcContentOutput;

    public static ReadFromTagFragment newInstance(Parcelable[] rawMessage) {
        ReadFromTagFragment fragment = new ReadFromTagFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(RAW_MESSAGE_KEY, rawMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        navigationListener.showHomeButton();
        if (getArguments().getParcelableArray(RAW_MESSAGE_KEY) != null) {
            Timber.d("setting rawMessage from arguments");
            rawMessage = getArguments().getParcelableArray(RAW_MESSAGE_KEY);
        }
        View view = inflater.inflate(R.layout.read_from_tag_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ReadFromTagViewModel.class);
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
        Timber.d("onResume");
        if (rawMessage != null) {
            Timber.d("setting rawMessage to viewModel");
            viewModel.processNewMessage(rawMessage);
        }
    }

    public void setRawMessage(Parcelable[] rawMessage) {
        this.rawMessage = rawMessage;
        viewModel.processNewMessage(rawMessage);
    }

    /////////////////////////////////////////////////////
    // viewModel Listener
    /////////////////////////////////////////////////////
    @Override
    public void setNFCContentText(String nfcContent) {
        nfcContentOutput.setText(nfcContent);
    }
}
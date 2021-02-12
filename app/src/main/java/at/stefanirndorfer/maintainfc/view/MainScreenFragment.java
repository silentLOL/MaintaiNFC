package at.stefanirndorfer.maintainfc.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.viewmodel.MainScreenViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainScreenFragment extends Fragment {

    @BindView(R.id.read_tag_bt)
    Button navToReadButton;
    @BindView(R.id.start_write_flow_bt)
    Button navToWriteFlowButton;
    @BindView(R.id.format_tag_bt)
    Button navToFormatButton;

    private MainScreenViewModel mViewModel;
    private NavigationListener navigationListener;

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navigationListener.hideHomeButton();
        navigationListener.isNFCReadingAllowed(true); /* we want to read while in foreground */
        navigationListener.setResultsFragmentVisibility(View.GONE);
        View view = inflater.inflate(R.layout.main_screen_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainScreenViewModel.class);
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

    @OnClick(R.id.read_tag_bt)
    void navigateToReadFragment() {
        navigationListener.navigateToReadFromTagFragment();
    }

    @OnClick(R.id.start_write_flow_bt)
    void navigateToWriteToTagFragment() {
        navigationListener.navigateToSetEmployeeIdFragment();
    }

    @OnClick(R.id.format_tag_bt)
    void navigateToFormatTagFragment() {
        navigationListener.navigateToFormatTagFragment();
    }

}
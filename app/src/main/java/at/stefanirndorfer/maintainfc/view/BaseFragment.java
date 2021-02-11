package at.stefanirndorfer.maintainfc.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.viewmodel.ResultsViewModel;
import timber.log.Timber;

public abstract class BaseFragment extends Fragment {
    protected NavigationListener navigationListener;
    protected ResultsViewModel resultsViewModel;


    /////////////////////////////////////////
    // lifecycle
    /////////////////////////////////////////

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            navigationListener = (NavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NavigationListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setShowHomeButton();
        setNFCReadingAllowed();
        ViewDataBinding viewDataBinding = onCreateViewBinding(inflater, container, savedInstanceState);
        viewDataBinding.setLifecycleOwner(this);
        onCreateSetupViewModel(viewDataBinding);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart");
        resultsViewModel = new ViewModelProvider(requireActivity()).get(ResultsViewModel.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop");
    }

    /////////////////////////////////////////
    // abstract methods
    /////////////////////////////////////////

    abstract ViewDataBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    abstract void onCreateSetupViewModel(ViewDataBinding binding);

    abstract void setNFCReadingAllowed();

    abstract void setShowHomeButton();

}

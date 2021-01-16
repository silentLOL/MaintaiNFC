package at.stefanirndorfer.maintainfc.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.viewmodel.ReadFromTagViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

public class ReadFromTagFragment extends Fragment {

    private ReadFromTagViewModel mViewModel;
    private NavigationListener navigationListener;

    public static ReadFromTagFragment newInstance() {
        return new ReadFromTagFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        navigationListener.showHomeButton();
        return inflater.inflate(R.layout.read_from_tag_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReadFromTagViewModel.class);
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

}
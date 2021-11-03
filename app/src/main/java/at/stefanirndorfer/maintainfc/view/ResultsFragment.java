package at.stefanirndorfer.maintainfc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.databinding.ResultsFragmentBinding;
import at.stefanirndorfer.maintainfc.viewmodel.ResultsViewModel;
import timber.log.Timber;

public class ResultsFragment extends Fragment {

    public static ResultsFragment newInstance() {
        return new ResultsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ResultsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.results_fragment, container, false);
        binding.setLifecycleOwner(this);
        onCreateSetupViewModel(binding);
        return binding.getRoot();
    }

    private void onCreateSetupViewModel(ResultsFragmentBinding binding) {
        ResultsViewModel viewModel = new ViewModelProvider(requireActivity()).get(ResultsViewModel.class);
        binding.setViewModel(viewModel);
//        viewModel.employeeId.observe(getViewLifecycleOwner(), employeeId -> {
//            Timber.d("employeeId is set to: %s", employeeId);
//        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
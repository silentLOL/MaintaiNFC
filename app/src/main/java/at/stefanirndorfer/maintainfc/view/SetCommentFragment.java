package at.stefanirndorfer.maintainfc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.databinding.SetCommentFragmentBinding;
import at.stefanirndorfer.maintainfc.model.CommentEvaluation;
import at.stefanirndorfer.maintainfc.viewmodel.ResultsViewModel;
import at.stefanirndorfer.maintainfc.viewmodel.SetCommentViewModel;
import timber.log.Timber;

public class SetCommentFragment extends BaseFragment {


    public static SetCommentFragment newInstance() {
        return new SetCommentFragment();
    }

    @Override
    ViewDataBinding onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.set_comment_fragment, container, false);
    }

    @Override
    void onCreateSetupViewModel(ViewDataBinding binding) {
        SetCommentViewModel setCommentViewModel = new ViewModelProvider(requireActivity()).get(SetCommentViewModel.class);
        ((SetCommentFragmentBinding) binding).setViewModel(setCommentViewModel);
        setCommentViewModel.comment.observe(this, comment -> {
            Timber.d("comment is set to: %s", comment);
            ResultsViewModel model = new ViewModelProvider(requireActivity()).get(ResultsViewModel.class);
            model.comment.setValue(comment);
            CommentEvaluation commentEvaluation = model.validateCommentInput(comment);
            if (commentEvaluation == CommentEvaluation.OK) {
                setCommentViewModel.isNextButtonAvailable.setValue(true);
                return;
            }
            if (commentEvaluation == CommentEvaluation.TOO_LONG) {
                setCommentViewModel.isNextButtonAvailable.setValue(false);
                ((SetCommentFragmentBinding) binding).commentInputLayout.setError(this.getString(R.string.comment_error_too_long));
            }
        });

        setCommentViewModel.nextButtonPressed.observe(this, comment -> {
            Timber.d("next button is pressed, moving forward with comment: %s", comment);
            navigateForward();
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
        navigationListener.setResultsFragmentVisibility(View.VISIBLE);
    }

    public void navigateForward() {
        navigationListener.navigateToWriteToTagFragment();
    }
}

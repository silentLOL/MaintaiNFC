package at.stefanirndorfer.maintainfc.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import at.stefanirndorfer.maintainfc.input.NavigationListener;

public abstract class BaseFragment extends Fragment {
    protected NavigationListener navigationListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setShowHomeButton();
        setNFCReadingAllowed();
        return onCreateViewBinding(inflater, container, savedInstanceState);
    }
    abstract View onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    abstract void setNFCReadingAllowed();
    abstract void setShowHomeButton();
}

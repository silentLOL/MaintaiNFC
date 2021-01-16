package at.stefanirndorfer.maintainfc.input;

import android.os.Parcelable;

public interface NavigationListener {
    void navigateToWriteToTagFragment();

    void navigateToReadFromTagFragment();

    void navigateToReadFromTagFragment(Parcelable[] rawMessage);

    void navigateToFormatTagFragment();

    void showHomeButton();

    void hideHomeButton();

    void writeModeOn();

    void writeModeOff();
}

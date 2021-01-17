package at.stefanirndorfer.maintainfc.input;

import android.nfc.Tag;
import android.os.Parcelable;

public interface NavigationListener {
    void navigateToSetEmployeeIdFragment();

    void navigateToSetDateTimeFragment(int employeeId);

    void navigateToSetCommentFragment(long dateTimeInMillis, int empId);

    void navigateToSummaryFragment();

    void navigateToWriteToTagFragment(long dateTimeInMillis, int empId, String comment);

    void navigateToReadFromTagFragment();

    void navigateToReadFromTagFragment(Parcelable[] rawMessage);

    void navigateToFormatTagFragment();

    void showHomeButton();

    void hideHomeButton();

    void isNFCReadingAllowed(boolean isNFCReadingAllowed);

    Tag getTag();
}

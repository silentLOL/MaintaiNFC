package at.stefanirndorfer.maintainfc.input;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;

public interface NavigationListener {
    void navigateToSetEmployeeIdFragment();

    void navigateToSetDateTimeFragment(int employeeId);

    void navigateToSetCommentFragment(long dateTimeInMillis, int empId);

    void navigateToSummaryFragment(Bundle arguments);

    void navigateToWriteToTagFragment(long dateTimeInMillis, int empId, String comment);

    void navigateToReadFromTagFragment();

    void navigateToReadFromTagFragment(Parcelable[] rawMessage);

    void navigateToFormatTagFragment();

    void navigateToMain();

    void showHomeButton();

    void hideHomeButton();

    void isNFCReadingAllowed(boolean isNFCReadingAllowed);

    /**
     * dodo move to a sedicated interface class -- does not fit here
     * @return
     */
    Tag getTag();

}

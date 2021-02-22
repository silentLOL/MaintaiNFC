package at.stefanirndorfer.maintainfc.input;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;

import at.stefanirndorfer.maintainfc.model.MaintenanceData;

public interface NavigationListener {
    void navigateToSetEmployeeIdFragment();

    void navigateToSetDateTimeFragment();

    void navigateToSetNextDateTimeFragment();

    void navigateToSetCommentFragment();

    void navigateToSummaryFragment();

    void navigateToWriteToTagFragment();

    void navigateToReadFromTagFragment();

    void navigateToReadFromTagFragment(Parcelable[] rawMessage);

    void navigateToFormatTagFragment();

    void navigateToMain();

    void showHomeButton();

    void hideHomeButton();

    void setResultsFragmentVisibility(int visibility);

    void isNFCReadingAllowed(boolean isNFCReadingAllowed);

    /**
     * todo move to a dedicated interface class -- does not fit here
     *
     * @return
     */
    Tag getNFCTag();

}

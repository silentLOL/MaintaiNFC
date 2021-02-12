package at.stefanirndorfer.maintainfc.input;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;

import at.stefanirndorfer.maintainfc.model.MaintenanceData;

public interface NavigationListener {
    void navigateToSetEmployeeIdFragment();

    void navigateToSetDateTimeFragment(MaintenanceData maintenanceData);

    void navigateToSetNextDateTimeFragment(MaintenanceData maintenanceData);

    void navigateToSetCommentFragment(MaintenanceData maintenanceData);

    void navigateToSummaryFragment(Bundle arguments);

    void navigateToWriteToTagFragment(MaintenanceData maintenanceData);

    void navigateToReadFromTagFragment();

    void navigateToReadFromTagFragment(Parcelable[] rawMessage);

    void navigateToFormatTagFragment();

    void navigateToMain();

    void showHomeButton();

    void hideHomeButton();

    void setResultsFragmentVisibility(int visibility);

    void isNFCReadingAllowed(boolean isNFCReadingAllowed);

    /**
     * dodo move to a sedicated interface class -- does not fit here
     * @return
     */
    Tag getTag();

}

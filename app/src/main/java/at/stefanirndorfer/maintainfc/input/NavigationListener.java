package at.stefanirndorfer.maintainfc.input;

import android.os.Parcelable;

import com.st.st25sdk.NFCTag;
import com.st.st25sdk.ndef.NDEFMsg;

import java.io.Serializable;

public interface NavigationListener {
    void navigateToSetEmployeeIdFragment();

    void navigateToSetDateTimeFragment();

    void navigateToSetNextDateTimeFragment();

    void navigateToSummaryFragment();

    void navigateToWriteToTagFragment();

    void navigateToReadFromTagFragment();

    void navigateToReadFromTagFragment(NDEFMsg msg);

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
    NFCTag getNFCTag();
}

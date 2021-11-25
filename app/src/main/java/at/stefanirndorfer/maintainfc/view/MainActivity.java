package at.stefanirndorfer.maintainfc.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.st.st25sdk.NFCTag;
import com.st.st25sdk.STException;
import com.st.st25sdk.TagHelper;
import com.st.st25sdk.ndef.NDEFMsg;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import at.stefanirndorfer.maintainfc.util.Constants;
import at.stefanirndorfer.maintainfc.util.TagDiscovery;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements NavigationListener, TagDiscovery.onTagDiscoveryCompletedListener {

    private FragmentManager fragmentManager;


    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    NFCTag myTag;

    // this determines if a particular fragment in foreground
    // allows to read nfc. E.g. if we want to write only we don't need to read at the same time
    // when approaching the tag.
    private boolean isNFCReadingAllowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            MainScreenFragment mainScreenFragment = MainScreenFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, mainScreenFragment)
                    .commit();
        }
        isNFCReadingAllowed = true; /* for the case the nfc reading was triggered from the outside */
        checkNFCSupport();

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};
    }

    ////////////////////////////////////
    // Lifecycle handling
    ////////////////////////////////////
    @Override
    public void onPause() {
        super.onPause();
        writeModeOff();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isNfCEnabled()) {
            // nfc is disabled
            Toast.makeText(this, R.string.nfc_currently_disabled, Toast.LENGTH_LONG).show();
        }
        writeModeOn();
        readFromIntent(getIntent());
    }

    private boolean isNfCEnabled() {
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            // adapter exists and is enabled.
            return true;
        }
        return false;
    }

    ////////////////////////////////////
    // Lifecycle handling end
    ////////////////////////////////////


    public void writeModeOn() {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }


    public void writeModeOff() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void checkNFCSupport() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, getString(R.string.device_does_not_support_nfc_msg), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Timber.d("onNewIntent %s", intent);
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            // If the resume was triggered by an NFC event, it will contain an EXTRA_TAG providing
            // the handle of the NFC Tag
            Tag androidTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (androidTag != null) {
                Toast.makeText(this, getString(R.string.msg_start_tag_discovery), Toast.LENGTH_SHORT).show();
                // This action will be done in an Asynchronous task.
                // onTagDiscoveryCompleted() of current activity is called when discovery is completed.
                Timber.d("starting tag discovery");
                startTagDiscovery(androidTag);
            }
        }

    }


    public void startTagDiscovery(Tag androidTag) {
        new TagDiscovery(this).execute(androidTag);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Timber.d("Option item home pressed");
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
            Timber.d("popping FragmentBackStack. Size: %s", getSupportFragmentManager().getFragments().size());
        }
    }


    //////////////////////////////////////////////
    // NavigationListener
    //////////////////////////////////////////////

    @Override
    public void navigateToSetEmployeeIdFragment() {
        Timber.d("navigating to SetEmployeeIdFragment");
        SetEmployeeIdFragment setEmployeeIdFragment = SetEmployeeIdFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, setEmployeeIdFragment, SetEmployeeIdFragment.class.getCanonicalName())
                .addToBackStack(SetEmployeeIdFragment.class.getCanonicalName())
                .commit();
    }

    @Override
    public void navigateToSetDateTimeFragment() {
        Timber.d("navigating to SetDateTimeFragment");
        SetDateTimeFragment setDateTimeFragment = SetDateTimeFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, setDateTimeFragment, SetDateTimeFragment.class.getCanonicalName())
                .addToBackStack(SetDateTimeFragment.class.getCanonicalName())
                .commit();
    }

    @Override
    public void navigateToSetNextDateTimeFragment() {
        Timber.d("navigating to SetDateTimeFragment");
        SetNextDateTimeFragment setNextDateTimeFragment = SetNextDateTimeFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, setNextDateTimeFragment, SetNextDateTimeFragment.class.getCanonicalName())
                .addToBackStack(SetNextDateTimeFragment.class.getCanonicalName())
                .commit();
    }


    @Override
    public void navigateToSummaryFragment() {
        Timber.d("navigating to SummaryFragment");
        SummaryFragment summaryFragment = SummaryFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, summaryFragment, SummaryFragment.class.getCanonicalName())
                .addToBackStack(SummaryFragment.class.getCanonicalName())
                .commit();
    }

    @Override
    public void navigateToWriteToTagFragment() {
        Timber.d("navigating to WriteToTagFragment");
        WriteToTagFragment writeToTagFragment = WriteToTagFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, writeToTagFragment, WriteToTagFragment.class.getCanonicalName())
                .addToBackStack(WriteToTagFragment.class.getCanonicalName())
                .commit();
    }

    @Override
    public void navigateToReadFromTagFragment() {
        Timber.d("navigating to ReadFromTagFragment");
        ReadFromTagFragment readFromTagFragment = ReadFromTagFragment.newInstance(null);
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, readFromTagFragment, ReadFromTagFragment.class.getCanonicalName())
                .addToBackStack(ReadFromTagFragment.class.getCanonicalName())
                .commit();
    }

    @Override
    public void navigateToReadFromTagFragment(NDEFMsg rawMessage) {
        Timber.d("navigating to ReadFromTagFragment with raw message");
        ReadFromTagFragment readFromTagFragment = ReadFromTagFragment.newInstance(rawMessage);
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, readFromTagFragment, ReadFromTagFragment.class.getCanonicalName())
                .addToBackStack(ReadFromTagFragment.class.getCanonicalName())
                .commit();
    }


    @Override
    public void navigateToFormatTagFragment() {
        Timber.d("navigating to FormatTagFragment");
        FormatTagFragment formatTagFragment = FormatTagFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, formatTagFragment, FormatTagFragment.class.getCanonicalName())
                .addToBackStack(FormatTagFragment.class.getCanonicalName())
                .commit();
    }

    @Override
    public void navigateToMain() {
        Timber.d("navigating to MainScreenFragment");
        MainScreenFragment mainScreenFragment = MainScreenFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, mainScreenFragment, MainScreenFragment.class.getCanonicalName())
                .addToBackStack(MainScreenFragment.class.getCanonicalName())
                .commit();
    }

    @Override
    public void showHomeButton() {
        Timber.d("showing back navigation button");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void hideHomeButton() {
        Timber.d("hiding back navigation button");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void setResultsFragmentVisibility(int visibility) {
        findViewById(R.id.result_fragment).setVisibility(visibility);
    }

    @Override
    public void isNFCReadingAllowed(boolean isNFCReadingAllowed) {
        this.isNFCReadingAllowed = isNFCReadingAllowed;
    }

    @Override
    public NFCTag getNFCTag() {
        return myTag;
    }

    /**
     * callback from the TagDiscovery util class
     *
     * @param nfcTag
     * @param productId
     * @param e
     */
    @Override
    public void onTagDiscoveryCompleted(NFCTag nfcTag, TagHelper.ProductID productId, STException e) {
        Timber.d("onTagDiscoveryCompleted");
        if (nfcTag == null) {
            Toast.makeText(this, "Tag discovery failed!", Toast.LENGTH_LONG).show();
            return;
        }
        // we need to eliminate unsupported product Ids
        switch (productId) {
            case PRODUCT_ST_ST25DV64K_I:
            case PRODUCT_ST_ST25DV64K_J:
            case PRODUCT_ST_ST25DV16K_I:
            case PRODUCT_ST_ST25DV16K_J:
            case PRODUCT_ST_ST25DV04K_I:
            case PRODUCT_ST_ST25DV04K_J:
            case PRODUCT_ST_ST25DV04KC_I:
            case PRODUCT_ST_ST25DV04KC_J:
            case PRODUCT_ST_ST25DV16KC_I:
            case PRODUCT_ST_ST25DV16KC_J:
            case PRODUCT_ST_ST25DV64KC_I:
            case PRODUCT_ST_ST25DV64KC_J:
                Timber.d("discovered NFCTag is supported");
                myTag = nfcTag;
                String tagName = nfcTag.getName();
                Toast.makeText(this, "Tag discovery done. Found tag: " + tagName, Toast.LENGTH_LONG).show();
                if (isNFCReadingAllowed) {
                    prepareForwardNavigationToRead();
                } else {
                    WriteToTagFragment writeToTagFragment = (WriteToTagFragment) fragmentManager.findFragmentByTag(WriteToTagFragment.class.getCanonicalName());
                    if(writeToTagFragment != null){
                     writeToTagFragment.setNFCTagToWriteOn(myTag);
                    }
                }
                break;
            default:
                Timber.d("discovered NFCTag is NOT supported");
        }

    }

    private void prepareForwardNavigationToRead() {
        if (!isNFCReadingAllowed) {
            Timber.d("current fragment does not allow to read");
            return;
        }
        Timber.d("current fragment allows to read");
        ReadFromTagFragment readFromTagFragment = (ReadFromTagFragment) fragmentManager.findFragmentByTag(ReadFromTagFragment.class.getCanonicalName());
        if (readFromTagFragment != null) {
            Timber.d("message can be set to an existing ReadFromTagFragment");
            // todo pass in the tag rather than the message
            new Thread(() -> {
                try {
                   byte[] bytes = myTag.readBytes(Constants.DATA_OFFSET, Constants.TOTAL_DATA_LENGTH);
                    this.runOnUiThread(() -> readFromTagFragment.setTagData(bytes));
                } catch (STException e) {
                    e.printStackTrace();
                }
            }).start();
            return;
        }
        Timber.d("navigating to ReadFromTagFragment with a rawMessage");
        new Thread(() -> {
            try {
                NDEFMsg ndefMsg = myTag.readNdefMessage();
                this.runOnUiThread(() -> navigateToReadFromTagFragment(ndefMsg));
            } catch (STException e) {
                this.runOnUiThread(() -> e.printStackTrace());
            }
        }).start();
    }

}

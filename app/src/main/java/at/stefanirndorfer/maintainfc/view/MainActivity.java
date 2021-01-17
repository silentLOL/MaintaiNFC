package at.stefanirndorfer.maintainfc.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements NavigationListener {

    private FragmentManager fragmentManager;


    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;

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
        readFromIntent(getIntent());

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
        writeModeOn();
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
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (isNFCReadingAllowed) {
                Timber.d("current fragment allows to read");
                ReadFromTagFragment readFromTagFragment = (ReadFromTagFragment) fragmentManager.findFragmentByTag(ReadFromTagFragment.class.getCanonicalName());
                if (readFromTagFragment != null) {
                    Timber.d("message can be set to an existing ReadFromTagFragment");
                    readFromTagFragment.setRawMessage(rawMsgs);
                    return;
                }
                Timber.d("navigating to ReadFromTagFragment with a rawMessage");
                navigateToReadFromTagFragment(rawMsgs);
                return;
            }
            Timber.d("current fragment does not allow to read");
        }
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
        }
    }


    //////////////////////////////////////////////
    // NavigationListener
    //////////////////////////////////////////////

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
    public void navigateToReadFromTagFragment(Parcelable[] rawMessage) {
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
    public void showHomeButton() {
        Timber.d("showing back navigation button");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void hideHomeButton() {
        Timber.d("hiding back navigation button");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void isNFCReadingAllowed(boolean isNFCReadingAllowed) {
        this.isNFCReadingAllowed = isNFCReadingAllowed;
    }

}
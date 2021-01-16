package at.stefanirndorfer.maintainfc.view;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import at.stefanirndorfer.maintainfc.R;
import at.stefanirndorfer.maintainfc.input.NavigationListener;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements NavigationListener {

    private FragmentManager fragmentManager;
    private WriteToTagFragment writeToTagFragment;
    private ReadFromTagFragment readFromTagFragment;
    private FormatTagFragment formatTagFragment;

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

    @Override
    public void navigateToWriteToTagFragment() {
        Timber.d("navigating to WriteToTagFragment");
        if (writeToTagFragment == null) {
            writeToTagFragment = WriteToTagFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, writeToTagFragment)
                .addToBackStack(writeToTagFragment.getClass().getCanonicalName())
                .commit();
    }

    @Override
    public void navigateToReadFromTagFragment() {
        Timber.d("navigating to ReadFromTagFragment");
        if (readFromTagFragment == null) {
            readFromTagFragment = ReadFromTagFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, readFromTagFragment)
                .addToBackStack(readFromTagFragment.getClass().getCanonicalName())
                .commit();
    }

    @Override
    public void navigateToFormatTagFragment() {
        Timber.d("navigating to FormatTagFragment");
        if (formatTagFragment == null) {
            formatTagFragment = FormatTagFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, formatTagFragment)
                .addToBackStack(formatTagFragment.getClass().getCanonicalName())
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
}
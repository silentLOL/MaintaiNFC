package at.stefanirndorfer.maintainfc.input;

public interface NavigationListener {
    void navigateToWriteToTagFragment();

    void navigateToReadFromTagFragment();

    void navigateToFormatTagFragment();

    void showHomeButton();

    void hideHomeButton();
}

package itukraine.com.ua.bestmobile.ui.fragment;

/**
 * Created by User on 13.01.2016.
 */
public interface SearchAction {
    void displayClearSearchButton(boolean isDisplayed);

    String getSearchQuery();

    void clearSearchQuery();
}

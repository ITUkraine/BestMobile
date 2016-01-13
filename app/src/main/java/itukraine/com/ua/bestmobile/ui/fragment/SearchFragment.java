package itukraine.com.ua.bestmobile.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by User on 13.01.2016.
 */
public abstract class SearchFragment extends Fragment implements SearchAction {

    protected EditText editSearch;
    protected ImageView imageClearSearch;

    @Override
    public void displayClearSearchButton(boolean isDisplayed) {
        imageClearSearch.setVisibility(isDisplayed ? View.VISIBLE : View.GONE);
    }

    @Override
    public String getSearchQuery() {
        return editSearch.getText().toString();
    }

    @Override
    public void clearSearchQuery() {
        editSearch.setText("");
    }
}

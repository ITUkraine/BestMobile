package itukraine.com.ua.bestmobile.ui.activity.view;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.View;

public interface MainView {

    void setNavigationHeaderPlayButtonDrawable(@DrawableRes int drawableId);

    void setNavigationHeaderBackgroundDrawable(Drawable drawable);

    void clearBackStack();

    void openFragment(Fragment fragment);

    void hideKeyboard(View view);

    void setToolbarTitle(String title);
}

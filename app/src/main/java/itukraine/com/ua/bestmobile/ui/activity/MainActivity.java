package itukraine.com.ua.bestmobile.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.presenter.MainPresenter;
import itukraine.com.ua.bestmobile.presenter.MainPresenterImpl;
import itukraine.com.ua.bestmobile.ui.fragment.AllPlaylistsFragment;
import itukraine.com.ua.bestmobile.ui.fragment.FeedbackFragment;
import itukraine.com.ua.bestmobile.ui.fragment.PlayerFragment;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MainView {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static boolean isActive = false;
    private Toolbar mToolbar;

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    private View mNavHeaderView;
    private ImageButton mNextSongBtnNavHeader;
    private ImageButton mPrevSongBtnNavHeader;
    private ImageButton mPlaySongBtnNavHeader;

    private MainPresenter mainPresenter;

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initNavigationDrawer();
        initNavigationHeaderControls();

        mainPresenter = new MainPresenterImpl(this);
    }

    private void initNavigationDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        CustomNavDrawerListener drawerListener = new CustomNavDrawerListener(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(drawerListener);
        drawerListener.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mNavHeaderView = mNavigationView.getHeaderView(0).findViewById(R.id.nav_bar_header);
        mNavHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new PlayerFragment());
                onBackPressed();
            }
        });
    }

    private void initNavigationHeaderControls() {
        mNextSongBtnNavHeader = (ImageButton) mNavigationView.getHeaderView(0).findViewById(R.id.header_btn_next);
        mPrevSongBtnNavHeader = (ImageButton) mNavigationView.getHeaderView(0).findViewById(R.id.header_btn_prev);
        mPlaySongBtnNavHeader = (ImageButton) mNavigationView.getHeaderView(0).findViewById(R.id.header_btn_play);

        mNextSongBtnNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onHeaderNextButtonPressed();
            }
        });

        mPrevSongBtnNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onHeaderPreviousButtonPressed();
            }
        });

        mPlaySongBtnNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onHeaderPlayButtonPressed();
            }
        });

    }

    /**
     * Open fragment(support) passed to parameter in current activity.
     *
     * @param fragment Fragment which should be opened.
     */
    @Override
    public void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_fragment, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // if drawer is open - close
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            // if back stack is empty minimize application
            moveTaskToBack(true);
        } else {
            // if back stack ISN'T empty - go back
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        clearBackStack();

        if (id == R.id.nav_playback) {
            openFragment(new PlayerFragment());
        } else if (id == R.id.nav_playlist) {
            openFragment(new AllPlaylistsFragment());
        } else if (id == R.id.nav_feedback) {
            openFragment(new FeedbackFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Clear fragments "back stack".
     */
    @Override
    public void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fm.popBackStackImmediate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        Log.i(TAG, "Activity minimised");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        Log.i(TAG, "Activity restored");
    }

    @Override
    protected void onDestroy() {
        mainPresenter.onDestroy();

        super.onDestroy();
    }

    /**
     * Hide keyboard.
     *
     * @param view Base focused view.
     */
    @Override
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Set play button drawable in navigation drawer header.
     *
     * @param drawableId Drawable resource id.
     */
    @Override
    public void setNavigationHeaderPlayButtonDrawable(@DrawableRes int drawableId) {
        mPlaySongBtnNavHeader.setBackgroundResource(drawableId);
    }

    /**
     * Set drawable to navigation drawer header.
     *
     * @param drawable Drawable to set.
     */
    @Override
    public void setNavigationHeaderBackgroundDrawable(Drawable drawable) {
        mNavHeaderView.setBackground(drawable);
    }

    private class CustomNavDrawerListener extends ActionBarDrawerToggle implements DrawerLayout.DrawerListener {

        public CustomNavDrawerListener(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            // hide keyboard when drawer open
            hideKeyboard(drawerView);

            mainPresenter.updateNavigationHeaderControls();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }

    }

}

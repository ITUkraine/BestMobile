package itukraine.com.ua.bestmobile;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import itukraine.com.ua.bestmobile.fragment.AllPlaylistsFragment;
import itukraine.com.ua.bestmobile.fragment.PlayerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private View mNavHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        openFragment(new PlayerFragment());

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

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

    /**
     * Open fragment from parameter.
     *
     * @param fragment Open fragment
     */
    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_fragment, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        clearBackStack();

        if (id == R.id.nav_playback) {
            openFragment(new PlayerFragment());
        } else if (id == R.id.nav_playlist) {
            openFragment(new AllPlaylistsFragment());
        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            fm.popBackStackImmediate();
        }
    }

    /**
     * Set picture to navigation drawer. It will be displayed as album picture.
     *
     * @param albumArt set image to header. If 'null' passed default will be set.
     */
    private void setNavigationDrawerHeader(Drawable albumArt) {
        if (albumArt == null) {
            albumArt = getResources().getDrawable(R.drawable.default_song_picture);
        }
        albumArt.setColorFilter(Color.argb(150, 155, 155, 155), PorterDuff.Mode.SRC_ATOP);
        mNavHeaderView.setBackground(albumArt);
    }
}

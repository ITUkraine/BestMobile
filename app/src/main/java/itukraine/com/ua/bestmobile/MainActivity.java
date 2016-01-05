package itukraine.com.ua.bestmobile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
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

import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.fragment.AllPlaylistsFragment;
import itukraine.com.ua.bestmobile.fragment.FeedbackFragment;
import itukraine.com.ua.bestmobile.fragment.PlayerFragment;
import itukraine.com.ua.bestmobile.service.PlaybackService;
import itukraine.com.ua.bestmobile.util.ImageUtil;
import itukraine.com.ua.bestmobile.util.MusicUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static boolean isActive = false;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private View mNavHeaderView;
    private ImageButton mNextSongBtnNavHeader;
    private ImageButton mPrevSongBtnNavHeader;
    private ImageButton mPlaySongBtnNavHeader;
    private PlaybackService mPlaybackService;
    private Intent mPlayIntent;
    private boolean mMusicBound = false;
    private BroadcastReceiver receiverProgressUpdate;
    private BroadcastReceiver receiverInfoUpdate;
    //connection to the service
    private ServiceConnection playbackConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlaybackService.PlaybackBinder binder = (PlaybackService.PlaybackBinder) service;
            //get service
            mPlaybackService = binder.getService();
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
        }
    };

    public PlaybackService getPlaybackService() {
        return mPlaybackService;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        openFragment(new PlayerFragment());

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

        mNextSongBtnNavHeader = (ImageButton) mNavigationView.getHeaderView(0).findViewById(R.id.header_btn_next);
        mPrevSongBtnNavHeader = (ImageButton) mNavigationView.getHeaderView(0).findViewById(R.id.header_btn_prev);
        mPlaySongBtnNavHeader = (ImageButton) mNavigationView.getHeaderView(0).findViewById(R.id.header_btn_play);

        mNextSongBtnNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaybackService.nextSong();
            }
        });

        mPrevSongBtnNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaybackService.prevSong();
            }
        });

        mPlaySongBtnNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaybackService.smartPlay();
                updateNavHeaderPlayButton();
            }
        });

        receiverProgressUpdate = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int pos = intent.getIntExtra(PlaybackService.EXTRA_PROGRESS, 0);
                sendDataToFragment(pos);
            }
        };

        receiverInfoUpdate = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean infoUpdate = intent.getBooleanExtra(PlaybackService.EXTRA_INFO_CHANGED, false);
                if (infoUpdate) {
                    sendUpdateToFragment();
                    Log.i(TAG, "Service: " + mPlaybackService);
                    updateNavigationHeaderSongInfo();
                    updateNavHeaderPlayButton();
                }
            }
        };
    }

    public void updateNavHeaderPlayButton() {
        if (mPlaybackService.isPlaying()) {
            mPlaySongBtnNavHeader.setBackgroundResource(android.R.drawable.ic_media_pause);
        } else {
            mPlaySongBtnNavHeader.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }

    private void sendDataToFragment(int pos) {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (currentFragment instanceof PlayerFragment) {
            ((PlayerFragment) currentFragment).updateSongProgress(pos);
        }
    }

    private void sendUpdateToFragment() {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (currentFragment instanceof PlayerFragment) {
            ((PlayerFragment) currentFragment).displaySongData();
        }
    }

    private void updateNavigationHeaderSongInfo() {
        Song current = mPlaybackService.getCurrentSong();
        Drawable art;
        Bitmap bitmap = MusicUtil.getInstance().getAlbumart(this, current.albumId);
        if (bitmap == null) {
            art = getResources().getDrawable(R.drawable.default_song_picture);
        } else {
            art = new BitmapDrawable(
                    getResources(),
                    ImageUtil.getInstance().getScaledBitmap1to1(this, bitmap));
        }

        art.setColorFilter(Color.argb(50, 155, 155, 155), PorterDuff.Mode.SRC_ATOP);
        mNavHeaderView.setBackground(art);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(this, PlaybackService.class);
            bindService(mPlayIntent, playbackConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver((receiverProgressUpdate),
                new IntentFilter(PlaybackService.PLAYBACK_PROGRESS_UPDATE));
        LocalBroadcastManager.getInstance(this).registerReceiver((receiverInfoUpdate),
                new IntentFilter(PlaybackService.PLAYBACK_INFO_UPDATE));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverProgressUpdate);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverInfoUpdate);
        super.onStop();
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
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            moveTaskToBack(true);
        } else {
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

    private void clearBackStack() {
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
        unbindService(playbackConnection);
        stopService(mPlayIntent);
        Log.i(TAG, "Activity destroyed");
        mPlaybackService = null;
        super.onDestroy();
    }

    private class CustomNavDrawerListener extends ActionBarDrawerToggle implements DrawerLayout.DrawerListener {

        public CustomNavDrawerListener(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            Log.i(TAG, "Drawer open");

            // hide keyboard when drawer open
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);

            updateNavHeaderPlayButton();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            Log.i(TAG, "Drawer close");

            sendUpdateToFragment();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            Log.i(TAG, "Drawer change");
        }

    }

}

package itukraine.com.ua.bestmobile.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.presenter.PlayerPresenter;
import itukraine.com.ua.bestmobile.presenter.impl.PlayerPresenterImpl;
import itukraine.com.ua.bestmobile.ui.activity.view.MainView;
import itukraine.com.ua.bestmobile.ui.fragment.view.PlayerView;
import itukraine.com.ua.bestmobile.util.TimeUtil;

public class PlayerFragment extends Fragment implements PlayerView {

    private static final String TAG = PlayerFragment.class.getCanonicalName();
    private ImageView imageAlbum;
    private TextView textArtist;
    private TextView textSong;
    private SeekBar songProgressbar;
    private Button buttonPlay;
    private Button buttonNextSong;
    private Button buttonPrevSong;
    private TextView textCurrentTime;
    private TextView textTotalDuration;
    private Button buttonShuffleMode;
    private Button buttonRepeatMode;

    private PlayerPresenter playerPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        ((MainView) getActivity()).setToolbarTitle(getResources().getString(R.string.app_name));

        initSongInfoViews(view);
        initPlaybackControlViews(view);
        initDurationProgressViews(view);

        playerPresenter = new PlayerPresenterImpl(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        playerPresenter.onCreate();
    }

    private void initSongInfoViews(View view) {
        imageAlbum = (ImageView) view.findViewById(R.id.song_picture);
        textArtist = (TextView) view.findViewById(R.id.song_artist);
        textSong = (TextView) view.findViewById(R.id.song_title);
    }

    private void initDurationProgressViews(View view) {
        textCurrentTime = (TextView) view.findViewById(R.id.text_current_time);
        textTotalDuration = (TextView) view.findViewById(R.id.text_total_duration);
        songProgressbar = (SeekBar) view.findViewById(R.id.song_progressbar);

        songProgressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setCurrentSongPlayedDuration(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playerPresenter.rewindTo(seekBar.getProgress());
            }
        });
    }

    private void initPlaybackControlViews(View view) {
        buttonPlay = (Button) view.findViewById(R.id.button_play);
        buttonNextSong = (Button) view.findViewById(R.id.button_next_song);
        buttonPrevSong = (Button) view.findViewById(R.id.button_prev_song);
        buttonShuffleMode = (Button) view.findViewById(R.id.button_shufle);
        buttonRepeatMode = (Button) view.findViewById(R.id.button_repeat);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerPresenter.onPlayButtonPressed();
            }
        });

        buttonNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerPresenter.onNextButtonPressed();
            }
        });

        buttonPrevSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerPresenter.onPreviousButtonPressed();
            }
        });

        buttonShuffleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerPresenter.onShuffleModePressed();
            }
        });

        buttonRepeatMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerPresenter.onRepeatModePressed();
            }
        });
    }

    @Override
    public void clearDurationProgress() {
        songProgressbar.setProgress(0);
        textCurrentTime.setText(getString(R.string.default_duration));
    }

    @Override
    public void setAlbumImage(Bitmap bitmap) {
        imageAlbum.setImageBitmap(bitmap);
    }

    @Override
    public void setSongInfo(String artist, String title, int time) {
        textArtist.setText(artist);
        textSong.setText(title);
        textTotalDuration.setText(TimeUtil.getInstance().formatTime(time));
        songProgressbar.setMax(time);
    }

    @Override
    public void setPlayButtonDrawable(@DrawableRes int drawableId) {
        buttonPlay.setBackgroundResource(drawableId);
    }

    @Override
    public void setCurrentSongPlayedDuration(int time) {
        textCurrentTime.setText(TimeUtil.getInstance().formatTime(time));
        songProgressbar.setProgress(time);
    }

    @Override
    public void setShuffleButtonDrawable(@DrawableRes int drawableId) {
        buttonShuffleMode.setBackgroundResource(drawableId);
    }

    @Override
    public void setRepeatButtonDrawable(@DrawableRes int drawableId) {
        buttonRepeatMode.setBackgroundResource(drawableId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playerPresenter.onDestroy();
    }
}

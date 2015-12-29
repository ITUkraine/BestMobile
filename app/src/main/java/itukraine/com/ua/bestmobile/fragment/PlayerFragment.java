package itukraine.com.ua.bestmobile.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import itukraine.com.ua.bestmobile.MainActivity;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.dao.Song;
import itukraine.com.ua.bestmobile.util.MusicUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    private static final String TAG = PlayerFragment.class.getCanonicalName();
    ImageView imageAlbum;
    TextView textArtist;
    TextView textSong;
    SeekBar songProgressbar;
    Button buttonPlay;
    Button buttonNextSong;
    Button buttonPrevSong;
    private MainActivity activity;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        // display data views
        imageAlbum = (ImageView) view.findViewById(R.id.song_picture);
        textArtist = (TextView) view.findViewById(R.id.song_artist);
        textSong = (TextView) view.findViewById(R.id.song_title);

        // control views
        buttonPlay = (Button) view.findViewById(R.id.button_play);
        buttonNextSong = (Button) view.findViewById(R.id.button_next_song);
        buttonPrevSong = (Button) view.findViewById(R.id.button_prev_song);
        songProgressbar = (SeekBar) view.findViewById(R.id.song_progressbar);

        activity = (MainActivity) getActivity();

        activity.getToolbar().setTitle(getResources().getString(R.string.app_name));

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getPlaybackService().isPlaying()) {
                    activity.getPlaybackService().pauseSong();
                } else {
                    if (activity.getPlaybackService().getCurrentTime() > activity.getPlaybackService().getCurrentSong().duration) {
                        activity.getPlaybackService().playSong();
                    } else {
                        activity.getPlaybackService().continueSong();
                    }
                }

                updatePlayButton();
            }
        });

        buttonNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songProgressbar.setProgress(0);
                activity.getPlaybackService().nextSong();
            }
        });

        buttonPrevSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songProgressbar.setProgress(0);
                activity.getPlaybackService().prevSong();
            }
        });

        songProgressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int pos = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pos = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                activity.getPlaybackService().rewindTo(pos);
                Log.i(TAG, String.format("Rewound to %d from %d",
                        pos,
                        activity.getPlaybackService().getCurrentSong().duration));
            }
        });

        return view;
    }

    public void updateSongProgress(final int pos) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                songProgressbar.setProgress(pos);
            }
        });
    }

    public void updatePlayButton() {
        if (activity.getPlaybackService().isPlaying()) {
            buttonPlay.setBackgroundResource(R.drawable.ic_pause);
        } else {
            buttonPlay.setBackgroundResource(R.drawable.ic_play);
        }
    }

    /**
     * Load data of song to the layout.
     */
    public void displaySongData() {
        Song currentSong = activity.getPlaybackService().getCurrentSong();

        textArtist.setText(currentSong.artist);
        textSong.setText(currentSong.title);

        songProgressbar.setMax(currentSong.duration);

        Bitmap bitmap = MusicUtil.getInstance().getAlbumart(getActivity(), currentSong.albumId);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_song_picture);
        }
        imageAlbum.setImageBitmap(getScaledBitmap1to1(bitmap));

        updatePlayButton();
    }

    /**
     * Scale bitmap in aspect ratio 1:1 based on width
     *
     * @param source Bitmap which need to be scaled
     * @return Scaled bitmap
     */
    private Bitmap getScaledBitmap1to1(Bitmap source) {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int newWidth = metrics.widthPixels;
        return Bitmap.createScaledBitmap(source, newWidth, newWidth, true);
    }

}

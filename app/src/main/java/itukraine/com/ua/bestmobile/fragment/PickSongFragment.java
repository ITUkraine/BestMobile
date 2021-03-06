package itukraine.com.ua.bestmobile.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.adapter.PickSongAdapter;
import itukraine.com.ua.bestmobile.dao.Playlist;
import itukraine.com.ua.bestmobile.dao.Song;
import itukraine.com.ua.bestmobile.data.DatabaseHelper;
import itukraine.com.ua.bestmobile.util.MusicUtil;
import itukraine.com.ua.bestmobile.util.RecyclerItemClickListener;
import itukraine.com.ua.bestmobile.util.TimeUtil;
import itukraine.com.ua.bestmobile.view.RecyclerViewLineDevider;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickSongFragment extends Fragment {

    private static final String TAG = PickSongFragment.class.getCanonicalName();

    private boolean isNewPlaylist;

    private RecyclerView mRecyclerView;
    private PickSongAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText editSearch;
    private ImageView imageClearSearch;
    private FloatingActionButton addSongsButton;

    private Context mContext;

    private List<Song> allSongs;
    private String playlistName;
    private Comparator<Song> alphabeticalSongComparator = new Comparator<Song>() {
        public int compare(Song s1, Song s2) {
            return s1.artist.toLowerCase().compareTo(s2.artist.toLowerCase());
        }
    };

    public PickSongFragment() {
    }

    public PickSongFragment(String playlistName, boolean isNewPlaylist) {
        this.playlistName = playlistName;
        this.isNewPlaylist = isNewPlaylist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pick_song_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.songs_view);

        imageClearSearch = (ImageView) view.findViewById(R.id.imageClearSearch);
        imageClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PickSongAdapter) mRecyclerView.getAdapter()).flushFilter();
                imageClearSearch.setVisibility(View.GONE);
                editSearch.setText("");
            }
        });
        editSearch = (EditText) view.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = editSearch.getText().toString();
                ((PickSongAdapter) mRecyclerView.getAdapter()).setFilter(query);
                imageClearSearch.setVisibility(query.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addSongsButton = (FloatingActionButton) view.findViewById(R.id.add_songs_to_playlist);
        addSongsButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        addSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.selectedSongs.size() == 0) {
                    Toast.makeText(mContext, R.string.msg_at_least_one_song, Toast.LENGTH_LONG).show();
                    return;
                }
                Playlist newPlaylist = new Playlist(playlistName);
                newPlaylist.songsId.clear();
                newPlaylist.songsId.addAll(mAdapter.selectedSongs);
                newPlaylist.totalTime = TimeUtil.getInstance().calculateTotalTimeOfPlaylist(MusicUtil.getInstance().getSongsByID(mContext, newPlaylist.songsId));
                if (isNewPlaylist) {
                    DatabaseHelper.getInstance(mContext).addPlaylist(newPlaylist);
                } else {
                    DatabaseHelper.getInstance(mContext).updatePlaylist(newPlaylist);
                }

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new AllPlaylistsFragment()).commit();
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        allSongs = MusicUtil.getInstance().getAllSongs(mContext);

        Collections.sort(allSongs, alphabeticalSongComparator);

        mAdapter = new PickSongAdapter(mContext, allSongs, playlistName, isNewPlaylist);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new RecyclerViewLineDevider(getResources()));

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new OnItemClickListener()));

        Toast.makeText(mContext, R.string.msg_select_songs, Toast.LENGTH_LONG).show();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        @Override
        public void onItemClick(View childView, int position) {
            if (!mAdapter.selectedSongs.contains(mAdapter.visibleSongs.get(position).id)) {
                mAdapter.selectedSongs.add(mAdapter.visibleSongs.get(position).id);
            } else {
                mAdapter.selectedSongs.remove(mAdapter.visibleSongs.get(position).id);
            }
            mAdapter.notifyItemChanged(position);
        }

    }

}

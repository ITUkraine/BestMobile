package itukraine.com.ua.bestmobile.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import itukraine.com.ua.bestmobile.MainActivity;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.adapter.SongAdapter;
import itukraine.com.ua.bestmobile.dao.Playlist;
import itukraine.com.ua.bestmobile.dao.Song;
import itukraine.com.ua.bestmobile.data.DatabaseHelper;
import itukraine.com.ua.bestmobile.util.MusicUtil;
import itukraine.com.ua.bestmobile.util.RecyclerItemClickListener;
import itukraine.com.ua.bestmobile.view.RecyclerViewLineDevider;

public class SongListFragment extends Fragment {

    private static final String TAG = SongListFragment.class.getCanonicalName();

    private RecyclerView mRecyclerView;
    private SongAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAddSongsToPlaylist;

    private Context mContext;

    private List<Song> songList; // TODO don't really know if it's needed
    private Playlist currentPlaylist;

    public SongListFragment() {
    }

    public SongListFragment(Playlist currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.songs_view);

        buttonAddSongsToPlaylist = (FloatingActionButton) view.findViewById(R.id.add_song_button);
        buttonAddSongsToPlaylist.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        buttonAddSongsToPlaylist.setVisibility(currentPlaylist.name.equals(getResources().getString(R.string.all_songs_playlist_name)) ? View.GONE : View.VISIBLE);
        buttonAddSongsToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_fragment, new PickSongFragment(currentPlaylist.name, false)).addToBackStack(null).commit();
            }
        });

        mRecyclerView.setHasFixedSize(true);

        ((MainActivity) getActivity()).getToolbar().setTitle(currentPlaylist.name);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (currentPlaylist.name.equals(getResources().getString(R.string.all_songs_playlist_name))) {
            songList = MusicUtil.getInstance().getAllSongs(mContext);
        } else {
            songList = MusicUtil.getInstance().getSongsByID(mContext, currentPlaylist.songsId);
        }

        mAdapter = new SongAdapter(mContext, songList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new RecyclerViewLineDevider(getResources()));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new OnItemClickListener()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void displayConfirmDeleteDialog(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(
                mContext.getString(R.string.msg_confirm_delete_song))
                .setCancelable(false).setPositiveButton(mContext.getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                @SuppressWarnings("unused") final int id) {
                currentPlaylist.songsId.remove(songList.get(position).id);
                DatabaseHelper.getInstance(mContext).updatePlaylist(currentPlaylist);
                songList.remove(position);
                mAdapter.notifyItemChanged(position);
            }
        }).setNegativeButton(mContext.getString(R.string.btn_no), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        @Override
        public void onItemClick(View childView, int position) {
            // TODO open player???
        }

        @Override
        public void onItemLongPress(View childView, final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(R.array.song_action_array, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //TODO Play song
                            break;
                        case 1:
                            //Delete song
                            displayConfirmDeleteDialog(position);
                            break;
                    }
                }
            });
            builder.show();
        }
    }

}

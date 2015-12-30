package itukraine.com.ua.bestmobile.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import itukraine.com.ua.bestmobile.MainActivity;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.adapter.PlaylistAdapter;
import itukraine.com.ua.bestmobile.dao.Playlist;
import itukraine.com.ua.bestmobile.data.DatabaseHelper;
import itukraine.com.ua.bestmobile.util.MusicUtil;
import itukraine.com.ua.bestmobile.util.RecyclerItemClickListener;
import itukraine.com.ua.bestmobile.view.RecyclerViewLineDevider;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPlaylistsFragment extends Fragment {

    private static final String TAG = AllPlaylistsFragment.class.getCanonicalName();

    private RecyclerView mRecyclerView;
    private PlaylistAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText editSearch;
    private ImageView imageClearSearch;
    private FloatingActionButton addPlaylistButton;

    private Context mContext;

    private MainActivity activity;

    private List<Playlist> playlists;

    private Comparator<Playlist> alphabeticalPlaylistComparator = new Comparator<Playlist>() {
        public int compare(Playlist p1, Playlist p2) {
            if (p1.name.equals(getResources().getString(R.string.all_songs_playlist_name)))
                return -1;
            if (p2.name.equals(getResources().getString(R.string.all_songs_playlist_name)))
                return 1;
            return p1.name.toLowerCase().compareTo(p2.name.toLowerCase());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_playlists, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.playlist_view);


        activity = (MainActivity) getActivity();

        activity.getToolbar().setTitle(R.string.playlists);

        imageClearSearch = (ImageView) view.findViewById(R.id.imageClearSearch);
        imageClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PlaylistAdapter) mRecyclerView.getAdapter()).flushFilter();
                imageClearSearch.setVisibility(View.GONE);
                editSearch.setText("");
            }
        });
        editSearch = (EditText) view.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = editSearch.getText().toString();
                ((PlaylistAdapter) mRecyclerView.getAdapter()).setFilter(query);
                imageClearSearch.setVisibility(query.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        addPlaylistButton = (FloatingActionButton) view.findViewById(R.id.add_playlist_button);
        addPlaylistButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPlaylistTitleDialog(null, true);
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        playlists = DatabaseHelper.getInstance(mContext).getPlaylists();
        playlists.add(new Playlist(getResources().getString(R.string.all_songs_playlist_name)));

        Collections.sort(playlists, alphabeticalPlaylistComparator);

        mAdapter = new PlaylistAdapter(mContext, playlists);
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
                mContext.getString(R.string.msg_confirm_delete_playlist))
                .setCancelable(false).setPositiveButton(mContext.getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                @SuppressWarnings("unused") final int id) {
                DatabaseHelper.getInstance(mContext).deletePlaylistByName(playlists.get(position).name);
                playlists.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton(mContext.getString(R.string.btn_no), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void displayPlaylistTitleDialog(final Integer position, final boolean isNewPlaylist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setTitle(isNewPlaylist ? mContext.getString(R.string.dlg_create_playlist) : mContext.getString(R.string.dlg_rename_playlist));
        final EditText input = new EditText(getActivity());
        builder.setPositiveButton(isNewPlaylist ? mContext.getString(R.string.btn_create_playlist) : mContext.getString(R.string.btn_rename_playlist), null);
        builder.setNegativeButton(mContext.getString(R.string.btn_cancel), null);
        builder.setView(input);

        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button positiveButton = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String newPlaylistName = input.getText().toString();
                        if (newPlaylistName.equals("")) {
                            Toast.makeText(mContext, R.string.msg_at_least_one_letter, Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (newPlaylistName.toLowerCase().equals(getResources().getString(R.string.all_songs_playlist_name).toLowerCase())
                                || DatabaseHelper.getInstance(mContext).findPlaylistByName(newPlaylistName) != null) {
                            Toast.makeText(mContext, R.string.msg_playlist_exist, Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!isNewPlaylist) {
                            String oldPlaylistName = playlists.get(position).name;
                            DatabaseHelper.getInstance(mContext).renamePlaylist(oldPlaylistName, newPlaylistName);
                            playlists.get(position).name = newPlaylistName;
                            mAdapter.notifyItemChanged(position);
                            mAlertDialog.cancel();
                            return;
                        }
                        mAlertDialog.cancel();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new PickSongFragment(newPlaylistName, true)).addToBackStack(null).commit();
                    }
                });

                Button negativeButton = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mAlertDialog.cancel();
                    }
                });
            }
        });
        mAlertDialog.show();
    }

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        @Override
        public void onItemClick(View childView, int position) {
            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, new SongListFragment(playlists.get(position)))
                    .addToBackStack(null)
                    .commit();
        }

        @Override
        public void onItemLongPress(View childView, final int position) {

            final boolean isDefaultPlaylist = playlists.get(position).name.equals(getResources().getString(R.string.all_songs_playlist_name));

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(isDefaultPlaylist ? R.array.play : R.array.playlist_action_array, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            // Play playlist
                            if (isDefaultPlaylist) {
                                activity.getPlaybackService().setSongs(
                                        MusicUtil.getInstance().getAllSongs(mContext));
                            } else {
                                activity.getPlaybackService().setSongs(
                                        MusicUtil.getInstance().getSongsByID(
                                                mContext,
                                                playlists.get(position).songsId));
                            }
                            activity.getPlaybackService().playSong();
                            break;
                        case 1:
                            //Rename playlist
                            displayPlaylistTitleDialog(position, false);
                            break;
                        case 2:
                            //Delete playlist
                            displayConfirmDeleteDialog(position);
                            break;
                    }
                }
            });
            builder.show();
        }

    }

}

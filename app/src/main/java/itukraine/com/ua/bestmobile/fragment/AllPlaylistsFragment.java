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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.adapter.PlaylistAdapter;
import itukraine.com.ua.bestmobile.dao.Playlist;
import itukraine.com.ua.bestmobile.data.DatabaseHelper;
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

    private FloatingActionButton addPlaylistButton;

    private Context mContext;

    private List<Playlist> playlists;

    private Comparator<Playlist> alphabeticalPlaylistComparator = new Comparator<Playlist>() {
        public int compare(Playlist p1, Playlist p2) {
            return p1.name.toLowerCase().compareTo(p2.name.toLowerCase());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_playlists, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.playlist_view);

        DatabaseHelper.getInstance(mContext).addPlaylist(new Playlist("All songs"));

        addPlaylistButton = (FloatingActionButton) view.findViewById(R.id.add_playlist_button);
        addPlaylistButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Playlist title");

                final EditText input = new EditText(mContext);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Playlist newPlaylist = new Playlist(input.getText().toString());
                        DatabaseHelper.getInstance(mContext).addPlaylist(newPlaylist);
                        playlists.add(newPlaylist);
                        Collections.sort(playlists, alphabeticalPlaylistComparator);
                        mAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        playlists = DatabaseHelper.getInstance(mContext).getPlaylists();

        Collections.sort(playlists, alphabeticalPlaylistComparator);

        mAdapter = new PlaylistAdapter(playlists);
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

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        @Override
        public void onItemClick(View childView, int position) {
            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, new SongListFragment())
                    .addToBackStack(null)
                    .commit();
        }

    }
}

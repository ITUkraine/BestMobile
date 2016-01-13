package itukraine.com.ua.bestmobile.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import itukraine.com.ua.bestmobile.Constants;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.presenter.SongListPresenter;
import itukraine.com.ua.bestmobile.presenter.impl.SongListPresenterImpl;
import itukraine.com.ua.bestmobile.ui.activity.MainActivity;
import itukraine.com.ua.bestmobile.ui.activity.view.MainView;
import itukraine.com.ua.bestmobile.ui.adapter.SongAdapter;
import itukraine.com.ua.bestmobile.ui.fragment.view.SongListView;
import itukraine.com.ua.bestmobile.util.RecyclerItemClickListener;
import itukraine.com.ua.bestmobile.view.RecyclerViewLineDevider;

public class SongListFragment extends Fragment implements SongListView {

    private RecyclerView mRecyclerView;
    private FloatingActionButton buttonAddSongsToPlaylist;

    private EditText editSearch;
    private ImageView imageClearSearch;

    private Context mContext;
    private MainActivity activity;

    private SongListPresenter songListPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        Bundle bundle = this.getArguments();
        String playlistName = bundle.getString(Constants.PLAYLIST_NAME, null);

        activity = (MainActivity) getActivity();

        ((MainView) getActivity()).setToolbarTitle(playlistName);

        songListPresenter = new SongListPresenterImpl(this, playlistName);

        initButtonAddSongsToPlaylist(view);

        initSearchViews(view);

        initRecyclerView(view);

        songListPresenter.onResume();

        return view;
    }

    public void initSearchViews(View view) {
        imageClearSearch = (ImageView) view.findViewById(R.id.imageClearSearch);
        imageClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SongAdapter) mRecyclerView.getAdapter()).flushFilter();
                imageClearSearch.setVisibility(View.GONE);
                editSearch.setText("");
            }
        });

        editSearch = (EditText) view.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                songListPresenter.filterSongs();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void initButtonAddSongsToPlaylist(View view) {
        buttonAddSongsToPlaylist = (FloatingActionButton) view.findViewById(R.id.add_song_button);
        buttonAddSongsToPlaylist.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        buttonAddSongsToPlaylist.setVisibility(songListPresenter.isCurrentPlaylistDefault() ? View.GONE : View.VISIBLE);
        buttonAddSongsToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songListPresenter.displayPickSongView();
            }
        });
    }

    public void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.songs_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerViewLineDevider(getResources()));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new OnItemClickListener()));
    }

    @Override
    public void setAdapter(SongAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void openFragmentWithBackStack(Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void callBackStack() {
        activity.onBackPressed();
    }

    @Override
    public void displayButtonAddSongsToPlaylist(boolean isDisplayed) {
        buttonAddSongsToPlaylist.setVisibility(isDisplayed ? View.GONE : View.VISIBLE);
    }

    @Override
    public String getSearchQuery() {
        return editSearch.getText().toString();
    }

    @Override
    public void displayClearSearchButton(boolean isDisplayed) {
        imageClearSearch.setVisibility(isDisplayed ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void displayConfirmDeleteDialog(final int position) {
        //If it is last song, we propose to delete playlist
        final boolean isLastSong = songListPresenter.isLastSong();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(
                mContext.getString(isLastSong ? R.string.msg_confirm_delete_playlist : R.string.msg_confirm_delete_song))
                .setCancelable(false).setPositiveButton(mContext.getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                @SuppressWarnings("unused") final int id) {

                songListPresenter.deleteLastSong(position);
            }
        }).setNegativeButton(mContext.getString(R.string.btn_no), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void displayPossibleOperationForSongDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        boolean isDefaultPlaylist = songListPresenter.isCurrentPlaylistDefault();
        builder.setItems(isDefaultPlaylist ? R.array.play : R.array.song_action_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Play song
                        songListPresenter.playCurrentPlaylistFromPosition(position);
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

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        @Override
        public void onItemClick(View childView, int position) {
            songListPresenter.playCurrentPlaylistFromPosition(position);
            // open player fragment on single click on song
            openFragmentWithBackStack(new PlayerFragment());
        }

        @Override
        public void onItemLongPress(View childView, final int position) {
            displayPossibleOperationForSongDialog(position);
        }
    }

}

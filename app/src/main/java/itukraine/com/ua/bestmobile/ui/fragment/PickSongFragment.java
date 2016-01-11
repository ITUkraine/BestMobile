package itukraine.com.ua.bestmobile.ui.fragment;


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

import itukraine.com.ua.bestmobile.Constants;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.presenter.PickSongPresenter;
import itukraine.com.ua.bestmobile.presenter.impl.PickSongPresenterImpl;
import itukraine.com.ua.bestmobile.ui.adapter.PickSongAdapter;
import itukraine.com.ua.bestmobile.ui.fragment.view.PickSongView;
import itukraine.com.ua.bestmobile.util.RecyclerItemClickListener;
import itukraine.com.ua.bestmobile.view.RecyclerViewLineDevider;

public class PickSongFragment extends Fragment implements PickSongView {

    private RecyclerView mRecyclerView;

    private EditText editSearch;
    private ImageView imageClearSearch;

    private Context mContext;

    private boolean isNewPlaylist;
    private String playlistName;

    private PickSongPresenter pickSongPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pick_song_fragment, container, false);

        Bundle bundle = this.getArguments();
        this.playlistName = bundle.getString(Constants.PLAYLIST_NAME);
        this.isNewPlaylist = bundle.getBoolean(Constants.PLAYLIST_IS_NEW);

        initSearchViews(view);

        initAddSongsButton(view);

        initRecyclerView(view);

        pickSongPresenter = new PickSongPresenterImpl(this);
        pickSongPresenter.init(playlistName);
        return view;
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.songs_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new RecyclerViewLineDevider(getResources()));

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new OnItemClickListener()));
    }

    private void initSearchViews(View view) {
        imageClearSearch = (ImageView) view.findViewById(R.id.imageClearSearch);
        imageClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickSongPresenter.clearFilter();
            }
        });

        editSearch = (EditText) view.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pickSongPresenter.filterSongs();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void displayClearSearchButton(boolean isDisplayed) {
        imageClearSearch.setVisibility(isDisplayed ? View.VISIBLE : View.GONE);
    }

    @Override
    public String getSearchQuery() {
        return editSearch.getText().toString();
    }

    @Override
    public void clearSearchQuery() {
        editSearch.setText("");
    }

    @Override
    public void showNotificationToastToSelectSongs() {
        Toast.makeText(mContext, R.string.msg_select_songs, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAdapter(PickSongAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    private void initAddSongsButton(View view) {
        FloatingActionButton addSongsButton = (FloatingActionButton) view.findViewById(R.id.add_songs_to_playlist);
        addSongsButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        addSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickSongPresenter.checkIfSongsWasSelected()) {

                    pickSongPresenter.createOrUpdatePlaylist();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment, new AllPlaylistsFragment())
                            .commit();
                }
            }
        });
    }

    @Override
    public void showErrorToast() {
        Toast.makeText(mContext, R.string.msg_at_least_one_song, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        @Override
        public void onItemClick(View childView, int position) {
            pickSongPresenter.selectSongFromList(position);
        }

    }

}

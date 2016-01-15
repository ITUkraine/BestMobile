package itukraine.com.ua.bestmobile.ui.fragment;


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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import itukraine.com.ua.bestmobile.Constants;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.presenter.AllPlaylistPresenter;
import itukraine.com.ua.bestmobile.presenter.impl.AllPlaylistPresenterImpl;
import itukraine.com.ua.bestmobile.ui.activity.view.MainView;
import itukraine.com.ua.bestmobile.ui.adapter.PlaylistAdapter;
import itukraine.com.ua.bestmobile.ui.fragment.view.AllPlaylistView;
import itukraine.com.ua.bestmobile.util.RecyclerItemClickListener;
import itukraine.com.ua.bestmobile.view.RecyclerViewLineDevider;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPlaylistsFragment extends SearchFragment implements AllPlaylistView {


    private AllPlaylistPresenter allPlaylistPresenter;

    private RecyclerView mRecyclerView;

    private FloatingActionButton addPlaylistButton;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_playlists, container, false);

        ((MainView) getActivity()).setToolbarTitle(getResources().getString(R.string.playlists));

        imageClearSearch = (ImageView) view.findViewById(R.id.imageClearSearch);
        editSearch = (EditText) view.findViewById(R.id.editSearch);
        addPlaylistButton = (FloatingActionButton) view.findViewById(R.id.add_playlist_button);
        addPlaylistButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));

        initListeners();

        initRecycleView(view);

        initAds(view);

        allPlaylistPresenter = new AllPlaylistPresenterImpl(this);

        return view;
    }

    private void initAds(View view) {
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("A90B694B76C9614CBB306B5655F8EABC")
                .build();
//        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void setAdapter(PlaylistAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void displayErrorToastThatPlaylistAlreadyExist() {
        Toast.makeText(mContext, R.string.msg_playlist_exist, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayErrorToastThatPlaylistMustHaveAtLeastOneChar() {
        Toast.makeText(mContext, R.string.msg_at_least_one_letter, Toast.LENGTH_LONG).show();
    }

    @Override
    public void openFragmentWithBackStack(Fragment fragment) {
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void initListeners() {
        imageClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allPlaylistPresenter.clearFilter();
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                allPlaylistPresenter.filterSongs();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPlaylistTitleDialog(null, true);
            }
        });
    }

    private void initRecycleView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.playlist_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerViewLineDevider(getResources()));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                new OnItemClickListener()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void displayConfirmDeleteDialog(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(
                mContext.getString(R.string.msg_confirm_delete_playlist))
                .setCancelable(false).setPositiveButton(mContext.getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                @SuppressWarnings("unused") final int id) {
                allPlaylistPresenter.deletePlaylist(position);
            }
        }).setNegativeButton(mContext.getString(R.string.btn_no), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void displayPlaylistTitleDialog(final Integer position, final boolean isNewPlaylist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setTitle(isNewPlaylist ? mContext.getString(R.string.dlg_create_playlist) : mContext.getString(R.string.dlg_rename_playlist));
        final EditText input = new EditText(getActivity());
        input.setText(isNewPlaylist ? mContext.getString(R.string.empty_string) : allPlaylistPresenter.getOldPlaylistName(position));
        input.setSelection(input.getText().length());
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
                        if (newPlaylistName.equals(mContext.getString(R.string.empty_string))) {
                            displayErrorToastThatPlaylistMustHaveAtLeastOneChar();
                            return;
                        }
                        if (allPlaylistPresenter.isPlaylistExist(newPlaylistName)) {
                            displayErrorToastThatPlaylistAlreadyExist();
                            return;
                        }
                        if (!isNewPlaylist) {
                            allPlaylistPresenter.renamePlaylist(position, newPlaylistName);
                            mAlertDialog.cancel();
                            return;
                        }
                        mAlertDialog.cancel();

                        PickSongFragment pickSongFragment = new PickSongFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.PLAYLIST_NAME, newPlaylistName);
                        bundle.putBoolean(Constants.PLAYLIST_IS_NEW, true);
                        pickSongFragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fragment, pickSongFragment)
                                .addToBackStack(null)
                                .commit();
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

    public void displayPossibleOperationForPlaylistDialog(final int position) {
        final boolean isDefaultPlaylist = allPlaylistPresenter.isPlaylistDefault(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(isDefaultPlaylist ? R.array.play : R.array.playlist_action_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Play selected playlist
                        allPlaylistPresenter.selectAndPlayPlaylist(position);
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

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener {

        @Override
        public void onItemClick(View childView, int position) {
            allPlaylistPresenter.openSongListFragmentForSelectedPlaylist(position);
        }

        @Override
        public void onItemLongPress(View childView, final int position) {
            displayPossibleOperationForPlaylistDialog(position);
        }

    }

}

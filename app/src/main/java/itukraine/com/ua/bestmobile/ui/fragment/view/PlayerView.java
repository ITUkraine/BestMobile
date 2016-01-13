package itukraine.com.ua.bestmobile.ui.fragment.view;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;

public interface PlayerView {

    void clearDurationProgress();

    void setAlbumImage(Bitmap bitmap);

    void setSongInfo(String artist, String title, int time);

    void setPlayButtonDrawable(@DrawableRes int drawableId);

    void setCurrentSongPlayedDuration(int time);
}

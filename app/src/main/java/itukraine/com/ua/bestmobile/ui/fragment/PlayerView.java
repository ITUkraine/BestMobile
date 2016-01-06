package itukraine.com.ua.bestmobile.ui.fragment;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;

public interface PlayerView {

    void clearDurationProgress();

    void setAlbumImage(Bitmap bitmap);

    void setSongInfo(String artist, String title, String duration);

    void setPlayButtonDrawable(@DrawableRes int drawableId);

    void setTextCurrentSongPlayedDuration(String formattedTime);

    void setSeekerCurrentSongDuration(int pos);
}

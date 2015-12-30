package itukraine.com.ua.bestmobile.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

public class ImageUtil {

    private static ImageUtil instance;

    private ImageUtil() {
    }

    public static ImageUtil getInstance() {
        if (instance == null) {
            instance = new ImageUtil();
        }
        return instance;
    }

    /**
     * Scale bitmap in aspect ratio 1:1 based on width
     *
     * @param source Bitmap which need to be scaled
     * @return Scaled bitmap
     */
    public Bitmap getScaledBitmap1to1(Context context, Bitmap source) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int newWidth = metrics.widthPixels;
        return Bitmap.createScaledBitmap(source, newWidth, newWidth, true);
    }

}

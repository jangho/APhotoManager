package de.k3b.android.androFotoFinder.imagedetail;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.GLES20;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;

import uk.co.senab.photoview.log.LogManager;

/**
 * If image is bigger that available memory
 * load a scaled down version.
 * see http://developer.android.com/training/displaying-bitmaps/index.html.
 *
 * Created by k3b on 14.09.2015
 * for https://github.com/k3b/APhotoManager/ */
public class HugeImageLoader {
    // public to allow crash-report to filter logcat for this
    public static final String LOG_TAG = "HugeImageLoader";

    // public to allow customer settings-activity to change this
    public static boolean DEBUG = false; //!!! Log.isLoggable(LOG_TAG, Log.DEBUG);

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static int getMaxTextureSize() {
        try {
            int[] max = new int[1];
            new GLES20().glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, max, 0);

            if (max[0] > 0) {
                return max[0];
            }
        } catch (Exception ex) {

        }
        return 4096;
    }

    public static Bitmap loadImage(File file, Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Activity.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            loadImageOld(file,  windowManager);
        }
        return loadImageHoneycombMr2(file, windowManager);
    }

    private static Bitmap loadImageOld(File file, WindowManager windowManager) {
        Point outSize = new Point();
        final Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        return loadImage(file, displaymetrics.widthPixels,displaymetrics.heightPixels);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private static Bitmap loadImageHoneycombMr2(File file, WindowManager windowManager) {
        Point outSize = new Point();
        final Display display = windowManager.getDefaultDisplay();
        display.getSize(outSize);
        return loadImage(file, outSize.x, outSize.y);
    }

    public static Bitmap loadImage(File file, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        // int imageHeight = options.outHeight;
        // int imageWidth = options.outWidth;
        // String imageType = options.outMimeType;

        int downscale = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inSampleSize = downscale;

        if (DEBUG) {
            Runtime r = Runtime.getRuntime();
            int width = options.outWidth;
            int height = options.outHeight;
            LogManager.getLogger().d(
                LOG_TAG,
                "loadImage(" +
                        "\n\t'" + file +
                        "', " + width +
                        "x" + height +
                        ", max=" + maxWidth +
                        "x" + maxHeight +
                        ", size=" + (width*height*4/1024) +
                        "k, " +
                        "\n\tmemory(total/free/avail)=(" + r.totalMemory()/1024 + "k,"+ r.freeMemory()/1024+ "k,"+ r.maxMemory()/1024 +
                        "k) ) " +
                        "\n\t==> " + width/downscale +
                        "x" + height/downscale +
                        ", size=" + (width*height*4/1024/downscale/downscale) +
                        "k, scale=" + downscale);
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((height > reqHeight)
                || (width > reqWidth)) {
            inSampleSize *= 2;
            height /= 2;
            width /= 2;
        }

        return inSampleSize;
    }
}

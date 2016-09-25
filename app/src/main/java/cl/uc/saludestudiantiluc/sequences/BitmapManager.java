package cl.uc.saludestudiantiluc.sequences;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.HashSet;

/**
 * Created by jchicao on 15-09-16.
 */
public final class BitmapManager {
  private static File sFilesDir;
  private static Context sContext;

  public static void setFilesDir(File dir) {
    sFilesDir = dir;
  }
  public static void setContext(Context con) {
    sContext = con;
  }

  private static LruCache<String, Bitmap> sMemoryCache;
  private static boolean sCachedHandled;

  private static void handleCache() {
    if (!sCachedHandled) {
      final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
      final int cacheSize = maxMemory / 8;
      sMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
          return bitmap.getByteCount() / 1024;
        }
      };
      sCachedHandled = true;
    }
  }

  public static Bitmap getBitmap(String key) {
    handleCache();
    if (key == null) {
      return null;
    }
    return sMemoryCache.get(key);
  }

  public static void setBitmap(String key, Bitmap bitmap) {
    handleCache();
    if (getBitmap(key) == null) {
      sMemoryCache.put(key, bitmap);
    }
  }
  public static void loadImage(ImageView imageView, String url) {
    Bitmap bitmap = getBitmap(url);
    if (bitmap != null) {
      imageView.setImageBitmap(bitmap);
      return;
    }
    if (sFilesDir != null) {
      String[] splitted = ("Sequences/" + url).split("/");
      String str = "";
      for (int i = 0; i < splitted.length; i++) {
        str += splitted[i];
        File file = new File(sFilesDir, str);
        //File
        if (i == splitted.length - 1) {
          if (file.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (myBitmap != null) {
              imageView.setImageBitmap(myBitmap);
              return;
            }
          }
        }
        //Directories
        else {
          if (!file.exists()) {
            file.mkdir();
          }
        }
        str += "/";
      }
    }
    downloadImage(imageView, url);
  }

  private static void downloadImage(final ImageView imageView, final String url) {
    Glide
        .with(imageView.getContext())
        .load(SequencesApi.BASE_URL + url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView);
  }
}
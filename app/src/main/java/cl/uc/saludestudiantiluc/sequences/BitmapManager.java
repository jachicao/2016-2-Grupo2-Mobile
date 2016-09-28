package cl.uc.saludestudiantiluc.sequences;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import cl.uc.saludestudiantiluc.sequences.data.SequencesApi;

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
    /*
    Bitmap bitmap = getBitmap(url);
    if (bitmap != null) {
      imageView.setImageBitmap(bitmap);
      return;
    }*/
    if (sFilesDir != null) {
      String[] splitted = ("Sequences/" + url).split("/");
      String str = "";
      for (int i = 0; i < splitted.length; i++) {
        str += splitted[i];
        File file = new File(sFilesDir, str);
        //File
        if (i == splitted.length - 1) {
          if (file.exists()) {
            Glide.with(sContext).load(file.getAbsolutePath()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
            return;
          }
        } else {
          if (!file.exists()) {
            file.mkdir();
          }
        }
        str += "/";
      }
    }
    downloadImage(url, imageView);
  }

  private static void downloadImage(final String url, final ImageView imageView) {
    Glide.with(sContext).load(SequencesApi.BASE_URL + url).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        /*
        setBitmap(url, resource);
        */
        File file = new File(sContext.getFilesDir(), "Sequences/" + url);
        new BitmapWorkerTask(resource, new BitmapWorkerListener() {
          @Override
          public void onFileReady() {
            if (imageView != null) {
              loadImage(imageView, url);
            }
          }
        }).execute(file);
      }
    });
  }
}
interface BitmapWorkerListener {
  void onFileReady();
}
class BitmapWorkerTask extends AsyncTask<File, Void, Void> {
  private final WeakReference<Bitmap> mBitmapReference;
  private final WeakReference<BitmapWorkerListener> mListener;
  public BitmapWorkerTask(Bitmap resource, BitmapWorkerListener listener) {
    mBitmapReference = new WeakReference<Bitmap>(resource);
    mListener = new WeakReference<BitmapWorkerListener>(listener);
  }


  @Override
  protected Void doInBackground(File... params) {
    try {
      File file = params[0];
      file.createNewFile();
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(file);
        final Bitmap resource = mBitmapReference.get();
        if (resource != null) {
          resource.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }
        fos.close();
      } catch (Exception e) {

      }
    } catch (IOException e) {
    }
    return null;
  }
  // Once complete, see if ImageView is still around and set bitmap.
  @Override
  protected void onPostExecute(Void n) {
    if (mListener != null) {
      BitmapWorkerListener listener = mListener.get();
      if (listener != null) {
        listener.onFileReady();
      }
    }
  }
}
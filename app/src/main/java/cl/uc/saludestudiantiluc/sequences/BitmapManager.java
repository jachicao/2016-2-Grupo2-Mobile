package cl.uc.saludestudiantiluc.sequences;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

  public static Bitmap setBitmap(String key) {
    handleCache();
    if (key == null) {
      return null;
    }
    return sMemoryCache.get(key);
  }

  public static void setBitmap(String key, Bitmap bitmap) {
    handleCache();
    if (setBitmap(key) == null) {
      sMemoryCache.put(key, bitmap);
    }
  }
  public static void loadImage(ImageView imageView, String url) {
    Bitmap bitmap = BitmapManager.setBitmap(url);
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
      downloadImage(imageView, url);
    }
  }

  private static HashSet<String> sImagesDownloaded = new HashSet<>();

  private static void downloadImage(final ImageView imageView, final String url) {
    if (sImagesDownloaded.add(url)) {
      /*
      Picasso.with(sContext).load(SequencesApi.BASE_URL + url).into(new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
          sImagesDownloaded.remove(url);
          setBitmap(url, bitmap);
          imageView.setImageBitmap(bitmap);
          File file = new File(sFilesDir, "Sequences/" + url);
          try {
            file.createNewFile();
            FileOutputStream fos = null;
            try {
              fos = new FileOutputStream(file);
              // Use the compress method on the BitMap object to write image to the OutputStream
              bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
              fos.close();
            } catch (Exception e) {

            }
          } catch (IOException e) {

          }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
          sImagesDownloaded.remove(url);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
      });*/
      Call<ResponseBody> callInstance = SequencesApiInstance.getInstance().getImageBytes(url);
      callInstance.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if (response.isSuccessful()) {
            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            setBitmap(url, bitmap);
            imageView.setImageBitmap(bitmap);
            File file = new File(sFilesDir, "Sequences/" + url);
            try {
              file.createNewFile();
              FileOutputStream fos = null;
              try {
                fos = new FileOutputStream(file);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
              } catch (Exception e) {

              }
            } catch (IOException e) {

            }
          }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
          sImagesDownloaded.remove(url);
        }
      });
    }
  }
}
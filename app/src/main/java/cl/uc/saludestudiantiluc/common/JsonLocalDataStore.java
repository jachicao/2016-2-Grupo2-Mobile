package cl.uc.saludestudiantiluc.common;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import cl.uc.saludestudiantiluc.utils.JsonStoreUtils;
import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by lukas on 9/28/16.
 */

public class JsonLocalDataStore {

  private Context mContext;
  private Gson mGson;

  public JsonLocalDataStore(@NonNull Context context, @NonNull Gson gson) {
    mContext = context.getApplicationContext();
    mGson = gson;
  }

  public Gson getGson() {
    return mGson;
  }

  public Context getContext() {
    return mContext;
  }

  protected  <T> Observable<T> getObjectFromStore(final String fileName,
                                                  final TypeToken<T> typeToken) {
    return Observable.defer(new Func0<Observable<T>>() {
      @Override
      public Observable<T> call() {
        T storedObject = null;
        try {
          String jsonSequences = JsonStoreUtils.readJsonStringFromFile(mContext.getCacheDir(),
              fileName);
          storedObject = mGson.fromJson(jsonSequences,
              typeToken.getType());
        } catch (IOException e) {
          e.printStackTrace();
        }

        return Observable.just(storedObject).subscribeOn(Schedulers.io());
      }
    });
  }

  protected <T> void storeObject(T object, String fileName) {
    final String json = mGson.toJson(object);
    try {
      JsonStoreUtils.storeJsonToFile(mContext.getCacheDir(), fileName, json);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}

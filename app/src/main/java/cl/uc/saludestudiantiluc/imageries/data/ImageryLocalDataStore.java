package cl.uc.saludestudiantiluc.imageries.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cl.uc.saludestudiantiluc.common.JsonLocalDataStore;
import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import rx.Observable;

/**
 * Created by lukas on 9/28/16.
 */

public class ImageryLocalDataStore extends JsonLocalDataStore implements ImageryDataStore {

  private static final String JSON_FILENAME_IMAGERY = "imagery_cache.json";

  public ImageryLocalDataStore(@NonNull Context context, @NonNull Gson gson) {
    super(context, gson);
  }

  @Override
  public Observable<List<Imagery>> getImagerySoundList() {
    return getObjectFromStore(JSON_FILENAME_IMAGERY, new TypeToken<List<Imagery>>(){});
  }

  void storeImagerySoundList(List<Imagery> imageryImageryList) {
    storeObject(imageryImageryList, JSON_FILENAME_IMAGERY);
  }
}

package cl.uc.saludestudiantiluc.common.sounds.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cl.uc.saludestudiantiluc.common.JsonLocalDataStore;
import cl.uc.saludestudiantiluc.common.sounds.Sound;
import rx.Observable;

/**
 * Created by lukas on 9/28/16.
 */

public class SoundsLocalDataStore  extends JsonLocalDataStore implements SoundsDataStore {

  private static final String JSON_FILENAME_IMAGERY = "imagery_cache.json";
  private static final String JSON_FILENAME_AMBIENTAL_SOUNDS = "ambiental_sounds_cache.json";

  public SoundsLocalDataStore(@NonNull Context context, @NonNull Gson gson) {
    super(context, gson);
  }

  @Override
  public Observable<List<Sound>> getImagerySoundList() {
    return getObjectFromStore(JSON_FILENAME_IMAGERY, new TypeToken<List<Sound>>(){});
  }

  @Override
  public Observable<List<Sound>> getAmbientalSoundList() {
    return getObjectFromStore(JSON_FILENAME_AMBIENTAL_SOUNDS, new TypeToken<List<Sound>>(){});
  }

  void storeImagerySoundList(List<Sound> imagerySoundList) {
    storeObject(imagerySoundList, JSON_FILENAME_IMAGERY);
  }

  void storeAmbientalSoundList(List<Sound> ambientalSoundList) {
    storeObject(ambientalSoundList, JSON_FILENAME_AMBIENTAL_SOUNDS);
  }
}

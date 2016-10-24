package cl.uc.saludestudiantiluc.ambiences.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.common.JsonLocalDataStore;
import rx.Observable;

/**
 * Created by jchicao on 10/20/16.
 */

public class AmbiencesLocalDataStore extends JsonLocalDataStore implements AmbiencesDataStore {
  private static final String JSON_FILENAME = "ambiences_cache.json";

  public AmbiencesLocalDataStore(@NonNull Context context, @NonNull Gson gson) {
    super(context, gson);
  }

  @Override
  public Observable<List<Ambience>> getAmbiences() {
    return getObjectFromStore(JSON_FILENAME, new TypeToken<List<Ambience>>(){});
  }
  void store(List<Ambience> ambiences) {
    storeObject(ambiences, JSON_FILENAME);
  }
}

package cl.uc.saludestudiantiluc.exerciseplans.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import cl.uc.saludestudiantiluc.common.JsonLocalDataStore;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseResponse;
import cl.uc.saludestudiantiluc.utils.JsonStoreUtils;
import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by camilo on 29-11-16.
 */

public class CurrentExerciseLocalDataStore extends JsonLocalDataStore implements CurrentExerciseDataStore {

  private static final String JSON_BASE_FILENAME = "current_exercise_sound_cache.json";

  public CurrentExerciseLocalDataStore(@NonNull Context context, @NonNull Gson gson) {
    super(context, gson);
  }

  @Override
  public Observable<ExerciseResponse> getCurrentExercise(int planId) {
    Observable<ExerciseResponse> plans = getObjectFromStore(JSON_BASE_FILENAME + String.valueOf(planId), new TypeToken<ExerciseResponse>(){});
    return plans;
  }

  @Override
  protected  <T> Observable<T> getObjectFromStore(final String fileName,
                                                  final TypeToken<T> typeToken) {
    return Observable.defer(new Func0<Observable<T>>() {
      @Override
      public Observable<T> call() {
        T storedObject = null;
        try {
          String jsonSequences = JsonStoreUtils.readJsonStringFromFile(getContext().getCacheDir(),
              fileName);
          storedObject = getGson().fromJson(jsonSequences,
              typeToken.getType());
        } catch (IOException e) {
          e.printStackTrace();
        }

        return Observable.just(storedObject).subscribeOn(Schedulers.io());
      }
    });
  }
  void store(ExerciseResponse exercise_sounds, int planId) {
    storeObject(exercise_sounds, JSON_BASE_FILENAME + String.valueOf(planId));
  }

}

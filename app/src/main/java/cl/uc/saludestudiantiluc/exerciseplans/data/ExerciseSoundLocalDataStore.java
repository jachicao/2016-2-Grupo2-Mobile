package cl.uc.saludestudiantiluc.exerciseplans.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cl.uc.saludestudiantiluc.common.JsonLocalDataStore;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import cl.uc.saludestudiantiluc.utils.JsonStoreUtils;
import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by camilo on 10-11-16.
 */

public class ExerciseSoundLocalDataStore extends JsonLocalDataStore implements
    ExerciseSoundDataStore {
  private static final String JSON_FILENAME = "exercise_sound_cache.json";

  public ExerciseSoundLocalDataStore(@NonNull Context context, @NonNull Gson gson) {
    super(context, gson);
  }

  @Override
  public Observable<List<ExercisePlan>> getExerciseSounds() {
    Observable<List<ExercisePlan>> plans = getObjectFromStore(JSON_FILENAME, new TypeToken<List<ExercisePlan>>(){});
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
          //storedObject = new ArrayList<ExercisePlan>(Arrays.asList(getGson().fromJson(jsonSequences, ExerciseSound[].class)));
          //String a = getGson().toJson(jsonSequences);
          //System.out.println(a);
        } catch (IOException e) {
          e.printStackTrace();
        }

        return Observable.just(storedObject).subscribeOn(Schedulers.io());
      }
    });
  }
  void store(List<ExercisePlan> exercise_sounds) {
    storeObject(exercise_sounds, JSON_FILENAME);
  }
}

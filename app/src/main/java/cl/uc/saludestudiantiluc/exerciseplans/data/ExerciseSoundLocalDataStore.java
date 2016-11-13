package cl.uc.saludestudiantiluc.exerciseplans.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import cl.uc.saludestudiantiluc.common.JsonLocalDataStore;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import rx.Observable;

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
  public Observable<List<ExerciseSound>> getExerciseSounds() {
    return getObjectFromStore(JSON_FILENAME, new TypeToken<List<ExerciseSound>>(){});
  }
  void store(List<ExerciseSound> exercise_sounds) {
    storeObject(exercise_sounds, JSON_FILENAME);
  }
}

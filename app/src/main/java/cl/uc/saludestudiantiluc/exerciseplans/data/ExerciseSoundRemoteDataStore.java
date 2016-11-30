package cl.uc.saludestudiantiluc.exerciseplans.data;

import java.util.List;

import cl.uc.saludestudiantiluc.ambiences.api.AmbienceApi;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.exerciseplans.api.ExerciseSoundApi;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import rx.Observable;

/**
 * Created by camilo on 10-11-16.
 */

public class ExerciseSoundRemoteDataStore implements ExerciseSoundDataStore{
  private ExerciseSoundApi mApi;
  public ExerciseSoundRemoteDataStore(ExerciseSoundApi api) {
    mApi = api;
  }

  @Override
  public Observable<List<ExercisePlan>> getExerciseSounds() {
    return mApi.getExerciseSounds();
  }
}

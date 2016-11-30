package cl.uc.saludestudiantiluc.exerciseplans.data;

import cl.uc.saludestudiantiluc.exerciseplans.api.ExerciseProgramApi;
import cl.uc.saludestudiantiluc.exerciseplans.api.ExerciseSoundApi;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseResponse;
import rx.Observable;

/**
 * Created by camilo on 29-11-16.
 */

public class CurrentExerciseRemoteDataStore implements CurrentExerciseDataStore {
  private ExerciseProgramApi mApi;
  public CurrentExerciseRemoteDataStore(ExerciseProgramApi api) {
    mApi = api;
  }

  @Override
  public Observable<ExerciseResponse> getCurrentExercise(int planId) {
    return mApi.getCurrentSound(planId);
  }
}

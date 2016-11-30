package cl.uc.saludestudiantiluc.exerciseplans.data;

import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseResponse;
import rx.Observable;

/**
 * Created by camilo on 29-11-16.
 */

public interface CurrentExerciseDataStore {
  Observable<ExerciseResponse> getCurrentExercise(int planId);
}

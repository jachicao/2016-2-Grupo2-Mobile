package cl.uc.saludestudiantiluc.exerciseplans.data;

import java.util.List;

import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseResponse;
import rx.Observable;

/**
 * Created by camilo on 29-11-16.
 */

public interface CurrentExerciseRepository {
  Observable<ExerciseResponse> getCurrentExercise(int planId);
}

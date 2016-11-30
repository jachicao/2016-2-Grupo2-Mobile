package cl.uc.saludestudiantiluc.exerciseplans.data;

import java.util.List;

import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import rx.Observable;

/**
 * Created by camilo on 10-11-16.
 */

public interface ExerciseSoundRepository {
  Observable<List<ExercisePlan>> getExerciseSounds();
}

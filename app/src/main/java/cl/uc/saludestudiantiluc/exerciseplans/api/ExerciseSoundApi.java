package cl.uc.saludestudiantiluc.exerciseplans.api;

import java.util.List;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by camilo on 10-11-16.
 */

public interface ExerciseSoundApi {
  String BASE_URL = BuildConfig.HOST + "/api/v1/"; //TODO: change URL

  @GET("content/programs")//TODO: change URL
  Observable<List<ExerciseSound>> getExerciseSounds();
}

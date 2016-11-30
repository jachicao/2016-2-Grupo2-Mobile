package cl.uc.saludestudiantiluc.exerciseplans.api;

import java.util.List;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSoundData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by camilo on 10-11-16.
 */

public interface ExerciseSoundApi {
  String BASE_URL = BuildConfig.HOST + "/api/v1/"; //TODO: change URL

  @GET("content/programs")//TODO: change URL
  Observable<List<ExercisePlan>> getExerciseSounds();

}

package cl.uc.saludestudiantiluc.exerciseplans.api;

import java.util.List;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by camilo on 13-11-16.
 */

public interface ExerciseProgramApi {
  String BASE_URL = BuildConfig.HOST + "/api/v1/"; //TODO: change URL

  @GET("content/programs")//TODO: change URL
  Call<List<ExercisePlan>> getExercisePrograms();
}

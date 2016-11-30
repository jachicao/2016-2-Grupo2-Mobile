package cl.uc.saludestudiantiluc.exerciseplans.api;

import java.util.List;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseResponse;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSoundData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by camilo on 13-11-16.
 */

public interface ExerciseProgramApi {
  String BASE_URL = BuildConfig.HOST + "/api/v1/"; //TODO: change URL

  @GET("programs/{id}")//TODO: change URL
  Observable<ExerciseResponse> getCurrentSound(@Path("id") int id);

  @FormUrlEncoded
  @PUT("programs/{id}")//TODO: change URL
  Call<Void> updateCurrentSound(@Path("id") int planId, @Field("current") int currentExerciseId);
}

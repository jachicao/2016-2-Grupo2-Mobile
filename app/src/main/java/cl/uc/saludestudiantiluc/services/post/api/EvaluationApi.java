package cl.uc.saludestudiantiluc.services.post.api;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.services.post.models.Evaluation;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Junior on 16-11-2016.
 */

public interface EvaluationApi {

  String BASE_URL = BuildConfig.HOST + "/api/v1/survey/";

  @POST("GAD7")
  Call<Evaluation> sendGAD7(@Query("score") int score);

  @POST("stress")
  Call<Evaluation> sendStress(@Query("score") int score);

  @POST("sleep")
  Call<Evaluation> sendSleep(@Query("score") int score);
}

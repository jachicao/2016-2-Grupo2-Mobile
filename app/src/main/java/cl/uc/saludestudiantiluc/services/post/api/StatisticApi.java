package cl.uc.saludestudiantiluc.services.post.api;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.services.post.models.Statistic;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jchicao on 10/29/16.
 */

public interface StatisticApi {

  String BASE_URL = BuildConfig.HOST + "/api/v1/logs/";

  @POST("nature")
  Call<Statistic> sendAmbienceLog(@Query("id") int id);

  @POST("long_audio")
  Call<Statistic> sendImageryLog(@Query("id") int id);

  @POST("infographics")
  Call<Statistic> sendSequenceLog(@Query("id") int id);

}

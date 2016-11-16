package cl.uc.saludestudiantiluc.ambiences.api;

import java.util.List;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by jchicao on 10/20/16.
 */

public interface AmbienceApi {
  String BASE_URL = BuildConfig.HOST + "/api/v1/content/";

  @GET("nature")
  Observable<List<Ambience>> getAmbiences();
}
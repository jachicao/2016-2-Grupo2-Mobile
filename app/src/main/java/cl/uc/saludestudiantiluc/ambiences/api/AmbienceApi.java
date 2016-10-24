package cl.uc.saludestudiantiluc.ambiences.api;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by jchicao on 10/20/16.
 */

public interface AmbienceApi {
  String BASE_URL = "https://raw.githubusercontent.com/jachicao/Testing/master/";

  @GET("ambientals.json")
  Observable<List<cl.uc.saludestudiantiluc.ambiences.models.Ambience>> getAmbientals();
}
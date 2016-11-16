package cl.uc.saludestudiantiluc.imageries.api;

import java.util.List;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by camilo on 20-09-16.
 */
public interface ImageryApi {

  String BASE_URL = BuildConfig.HOST + "/api/v1/content/";

  @GET("imagineries")
  Observable<List<Imagery>> getImagerySoundList();

}

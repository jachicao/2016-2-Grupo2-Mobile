package cl.uc.saludestudiantiluc.imageries.api;

import java.util.List;

import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by camilo on 20-09-16.
 */
public interface ImageryApi {

  String BASE_URL = "https://raw.githubusercontent.com/jachicao/Testing/master/";

  @GET("imagery.json")
  Observable<List<Imagery>> getImagerySoundList();

  @GET("ambiental.json")
  Observable<List<Imagery>> getAmbientalSoundList();

}

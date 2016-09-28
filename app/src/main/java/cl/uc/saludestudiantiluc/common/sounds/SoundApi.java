package cl.uc.saludestudiantiluc.common.sounds;

import java.util.List;

import cl.uc.saludestudiantiluc.common.sounds.Sound;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by camilo on 20-09-16.
 */
public interface SoundApi {

  String BASE_URL = "https://raw.githubusercontent.com/jachicao/Testing/master/";

  @GET("imagery.json")
  Call<List<Sound>> getImagerySoundList();

  @GET("ambiental.json")
  Call<List<Sound>> getAmbientalSoundList();

}

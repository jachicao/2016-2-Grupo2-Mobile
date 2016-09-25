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

  public static final String BASE_URL = "http://private-6d312d-sound.apiary-mock.com";

  @GET("/imagery")
  Call<List<Sound>> getImagerySoundList();

  @GET("/ambiental")
  Call<List<Sound>> getAmbientalSoundList();

  @GET("sounds/{soundId}")
  Call<Sound> getSound(@Path("soundId") String soundId);

}

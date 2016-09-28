package cl.uc.saludestudiantiluc.common.sounds.data;

import java.util.List;

import cl.uc.saludestudiantiluc.common.sounds.Sound;
import cl.uc.saludestudiantiluc.common.sounds.SoundApi;
import rx.Observable;

/**
 * Created by lukas on 9/28/16.
 */

public class SoundsRemoteDataStore implements SoundsDataStore {

  private SoundApi mSoundApi;

  public SoundsRemoteDataStore(SoundApi soundApi) {
    mSoundApi = soundApi;
  }

  @Override
  public Observable<List<Sound>> getImagerySoundList() {
    return mSoundApi.getImagerySoundList();
  }

  @Override
  public Observable<List<Sound>> getAmbientalSoundList() {
    return mSoundApi.getAmbientalSoundList();
  }
}

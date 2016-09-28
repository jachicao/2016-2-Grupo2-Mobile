package cl.uc.saludestudiantiluc.common.sounds.data;

import java.util.List;

import cl.uc.saludestudiantiluc.common.sounds.Sound;
import rx.Observable;

/**
 * Created by lukas on 9/28/16.
 */
public interface SoundsRepository {

  Observable<List<Sound>> getImagerySoundList();

  Observable<List<Sound>> getAmbientalSoundList();
}

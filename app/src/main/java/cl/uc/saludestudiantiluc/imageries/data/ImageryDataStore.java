package cl.uc.saludestudiantiluc.imageries.data;

import java.util.List;

import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import rx.Observable;

/**
 * Created by lukas on 9/28/16.
 */

public interface ImageryDataStore {

  Observable<List<Imagery>> getImagerySoundList();

}

package cl.uc.saludestudiantiluc.ambiences.data;

import java.util.List;

import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import rx.Observable;

/**
 * Created by jchicao on 10/20/16.
 */

interface AmbiencesDataStore {

  Observable<List<Ambience>> getAmbiences();
}

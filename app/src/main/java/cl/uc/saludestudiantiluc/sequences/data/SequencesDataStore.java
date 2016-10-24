package cl.uc.saludestudiantiluc.sequences.data;

import java.util.List;

import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import rx.Observable;

/**
 * Created by lukas on 9/27/16.
 */

interface SequencesDataStore {

  Observable<List<Sequence>> getSequences();
}

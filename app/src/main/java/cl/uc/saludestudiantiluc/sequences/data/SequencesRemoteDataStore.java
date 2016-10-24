package cl.uc.saludestudiantiluc.sequences.data;

import java.util.List;

import cl.uc.saludestudiantiluc.sequences.api.SequencesApi;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import rx.Observable;

/**
 * Created by lukas on 9/27/16.
 */

public class SequencesRemoteDataStore implements SequencesDataStore {

  private SequencesApi mSequencesApi;

  public SequencesRemoteDataStore(SequencesApi sequencesApi) {
    mSequencesApi = sequencesApi;
  }

  @Override
  public Observable<List<Sequence>> getSequences() {
    return mSequencesApi.getSequences();
  }
}

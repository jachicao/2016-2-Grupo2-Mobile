package cl.uc.saludestudiantiluc.sequences.data;

import android.util.Log;

import java.util.List;

import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lukas on 9/27/16.
 */

public class SequencesDataRepository implements SequencesRepository {

  private SequencesLocalDataStore mLocalDataStore;
  private SequencesRemoteDataStore mRemoteDataStore;

  public SequencesDataRepository(SequencesLocalDataStore localDataStore,
                                 SequencesRemoteDataStore remoteDataStore) {
    mLocalDataStore = localDataStore;
    mRemoteDataStore = remoteDataStore;
  }

  @Override
  public Observable<List<Sequence>> getSequences() {
    Observable<List<Sequence>> localSequences = mLocalDataStore.getSequences()
        .filter(new Func1<List<Sequence>, Boolean>() {
          @Override
          public Boolean call(List<Sequence> sequences) {
            return sequences != null;
          }
        })
        .subscribeOn(Schedulers.io());
    Observable<List<Sequence>> remoteSequences = mRemoteDataStore.getSequences()
        .onErrorReturn(new Func1<Throwable, List<Sequence>>() {
          @Override
          public List<Sequence> call(Throwable throwable) {
            Log.e(SequencesDataRepository.class.getSimpleName(),
                "Error while fetching data. Swallowing the exception.", throwable);
            return null;
          }
        })
        .filter(new Func1<List<Sequence>, Boolean>() {
          @Override
          public Boolean call(List<Sequence> sequences) {
            return sequences != null;
          }
        })
        .doOnNext(new Action1<List<Sequence>>() {
          @Override
          public void call(List<Sequence> sequences) {
            mLocalDataStore.storeSequences(sequences);
          }
        }).subscribeOn(Schedulers.io());

    return Observable.concat(localSequences, remoteSequences);
  }

}

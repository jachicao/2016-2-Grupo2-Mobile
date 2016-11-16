package cl.uc.saludestudiantiluc.ambiences.data;

import android.util.Log;

import java.util.List;

import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jchicao on 10/20/16.
 */

public class AmbiencesDataRepository implements AmbiencesRepository {
  private AmbiencesLocalDataStore mLocalDataStore;
  private AmbiencesRemoteDataStore mRemoteDataStore;

  public AmbiencesDataRepository(AmbiencesLocalDataStore localDataStore, AmbiencesRemoteDataStore remoteDataStore) {
    mLocalDataStore = localDataStore;
    mRemoteDataStore = remoteDataStore;
  }
  @Override
  public Observable<List<Ambience>> getAmbiences() {
    Observable<List<Ambience>> local = mLocalDataStore.getAmbiences()
        .filter(new Func1<List<Ambience>, Boolean>() {
          @Override
          public Boolean call(List<Ambience> ambiences) {
            return ambiences != null;
          }
        })
        .subscribeOn(Schedulers.io());
    Observable<List<Ambience>> remote = mRemoteDataStore.getAmbiences()
        .onErrorReturn(new Func1<Throwable, List<Ambience>>() {
          @Override
          public List<Ambience> call(Throwable throwable) {
            Log.e(AmbiencesDataRepository.class.getSimpleName(),
                "Error while fetching data. Swallowing the exception.", throwable);
            return null;
          }
        })
        .filter(new Func1<List<Ambience>, Boolean>() {
          @Override
          public Boolean call(List<Ambience> ambiences) {
            return ambiences != null;
          }
        })
        .doOnNext(new Action1<List<Ambience>>() {
          @Override
          public void call(List<Ambience> ambiences) {
            mLocalDataStore.store(ambiences);
          }
        })
        .subscribeOn(Schedulers.io());
    return Observable.concat(local, remote);
  }
}

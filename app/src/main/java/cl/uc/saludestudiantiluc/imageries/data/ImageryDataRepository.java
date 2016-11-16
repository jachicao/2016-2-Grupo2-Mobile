package cl.uc.saludestudiantiluc.imageries.data;

import android.util.Log;

import java.util.List;

import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lukas on 9/28/16.
 */

public class ImageryDataRepository implements ImageryRepository {

  private ImageryLocalDataStore mSoundsLocalDataStore;
  private ImageryRemoteDataStore mSoundsRemoteDataStore;

  public ImageryDataRepository(ImageryLocalDataStore soundsLocalDataStore,
                               ImageryRemoteDataStore soundsRemoteDataStore) {
    mSoundsLocalDataStore = soundsLocalDataStore;
    mSoundsRemoteDataStore = soundsRemoteDataStore;
  }
  @Override
  public Observable<List<Imagery>> getImagerySoundList() {
    Observable<List<Imagery>> localData = mSoundsLocalDataStore.getImagerySoundList()
        .filter(new Func1<List<Imagery>, Boolean>() {
          @Override
          public Boolean call(List<Imagery> sounds) {
            return sounds != null;
          }
        }).subscribeOn(Schedulers.io());
    Observable<List<Imagery>> remoteData = mSoundsRemoteDataStore.getImagerySoundList()
        .onErrorReturn(new Func1<Throwable, List<Imagery>>() {
          @Override
          public List<Imagery> call(Throwable throwable) {
            Log.e(ImageryDataRepository.class.getSimpleName(),
                "Error while fetching data. Swallowing the exception.", throwable);
            return null;
          }
        })
        .filter(new Func1<List<Imagery>, Boolean>() {
          @Override
          public Boolean call(List<Imagery> sounds) {
            return sounds != null;
          }
        }).doOnNext(new Action1<List<Imagery>>() {
          @Override
          public void call(List<Imagery> sounds) {
            mSoundsLocalDataStore.storeImagerySoundList(sounds);
          }
        })
        .subscribeOn(Schedulers.io());

    return Observable.concat(localData, remoteData);
  }
}

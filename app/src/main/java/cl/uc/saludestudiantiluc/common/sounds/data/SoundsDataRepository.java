package cl.uc.saludestudiantiluc.common.sounds.data;

import android.util.Log;

import java.util.List;

import cl.uc.saludestudiantiluc.common.sounds.Sound;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lukas on 9/28/16.
 */

public class SoundsDataRepository implements SoundsRepository {

  private SoundsLocalDataStore mSoundsLocalDataStore;
  private SoundsRemoteDataStore mSoundsRemoteDataStore;

  public SoundsDataRepository(SoundsLocalDataStore soundsLocalDataStore,
                              SoundsRemoteDataStore soundsRemoteDataStore) {
    mSoundsLocalDataStore = soundsLocalDataStore;
    mSoundsRemoteDataStore = soundsRemoteDataStore;
  }
  @Override
  public Observable<List<Sound>> getImagerySoundList() {
    Observable<List<Sound>> localData = mSoundsLocalDataStore.getImagerySoundList()
        .filter(new Func1<List<Sound>, Boolean>() {
          @Override
          public Boolean call(List<Sound> sounds) {
            return sounds != null;
          }
        }).subscribeOn(Schedulers.io());
    Observable<List<Sound>> remoteData = mSoundsRemoteDataStore.getImagerySoundList()
        .onErrorReturn(new Func1<Throwable, List<Sound>>() {
          @Override
          public List<Sound> call(Throwable throwable) {
            Log.e(SoundsDataRepository.class.getSimpleName(),
                "Error while fetching data. Swallowing the exception.", throwable);
            return null;
          }
        }).filter(new Func1<List<Sound>, Boolean>() {
          @Override
          public Boolean call(List<Sound> sounds) {
            return sounds != null;
          }
        }).doOnNext(new Action1<List<Sound>>() {
          @Override
          public void call(List<Sound> sounds) {
            mSoundsLocalDataStore.storeImagerySoundList(sounds);
          }
        })
        .subscribeOn(Schedulers.io());

    return Observable.concat(localData, remoteData);
  }

  @Override
  public Observable<List<Sound>> getAmbientalSoundList() {
    Observable<List<Sound>> localData = mSoundsLocalDataStore.getAmbientalSoundList()
        .filter(new Func1<List<Sound>, Boolean>() {
          @Override
          public Boolean call(List<Sound> sounds) {
            return sounds != null;
          }
        }).subscribeOn(Schedulers.io());
    Observable<List<Sound>> remoteData = mSoundsRemoteDataStore.getAmbientalSoundList()
        .onErrorReturn(new Func1<Throwable, List<Sound>>() {
          @Override
          public List<Sound> call(Throwable throwable) {
            Log.e(SoundsDataRepository.class.getSimpleName(),
                "Error while fetching data. Swallowing the exception.", throwable);
            return null;
          }
        }).filter(new Func1<List<Sound>, Boolean>() {
          @Override
          public Boolean call(List<Sound> sounds) {
            return sounds != null;
          }
        }).doOnNext(new Action1<List<Sound>>() {
          @Override
          public void call(List<Sound> sounds) {
            mSoundsLocalDataStore.storeAmbientalSoundList(sounds);
          }
        })
        .subscribeOn(Schedulers.io());

    return Observable.concat(localData, remoteData);
  }
}

package cl.uc.saludestudiantiluc.exerciseplans.data;

import android.util.Log;

import java.util.List;

import cl.uc.saludestudiantiluc.ambiences.data.AmbiencesLocalDataStore;
import cl.uc.saludestudiantiluc.ambiences.data.AmbiencesRemoteDataStore;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by camilo on 10-11-16.
 */

public class ExerciseSoundDataRepository implements ExerciseSoundRepository{
  private ExerciseSoundLocalDataStore mLocalDataStore;
  private ExerciseSoundRemoteDataStore mRemoteDataStore;

  public ExerciseSoundDataRepository(ExerciseSoundLocalDataStore localDataStore, ExerciseSoundRemoteDataStore remoteDataStore) {
    mLocalDataStore = localDataStore;
    mRemoteDataStore = remoteDataStore;
  }
  @Override
  public Observable<List<ExerciseSound>> getExerciseSounds() {
    Observable<List<ExerciseSound>> local = mLocalDataStore.getExerciseSounds()
        .filter(new Func1<List<ExerciseSound>, Boolean>() {
          @Override
          public Boolean call(List<ExerciseSound> exerciseSounds) {
            return exerciseSounds != null;
          }
        })
        .subscribeOn(Schedulers.io());
    Observable<List<ExerciseSound>> remote = mRemoteDataStore.getExerciseSounds()
        .onErrorReturn(new Func1<Throwable, List<ExerciseSound>>() {
          @Override
          public List<ExerciseSound> call(Throwable throwable) {
            Log.e(ExerciseSoundRemoteDataStore.class.getSimpleName(),
                "Error while fetching data. Swallowing the exception.", throwable);
            return null;

          }
        })
        .filter(new Func1<List<ExerciseSound>, Boolean>() {
          @Override
          public Boolean call(List<ExerciseSound> exerciseSounds) {
            return exerciseSounds != null;
          }
        })
        .doOnNext(new Action1<List<ExerciseSound>>() {
          @Override
          public void call(List<ExerciseSound> exerciseSounds) {
            mLocalDataStore.store(exerciseSounds);
          }
        })
        .subscribeOn(Schedulers.io());
    return Observable.concat(local, remote);
  }
}

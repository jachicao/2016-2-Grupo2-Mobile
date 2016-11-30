package cl.uc.saludestudiantiluc.exerciseplans.data;

import android.util.Log;

import java.util.List;

import cl.uc.saludestudiantiluc.ambiences.data.AmbiencesLocalDataStore;
import cl.uc.saludestudiantiluc.ambiences.data.AmbiencesRemoteDataStore;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
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
  public Observable<List<ExercisePlan>> getExerciseSounds() {
    Observable<List<ExercisePlan>> local = mLocalDataStore.getExerciseSounds()
        .filter(new Func1<List<ExercisePlan>, Boolean>() {
          @Override
          public Boolean call(List<ExercisePlan> exerciseSounds) {
            return exerciseSounds != null;
          }
        })
        .subscribeOn(Schedulers.io());
    Observable<List<ExercisePlan>> remote = mRemoteDataStore.getExerciseSounds()
        .onErrorReturn(new Func1<Throwable, List<ExercisePlan>>() {
          @Override
          public List<ExercisePlan> call(Throwable throwable) {
            Log.e(ExerciseSoundRemoteDataStore.class.getSimpleName(),
                "Error while fetching data. Swallowing the exception.", throwable);
            return null;

          }
        })
        .filter(new Func1<List<ExercisePlan>, Boolean>() {
          @Override
          public Boolean call(List<ExercisePlan> exerciseSounds) {
            return exerciseSounds != null;
          }
        })
        .doOnNext(new Action1<List<ExercisePlan>>() {
          @Override
          public void call(List<ExercisePlan> exerciseSounds) {
            mLocalDataStore.store(exerciseSounds);
          }
        })
        .subscribeOn(Schedulers.io());
    return Observable.concat(local, remote);
  }
}

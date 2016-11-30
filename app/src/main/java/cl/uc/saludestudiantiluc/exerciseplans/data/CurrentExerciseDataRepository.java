package cl.uc.saludestudiantiluc.exerciseplans.data;

import android.util.Log;

import java.util.List;

import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseResponse;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by camilo on 29-11-16.
 */

public class CurrentExerciseDataRepository implements CurrentExerciseRepository {
  private CurrentExerciseLocalDataStore mLocalDataStore;
  private CurrentExerciseRemoteDataStore mRemoteDataStore;

  public CurrentExerciseDataRepository(CurrentExerciseLocalDataStore localDataStore, CurrentExerciseRemoteDataStore remoteDataStore) {
    mLocalDataStore = localDataStore;
    mRemoteDataStore = remoteDataStore;
  }
  @Override
  public Observable<ExerciseResponse> getCurrentExercise(final int planId) {
    Observable<ExerciseResponse> local = mLocalDataStore.getCurrentExercise(planId)
        .filter(new Func1<ExerciseResponse, Boolean>() {
          @Override
          public Boolean call(ExerciseResponse exerciseSounds) {
            return exerciseSounds != null;
          }
        })
        .subscribeOn(Schedulers.io());
    Observable<ExerciseResponse> remote = mRemoteDataStore.getCurrentExercise(planId)
        .onErrorReturn(new Func1<Throwable, ExerciseResponse>() {
          @Override
          public ExerciseResponse call(Throwable throwable) {
            Log.e(CurrentExerciseRemoteDataStore.class.getSimpleName(),
                "Error while fetching data. Swallowing the exception.", throwable);
            return null;

          }
        })
        .filter(new Func1<ExerciseResponse, Boolean>() {
          @Override
          public Boolean call(ExerciseResponse exerciseSounds) {
            return exerciseSounds != null;
          }
        })
        .doOnNext(new Action1<ExerciseResponse>() {
          @Override
          public void call(ExerciseResponse exerciseSounds) {
            mLocalDataStore.store(exerciseSounds, planId);
          }
        })
        .subscribeOn(Schedulers.io());
    return Observable.concat(local, remote);
  }
}

package cl.uc.saludestudiantiluc.exerciseplans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.MediaListFragment;
import cl.uc.saludestudiantiluc.exerciseplans.api.ExerciseProgramApi;
import cl.uc.saludestudiantiluc.exerciseplans.data.CurrentExerciseRepository;
import cl.uc.saludestudiantiluc.exerciseplans.data.ExerciseSoundRepository;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseResponse;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSoundData;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class ExercisePlanMenu extends MediaListFragment {

  private RecyclerView mRecyclerView;
  private View mView;
  public static final String TAG = ExercisePlanMenu.class.getSimpleName();
  public static final String EXERCISE_EXTRAS_SOUND = "ExerciseSound";
  public static final String EXERCISE_EXTRAS_PLAN = "ExercisePlanId";
  public static final String EXERCISE_EXTRAS_ORDER = "ExercisePlanOrder";
  public static final String EXERCISE_EXTRAS_LIST = "ExerciseList";
  public static final String EXERCISES_DOWNLOADED = "ExerciseDownloaded";

  private List<ExercisePlan> mExercises = new ArrayList<>();
  private boolean mLoaded = false;
  private ExerciseExpandableAdapter mExerciseExpandableAdapter;
  private ExerciseProgramApi mExerciseProgramApi;
  private ExerciseSoundRepository mExerciseSoundRepository;

  public static Fragment newInstance() {
    return new ExercisePlanMenu();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mView = inflater.inflate(R.layout.activity_exercise_plan_menu, container, false);
    return mView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (savedInstanceState != null) {
      mLoaded = savedInstanceState.getBoolean(EXERCISES_DOWNLOADED);
      mExercises = savedInstanceState.getParcelableArrayList(EXERCISE_EXTRAS_LIST);
    }
    mExerciseSoundRepository = getBaseActivity().getRelaxUcApplication().getExerciseSoundRepository();
    mExerciseProgramApi = getBaseActivity().getRelaxUcApplication().getExerciseSoundService();
    if (mLoaded) {
      setView();
    } else {
      getListFromRepository();
    }
  }

  private void getListFromRepository() {

    mExerciseSoundRepository
        .getExerciseSounds()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<ExercisePlan>>() {
          @Override
          public void onCompleted() {
            Log.d(TAG, "onCompleted");
            setView();
          }

          @Override
          public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            showSnackbarMessage(getString(R.string.failed_download_json));
          }

          @Override
          public void onNext(List<ExercisePlan> exercises) {
            Log.d(TAG, "onNext");
            mExercises.clear();
            //if (mExercises.indexOf(exercises) < 0) {
            //  mExercises.add(exercises);
            //}
            mExercises.addAll(exercises);
            //mExerciseExpandableAdapter.notifyDataSetChanged();
          }
        });
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    if (mExercises != null) {
      mLoaded = true;
      outState.putParcelableArrayList(EXERCISE_EXTRAS_LIST, (ArrayList<? extends Parcelable>) mExercises);
      outState.putBoolean(EXERCISES_DOWNLOADED, mLoaded);
    }
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public void setView() {
    if (isAdded()) {
      if (mExercises.size() > 0) {
        mLoaded = true;
        ArrayList<ParentObject> exercisePlanList = downloadExercisePlans();
        mExerciseExpandableAdapter = new ExerciseExpandableAdapter(getContext(),
            exercisePlanList, this);
        mExerciseExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        mExerciseExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        mExerciseExpandableAdapter.setParentAndIconExpandOnClick(true);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.exercise_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mExerciseExpandableAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      } else {
        showSnackbarMessage(getString(R.string.unsuccessful_error));
      }
    }
  }

  public ArrayList<ParentObject> downloadExercisePlans() {
    ArrayList<ParentObject> parentObjects = new ArrayList<>();
    for (ExercisePlan exp: mExercises) {
      ArrayList<Object> childList = new ArrayList<>();
      for (ExerciseSound exs: exp.getExercises()) {
        ExerciseChild child = new ExerciseChild(exs, exp);
        childList.add(child);
      }
      exp.setChildObjectList(childList);
      parentObjects.add(exp);
    }
    return parentObjects;
  }

  public void loadActivity(ExerciseSoundData exerciseSound, int planId, int order) {

    if (DownloadService.containsFiles(getContext(), exerciseSound.getFilesRequest())) {
      Intent intent = new Intent(getActivity(), ExercisePlanActivity.class);
      intent.putExtra(EXERCISE_EXTRAS_SOUND, exerciseSound);
      intent.putExtra(EXERCISE_EXTRAS_PLAN, planId);
      intent.putExtra(EXERCISE_EXTRAS_ORDER, order);
      startActivity(intent);
    } else {
      showSnackbarMessage(getString(R.string.file_not_downloaded));
    }

  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, ExercisePlanMenu.class);
  }

  public ExerciseProgramApi getExerciseApi() {
    return mExerciseProgramApi;
  }

  public List<ExercisePlan> getExercisePlans() {
    return mExercises;
  }

  public CurrentExerciseRepository getCurrentRepository() {
    return getBaseActivity().getRelaxUcApplication().getCurrentExerciseRepository();
  }
}


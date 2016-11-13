package cl.uc.saludestudiantiluc.exerciseplans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.ambiences.AmbienceActivity;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.calendar.CalendarApi;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.exerciseplans.api.ExerciseSoundApi;
import cl.uc.saludestudiantiluc.exerciseplans.data.ExerciseSoundRepository;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class ExercisePlanMenu extends BaseActivity {

  private RecyclerView mRecyclerView;
  public static final String TAG = ExercisePlanMenu.class.getSimpleName();
  public static final String EXERCISE_EXTRAS_LIST = "AmbienceList";
  public static final String EXERCISE_EXTRAS_INDEX = "AmbienceIndex";
  private List<ExerciseSound> mExercises = new ArrayList<>();
  private ExerciseSoundRepository mExerciseSoundRepository;
  private ExerciseExpandableAdapter mExerciseExpandableAdapter;
  private ExerciseSoundApi mApiInstance;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exercise_plan_menu);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.main_menu_exercise_plans);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
    getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });
    Glide
        .with(this)
        .load(R.drawable.main_background)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .centerCrop()
        .into((ImageView) findViewById(R.id.main_background_image));

    mExerciseSoundRepository = getRelaxUcApplication()
        .getExerciseSoundRepository();
    getListFromRepository();
    ArrayList<ParentObject> exercisePlanList = downloadExercisePlans();
    mExerciseExpandableAdapter = new ExerciseExpandableAdapter(this,
        exercisePlanList, this);
    mExerciseExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
    mExerciseExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
    mExerciseExpandableAdapter.setParentAndIconExpandOnClick(true);

    mRecyclerView = (RecyclerView) findViewById(R.id.exercise_recycler_view);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setAdapter(mExerciseExpandableAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  private void getListFromRepository() {
    mExerciseSoundRepository
        .getExerciseSounds()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<ExerciseSound>>() {
          @Override
          public void onCompleted() {
            Log.d(TAG, "onCompleted");
          }

          @Override
          public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            notifyMessage(getString(R.string.failed_download_json));
          }

          @Override
          public void onNext(List<ExerciseSound> exercises) {
            Log.d(TAG, "onNext");
            mExercises.clear();
            mExercises.addAll(exercises);
            mExerciseExpandableAdapter.notifyDataSetChanged();
          }
        });
  }

  public ArrayList<ParentObject> downloadExercisePlans() {
    ArrayList<ParentObject> parentObjects = new ArrayList<>();

    ArrayList<Object> childList1 = new ArrayList<>();
    childList1.add(new ExerciseChild("Día 1",true));
    childList1.add(new ExerciseChild("Día 2",false));
    childList1.add(new ExerciseChild("Día 3",false));
    childList1.add(new ExerciseChild("Día 4",false));
    childList1.add(new ExerciseChild("Día 5",false));
    ExercisePlan e1 = new ExercisePlan("Plan de 5 días", childList1);
    parentObjects.add(e1);
    ArrayList<Object> childList2 = new ArrayList<>();
    childList2.add(new ExerciseChild("10 días",true));
    ExercisePlan e2 = new ExercisePlan("Plan de 10 días", childList2);
    parentObjects.add(e2);
    ArrayList<Object> childList3 = new ArrayList<>();
    childList3.add(new ExerciseChild("10 días",true));
    ExercisePlan e3 = new ExercisePlan("Plan de 7 días", childList3);
    parentObjects.add(e3);
    return parentObjects;
  }

  public void loadActivity(ExerciseSound exerciseSound) {
    /*int index = -1;
    ArrayList<ExerciseSound> downloadedList = new ArrayList<>();
    for(ExerciseSound exc : mExercises) {
      if (exc.equals(exerciseSound)) {
        index = downloadedList.size();
      }
      if (DownloadService.containsFiles(this, exc.getFilesRequest())) {
        downloadedList.add(exc);
      }
    }
    if (downloadedList.size() > 0) {
      if (index > -1 && downloadedList.contains(exerciseSound)) {
        Intent intent = new Intent(this, AmbienceActivity.class);
        intent.putExtra(EXERCISE_EXTRAS_INDEX, index);
        intent.putParcelableArrayListExtra(EXERCISE_EXTRAS_LIST, downloadedList);
        startActivity(intent);
      } else {
        notifyMessage(getString(R.string.file_not_downloaded));
      }
    } else {
      notifyMessage(getString(R.string.zero_files_downloaded));
    }*/
  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, ExercisePlanMenu.class);
  }

  public List<ExerciseSound> getDetailedList() {
    return mExercises;
  }
}


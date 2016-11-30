package cl.uc.saludestudiantiluc.exerciseplans;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.MediaListAdapter;
import cl.uc.saludestudiantiluc.common.MediaListFragment;
import cl.uc.saludestudiantiluc.exerciseplans.data.CurrentExerciseRepository;
import cl.uc.saludestudiantiluc.exerciseplans.data.ExerciseSoundRepository;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseResponse;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSoundData;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.download.FilesListener;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by camilo on 06-11-16.
 */

public class ExerciseExpandableAdapter extends ExpandableRecyclerAdapter<ExerciseParentViewHolder, ExerciseChildViewHolder> {
  private LayoutInflater mInflater;
  private Context mContext;
  private ExercisePlanMenu mMenu;
  private MediaListAdapter mAdapter;
  private CurrentExerciseRepository mExercisesRepository;

  public static final String TAG = ExerciseExpandableAdapter.class.getSimpleName();

  public ExerciseExpandableAdapter(Context context, List<ParentObject> parentItemList, ExercisePlanMenu baseListFragment) {
    super(context, parentItemList);
    mContext = context;
    mInflater = LayoutInflater.from(context);
    mMenu = baseListFragment;
    mAdapter = new MediaListAdapter(mMenu);
    mExercisesRepository = mMenu.getBaseActivity().getRelaxUcApplication().getCurrentExerciseRepository();
  }

  @Override
  public ExerciseParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
    View view = mInflater.inflate(R.layout.exercise_view_parent, viewGroup, false);
    return new ExerciseParentViewHolder(view);
  }

  @Override
  public ExerciseChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
    View view = mInflater.inflate(R.layout.exercise_expandable_view, viewGroup, false);
    return new ExerciseChildViewHolder(view);
  }

  @Override
  public void onBindParentViewHolder(ExerciseParentViewHolder exerciseParentViewHolder, int i, Object o) {
    final ExercisePlan crime = (ExercisePlan) o;
    exerciseParentViewHolder.mCrimeTitleTextView.setText(crime.getTitle());
    final ExerciseResponse[] response = new ExerciseResponse[1];

    mExercisesRepository.getCurrentExercise(crime.getId())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<ExerciseResponse>() {
          @Override
          public void onCompleted() {
            Log.d(TAG, "onCompleted");
            if (response[0] != null) {
              crime.setCurrentExerciseId(response[0].getCurrent());
            }
          }

          @Override
          public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
          }

          @Override
          public void onNext(ExerciseResponse exercises) {
            Log.d(TAG, "onNext");
            response[0] = exercises;
          }
        });

  }

  @Override
  public void onBindChildViewHolder(final ExerciseChildViewHolder exerciseChildViewHolder, final int position, final Object o) {


    final ExerciseChild exerciseChild = (ExerciseChild) o;
    final ExercisePlan e = exerciseChild.getPlan();
    exerciseChildViewHolder.getText().setText(((ExerciseChild) o).getSoundData().getName());
    exerciseChildViewHolder.getImageButton().setClickable(e.getCurrentExerciseId() >= exerciseChild.getOrder());
    final ExerciseSoundData exerciseSound = ((ExerciseChild) o).getSoundData();

    if (e.getCurrentExerciseId() < exerciseChild.getOrder()) {
      exerciseChildViewHolder.getCardView().setCardBackgroundColor(Color.GRAY);
    } else {
      exerciseChildViewHolder.getCardView().setCardBackgroundColor(Color.WHITE);
      exerciseChildViewHolder.getImageButton().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          getMenu().loadActivity(exerciseSound, e.getId(), exerciseChild.getOrder());
        }
      });
    }
    final Button downloadButton = exerciseChildViewHolder.getDownloadButton();
    if (isDownloaded(mContext, exerciseSound)) {
      exerciseChildViewHolder.getImageButton().setVisibility(View.VISIBLE);
      downloadButton.setVisibility(View.GONE);
    } else {
      exerciseChildViewHolder.getImageButton().setVisibility(View.GONE);
      final boolean[] clicked = { false };
      downloadButton.setVisibility(View.VISIBLE);
      downloadButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
          if (clicked[0]) {
            return;
          }
          FilesRequest filesRequest = exerciseSound.getFilesRequest();
          filesRequest.addFilesListener(new FilesListener() {
            @Override
            public void onFilesReady(ArrayList<File> files) {
              MediaListFragment fragment = mAdapter.getFragment();
              fragment.showSnackbarMessage(exerciseSound.getName() + " " + getMenu().getString(R.string.downloaded).toLowerCase());
              v.setVisibility(View.GONE);
              exerciseChildViewHolder.getImageButton().setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressUpdate(long percentage) {
              downloadButton.setText(percentage + "%");
            }
          });
          getMenu().getDownloadService().requestFiles(mContext, filesRequest);
          clicked[0] = true;
        }
      });
    }
  }


  public boolean isDownloaded(Context context, ExerciseSoundData ex) {
    return DownloadService.containsFiles(context, ex.getFilesRequest());
  }

  public ExercisePlanMenu getMenu() {
    return mMenu;
  }

}

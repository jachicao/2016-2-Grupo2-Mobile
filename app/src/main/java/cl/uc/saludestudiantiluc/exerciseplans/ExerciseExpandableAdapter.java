package cl.uc.saludestudiantiluc.exerciseplans;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.MediaListAdapter;
import cl.uc.saludestudiantiluc.common.MediaListFragment;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSoundData;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.download.FilesListener;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by camilo on 06-11-16.
 */

public class ExerciseExpandableAdapter extends ExpandableRecyclerAdapter<ExerciseParentViewHolder, ExerciseChildViewHolder> {
  private LayoutInflater mInflater;
  private Context mContext;
  private ExercisePlanMenu mMenu;
  private MediaListAdapter mAdapter;

  public ExerciseExpandableAdapter(Context context, List<ParentObject> parentItemList, ExercisePlanMenu baseListFragment) {
    super(context, parentItemList);
    mContext = context;
    mInflater = LayoutInflater.from(context);
    mMenu = baseListFragment;
    mAdapter = new MediaListAdapter(mMenu);
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
    ExercisePlan crime = (ExercisePlan) o;
    exerciseParentViewHolder.mCrimeTitleTextView.setText(crime.getTitle());
  }

  @Override
  public void onBindChildViewHolder(ExerciseChildViewHolder exerciseChildViewHolder, int position, Object o) {
    ExerciseChild exerciseChild = (ExerciseChild) o;
    exerciseChildViewHolder.getText().setText(((ExerciseChild) o).getSoundData().getName());
    exerciseChildViewHolder.getImageButton().setClickable(exerciseChild.isUnlocked());

    //final ExerciseSound exerciseSound =  getMenu().getDetailedList().get(position).getExercises().get(position);

    final ExerciseSoundData exerciseSound = ((ExerciseChild) o).getSoundData();

    if (!exerciseChild.isUnlocked()) {
      exerciseChildViewHolder.getCardView().setCardBackgroundColor(Color.GRAY);
    } else {
      exerciseChildViewHolder.getCardView().setCardBackgroundColor(Color.WHITE);
      exerciseChildViewHolder.getImageButton().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          getMenu().loadActivity(exerciseSound);
        }
      });
    }
    final ImageButton downloadButton = exerciseChildViewHolder.getDownloadButton();
    if (isDownloaded(mContext, exerciseSound)) {
      downloadButton.setVisibility(View.GONE);
    } else {
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
              //fragment.showSnackbarMessage(exerciseSound.getAudioName() + " " + getMenu().getString(R.string.downloaded).toLowerCase());
              v.setVisibility(View.GONE);
            }

            @Override
            public void onProgressUpdate(long percentage) {
              //downloadButton.setText(getFragment().getString(R.string.downloading) + " " + percentage + "%");
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

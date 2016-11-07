package cl.uc.saludestudiantiluc.exerciseplans;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by camilo on 06-11-16.
 */

public class ExerciseExpandableAdapter extends ExpandableRecyclerAdapter<ExerciseParentViewHolder, ExerciseChildViewHolder> {
  private LayoutInflater mInflater;

  public ExerciseExpandableAdapter(Context context, List<ParentObject> parentItemList) {
    super(context, parentItemList);
    mInflater = LayoutInflater.from(context);
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
  public void onBindChildViewHolder(ExerciseChildViewHolder exerciseChildViewHolder, int i, Object o) {
    ExerciseChild exerciseChild = (ExerciseChild) o;
    exerciseChildViewHolder.getText().setText(exerciseChild.getTitle().toString());
    exerciseChildViewHolder.getImageButton().setClickable(exerciseChild.isSolved());
    if (!exerciseChild.isSolved()) {
      exerciseChildViewHolder.getCardView().setCardBackgroundColor(Color.GRAY);
    }
  }
}

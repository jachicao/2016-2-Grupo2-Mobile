package cl.uc.saludestudiantiluc.exerciseplans;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;

/**
 * Created by camilo on 06-11-16.
 */

public class ExerciseParentViewHolder extends ParentViewHolder {
  public TextView mCrimeTitleTextView;
  public ImageButton mParentDropDownArrow;

  public ExerciseParentViewHolder(View itemView) {
    super(itemView);

    mCrimeTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_crime_title_text_view);
    mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_list_item_expand_arrow);
  }

  public void bind(ExercisePlan exercisePlan) {
    mCrimeTitleTextView.setText(exercisePlan.getTitle());
  }
}

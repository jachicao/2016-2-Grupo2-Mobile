package cl.uc.saludestudiantiluc.exerciseplans;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by camilo on 06-11-16.
 */

public class ExerciseChildViewHolder extends ChildViewHolder{
  private TextView mText;
  private ImageButton mImageButton;
  private Button mDownloadButton;
  private CardView mCardView;

  public ExerciseChildViewHolder(View itemView) {
    super(itemView);
    mText = (TextView) itemView.findViewById(R.id.child_list_item_crime_date_text_view);
    mImageButton = (ImageButton) itemView.findViewById(R.id.playExerciseButton);
    mCardView = (CardView) itemView.findViewById(R.id.exercise_cardview);
    mDownloadButton = (Button) itemView.findViewById(R.id.downloadExerciseButton);
  }

  public TextView getText() {
    return mText;
  }

  public ImageButton getImageButton() {
    return mImageButton;
  }

  public CardView getCardView() {
    return mCardView;
  }

  public Button getDownloadButton() {
    return mDownloadButton;
  }
}

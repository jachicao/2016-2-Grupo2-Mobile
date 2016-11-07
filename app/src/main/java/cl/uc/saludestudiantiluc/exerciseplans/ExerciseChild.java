package cl.uc.saludestudiantiluc.exerciseplans;

import java.util.Date;

/**
 * Created by camilo on 06-11-16.
 */

public class ExerciseChild {
  private String mTitle;
  private boolean mSolved;

  public ExerciseChild(String title, boolean solved) {
    mTitle = title;
    mSolved = solved;
  }

  public String getTitle() {
    return mTitle;
  }

  public boolean isSolved() {
    return mSolved;
  }
}

package cl.uc.saludestudiantiluc.squarebreathing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseFragment;

public class SquareBreathingActivity extends BaseFragment {

  private static final String TAG = SquareBreathingActivity.class.getSimpleName();

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.exercise_square_breathing, container, false);
    /*
    final BaseActivity activity = (BaseActivity)getActivity();
    if (activity != null) {
      ActionBar actionBar = activity.getSupportActionBar();
      if (actionBar != null) {
        actionBar.setTitle(R.string.square_breathing);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
      }
      activity.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          activity.getSupportFragmentManager().beginTransaction().remove(SquareBreathingActivity.this).commit();
        }
      });
    }
    */
    return rootView;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
  /*
  public static Intent getIntent(Activity activity) {
    return new Intent(activity, SquareBreathingActivity.class);
  }
  */
}

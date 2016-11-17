package cl.uc.saludestudiantiluc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.uc.saludestudiantiluc.common.BaseFragment;
import cl.uc.saludestudiantiluc.breathingexcercises.squarebreathing.SquareBreathingActivity;

/**
 * Created by lukas on 9/27/16.
 */

public class HomeFragment extends BaseFragment {

  public static final String TAG = HomeFragment.class.getSimpleName();
  public HomeFragment() {
    // Required empty public constructor
  }

  public static HomeFragment newInstance() {
    return new HomeFragment();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    View fab = view.findViewById(R.id.home_start_now);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(SquareBreathingActivity.getIntent(getActivity()));
      }
    });
    //fab.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
    return view;
  }
}

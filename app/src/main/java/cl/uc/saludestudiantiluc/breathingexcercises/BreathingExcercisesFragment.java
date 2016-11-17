package cl.uc.saludestudiantiluc.breathingexcercises;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.breathingexcercises.guidedbreathing.GuidedBreathingActivity;
import cl.uc.saludestudiantiluc.breathingexcercises.squarebreathing.SquareBreathingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BreathingExcercisesFragment extends Fragment {


  public static Fragment newInstance() {
    return new BreathingExcercisesFragment();
  }

  public BreathingExcercisesFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_breathing_excercises, container, false);

    view.findViewById(R.id.square_breathing_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(SquareBreathingActivity.getIntent(getActivity()));
      }
    });

    view.findViewById(R.id.guided_breathing_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(GuidedBreathingActivity.getIntent(getActivity()));
      }
    });

    return view;
  }

}

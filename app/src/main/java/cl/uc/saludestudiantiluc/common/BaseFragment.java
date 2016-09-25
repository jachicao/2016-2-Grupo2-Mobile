package cl.uc.saludestudiantiluc.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by jchicao on 9/25/16.
 */

public class  BaseFragment extends Fragment {
  public FragmentListener mListener;
  public void dismiss() {
    FragmentActivity activity = getActivity();
    if (activity != null) {
      activity.getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
      if (mListener != null) {
        mListener.onDismissed();
      }
    }
  }
  public void onBackPressed() {
    dismiss();
  }
}

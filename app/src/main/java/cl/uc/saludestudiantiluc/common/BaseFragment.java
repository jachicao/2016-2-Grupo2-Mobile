package cl.uc.saludestudiantiluc.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by jchicao on 9/25/16.
 */

public class  BaseFragment extends Fragment {
  public void dismiss() {
    FragmentActivity activity = getActivity();
    if (activity != null) {
      getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
  }
  public void onBackPressed(){
    dismiss();
  }
}

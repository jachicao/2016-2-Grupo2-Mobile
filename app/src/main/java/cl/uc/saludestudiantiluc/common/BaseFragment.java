package cl.uc.saludestudiantiluc.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by jchicao on 9/25/16.
 */

public class  BaseFragment extends Fragment {

  public interface FragmentListener {
    void onDismissed();
  }

  private FragmentListener mListener;

  public void setListener(FragmentListener listener) {
    mListener = listener;
  }
}

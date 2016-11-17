package cl.uc.saludestudiantiluc.common;

import android.support.v4.app.Fragment;

import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.post.PostService;

/**
 * Created by jchicao on 9/25/16.
 */

public class BaseFragment extends Fragment {

  public BaseActivity getBaseActivity() {
    return (BaseActivity) getActivity();
  }

  public DownloadService getDownloadService() {
    return getBaseActivity().getDownloadService();
  }

  public void showSnackbarMessage(String message) {
    getBaseActivity().showSnackbarMessage(message);
  }
}

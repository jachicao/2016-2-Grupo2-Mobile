package cl.uc.saludestudiantiluc.common;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.services.download.DownloadService;

/**
 * Created by jchicao on 9/25/16.
 */

public class BaseFragment extends Fragment {

  private DownloadService mDownloadService = null;
  public DownloadService getDownloadService() {
    if (mDownloadService == null) {
      mDownloadService = new DownloadService(getContext());
    }
    return mDownloadService;
  }

  public BaseActivity getBaseActivity() {
    return (BaseActivity) getActivity();
  }

  @Override
  public void onDestroy() {
    if (mDownloadService != null) {
      mDownloadService.onDestroy();
    }
    super.onDestroy();
  }

  public void notifyMessage(String message) {
    getBaseActivity().notifyMessage(message);
  }
}

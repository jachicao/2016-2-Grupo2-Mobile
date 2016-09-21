package cl.uc.saludestudiantiluc.common;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.WindowManager;

/**
 * Created by lukas on 9/21/16.
 */

public class TranslucentActivity extends BaseActivity {

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    setTranslucent();
  }

  private void setTranslucent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
  }
}

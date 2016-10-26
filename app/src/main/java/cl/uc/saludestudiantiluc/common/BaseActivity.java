package cl.uc.saludestudiantiluc.common;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.RelaxUcApplication;
import cl.uc.saludestudiantiluc.auth.data.UserRepository;
import cl.uc.saludestudiantiluc.services.download.DownloadService;

/**
 * Created by lukas on 9/21/16.
 */

public class BaseActivity extends AppCompatActivity {

  private static final String TAG = BaseActivity.class.getSimpleName();
  private DownloadService mDownloadService = null;

  private RelaxUcApplication mRelaxUcApplication;
  private Toolbar mToolbar;
  private boolean mImmersiveMode = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mRelaxUcApplication = (RelaxUcApplication) getApplication();
  }

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setStatusBarColor(getResources().getColor(R.color.black));
    }
    setSupportActionBar(mToolbar);
  }

  protected void loadMainBackground() {
    Glide
        .with(this)
        .load(R.drawable.main_background)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .centerCrop()
        .into((ImageView) findViewById(R.id.main_background_image));
  }

  public Toolbar getToolbar() {
    return mToolbar;
  }

  public RelaxUcApplication getRelaxUcApplication() {
    return mRelaxUcApplication;
  }

  public UserRepository getUserRepository() {
    return mRelaxUcApplication.getUserRepository();
  }

  public DownloadService getDownloadService() {
    if (mDownloadService == null) {
      mDownloadService = new DownloadService(this);
    }
    return mDownloadService;
  }

  @Override
  protected void onDestroy() {
    if (mDownloadService != null) {
      mDownloadService.onDestroy();
    }
    super.onDestroy();
  }

  public void notifyMessage(String message) {
    Snackbar.make(findViewById(R.id.main_coordinator_layout),
        message, Snackbar.LENGTH_SHORT).show();
  }

  public boolean isImmersiveMode() {
    return mImmersiveMode;
  }

  public void enableImmersiveMode() {
    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN;
    if (Build.VERSION.SDK_INT >= 19) {
      uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }
    getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    mImmersiveMode = true;
  }

  public void disableImmersiveMode() {
    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    mImmersiveMode = false;
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus && mImmersiveMode) {
      enableImmersiveMode();
    }
  }

  public void onSingleTap() {
    Log.v(TAG, "onSingleTap");
    if (mImmersiveMode) {
      disableImmersiveMode();
    } else {
      enableImmersiveMode();
    }
  }
}

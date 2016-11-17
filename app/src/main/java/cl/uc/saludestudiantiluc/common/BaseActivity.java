package cl.uc.saludestudiantiluc.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.RelaxUcApplication;
import cl.uc.saludestudiantiluc.auth.AuthActivity;
import cl.uc.saludestudiantiluc.auth.data.UserRepository;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.post.PostService;

/**
 * Created by lukas on 9/21/16.
 */

public class BaseActivity extends AppCompatActivity {

  private static final String TAG = BaseActivity.class.getSimpleName();
  private static final String IMMERSIVE_MODE = "ImmersiveMode";

  private DownloadService mDownloadService = null;
  private RelaxUcApplication mRelaxUcApplication;
  private Toolbar mToolbar;
  private PostService mPostService = null;
  protected boolean mPreviousImmersiveModeFound = false;
  private boolean mImmersiveMode = false;
  private BroadcastReceiver mBroadcastReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mRelaxUcApplication = (RelaxUcApplication) getApplication();
    mPreviousImmersiveModeFound = savedInstanceState != null && savedInstanceState.containsKey(IMMERSIVE_MODE);
    if (mPreviousImmersiveModeFound) {
      boolean immersive = savedInstanceState.getBoolean(IMMERSIVE_MODE, false);
      if (immersive) {
        enableImmersiveMode();
      } else {
        disableImmersiveMode();
      }
    }
    mBroadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        logOut();
        Toast.makeText(getApplicationContext(), getString(R.string.error_401), Toast.LENGTH_LONG).show();
      }
    };
    LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver, new IntentFilter(RelaxUcApplication.INTERCEPTOR_LOG_OUT));
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

  public PostService getPostService() {
    if (mPostService == null) {
      mPostService = new PostService();
    }
    return mPostService;
  }

  @Override
  protected void onDestroy() {
    if (mDownloadService != null) {
      mDownloadService.onDestroy();
    }

    if (mBroadcastReceiver != null) {
      LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }
    super.onDestroy();
  }

  public void showSnackbarMessage(String message) {
    Snackbar.make(findViewById(R.id.coordinator_layout),
        message, Snackbar.LENGTH_SHORT).show();
  }

  public void showToastMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  public boolean isImmersiveMode() {
    return (getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0 || mImmersiveMode;
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
    if (hasFocus && isImmersiveMode()) {
      enableImmersiveMode();
    }
  }

  public void onSingleTap() {
    Log.v(TAG, "onSingleTap");
    if (isImmersiveMode()) {
      disableImmersiveMode();
    } else {
      enableImmersiveMode();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putBoolean(IMMERSIVE_MODE, isImmersiveMode());
    super.onSaveInstanceState(outState);
  }

  public void logOut() {
    getUserRepository().logOut();
    startActivity(AuthActivity.getIntent(this));
    finish();
  }
}

package cl.uc.saludestudiantiluc.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import cl.uc.saludestudiantiluc.services.sound.SoundService;

/**
 * Created by jchicao on 10/22/16.
 */

public class SoundServiceActivity extends BaseActivity implements ServiceConnection {

  private static final String TAG = SoundServiceActivity.class.getSimpleName();

  protected SoundService mSoundService;
  private boolean mSoundServiceBound = false;

  private boolean mPendingSound = false;
  private String mPendingSoundDisplayName = "";
  private String mPendingSoundPath = "";
  private boolean mPendingSoundPlay = false;
  private int mPendingSoundPosition = 0;

  @Override
  protected void onStart() {
    super.onStart();
    Intent soundServiceIntent = new Intent(this, SoundService.class);
    bindService(soundServiceIntent, this, Context.BIND_AUTO_CREATE);
    startService(soundServiceIntent);
  }

  private void unbindService() {
    if (mSoundServiceBound) {
      unbindService(this);
      mSoundServiceBound = false;
    }
  }

  @Override
  public void onSingleTap() {
    super.onSingleTap();
    if (mSoundService != null) {
      int state = mSoundService.getMediaPlayerState();
      switch (state) {
        case SoundService.MEDIA_PLAYER_STATE_PLAY:
          mSoundService.pauseSound();
          break;
        case SoundService.MEDIA_PLAYER_STATE_PAUSE:
          mSoundService.startSound();
          break;
        default:
          break;
      }
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mSoundService != null) {
      mSoundService.onResume();
    }
  }

  @Override
  protected void onStop() {
    if (mSoundService != null) {
      mSoundService.onStop();
    }
    unbindService();
    super.onStop();
  }

  @Override
  public void onServiceConnected(ComponentName name, IBinder service) {
    Log.v(TAG, "onServiceConnected");
    SoundService.LocalBinder binder = (SoundService.LocalBinder) service;
    mSoundService = binder.getService();
    mSoundServiceBound = true;
    if (mSoundService != null) {
      mSoundService.onServiceConnected();
    }
    if (mPendingSound) {
      setMediaPlayerSound(mPendingSoundPath, mPendingSoundDisplayName, mPendingSoundPlay, mPendingSoundPosition);
    }
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    Log.v(TAG, "onServiceDisconnected");
    mSoundServiceBound = false;
  }


  public void setMediaPlayerSound(String soundPath, String displayName, boolean play, int position) {
    if (mSoundService != null) {
      mSoundService.newSound(soundPath, displayName, play, position);
      mPendingSound = false;
    } else {
      Log.v(TAG, "Pending");
      mPendingSound = true;
      mPendingSoundPath = soundPath;
      mPendingSoundDisplayName = displayName;
      mPendingSoundPlay = play;
      mPendingSoundPosition = position;
    }
  }
}

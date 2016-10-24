package cl.uc.saludestudiantiluc.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import cl.uc.saludestudiantiluc.services.sound.SoundService;

/**
 * Created by jchicao on 10/22/16.
 */

public class SoundServiceActivity extends BaseActivity implements ServiceConnection {

  protected SoundService mSoundService;
  private boolean mSoundServiceBound = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startSoundService();
  }

  private void startSoundService() {
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
      MediaPlayer mediaPlayer = mSoundService.getMediaPlayer();
      if(mediaPlayer != null) {
        if (mediaPlayer.isPlaying()) {
          mSoundService.pauseSound();
        } else {
          mSoundService.startSound();
        }
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
  protected void onPause() {
    unbindService();
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    unbindService();
    super.onDestroy();
  }

  @Override
  public void onServiceConnected(ComponentName name, IBinder service) {
    SoundService.LocalBinder binder = (SoundService.LocalBinder) service;
    mSoundService = binder.getService();
    mSoundServiceBound = true;
    if (mSoundService != null) {
      mSoundService.onResume();
    }
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    mSoundServiceBound = false;
  }
}

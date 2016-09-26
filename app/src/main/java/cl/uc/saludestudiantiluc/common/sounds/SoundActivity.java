package cl.uc.saludestudiantiluc.common.sounds;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import cl.uc.saludestudiantiluc.R;

public class SoundActivity extends AppCompatActivity {

  private static final String MEDIA_STATE = "is_playing";

  private SoundService mService;
  private Sound mSound;
  private boolean mBound = false;
  private ImageButton mMediaPlayerButton;
  private String mServiceState;
  private VideoView mVideoView;
  public static final String START_STATE = "playing";
  public static final String STOP_STATE = "stopped";
  public static final String PAUSE_STATE = "paused";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sound_activity);
    final Intent soundServiceIntent = new Intent(this, SoundService.class);
    bindService(soundServiceIntent, mConnection, this.BIND_AUTO_CREATE);
    mMediaPlayerButton = (ImageButton) findViewById(R.id.sound_activity_stop);
    mMediaPlayerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        handleService(soundServiceIntent);
      }
    });

    if (savedInstanceState != null) {
      mServiceState = savedInstanceState.getString(MEDIA_STATE);
      setMediaButtonIcon(mServiceState);
      mSound = savedInstanceState.getParcelable("Sound");
    } else {
      mSound = getIntent().getParcelableExtra("Sound");
    }
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putString(MEDIA_STATE, mServiceState);
    savedInstanceState.putParcelable("Sound", mSound);
    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();
    setVideo();
    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
        | View.SYSTEM_UI_FLAG_FULLSCREEN;
    if (Build.VERSION.SDK_INT >= 19) {
      uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }
    getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    if (mService != null) {
      mService.undoNotify();
    }
  }

  public void setVideo() {
    mVideoView = (VideoView) findViewById(R.id.sound_activity_surface_view);
    //de forma alternativa si queremos un streaming usar
    //mVideoView.setVideoURI(Uri.parse(URLstring));
    Uri video = Uri.parse("android.resource://cl.uc.saludestudiantiluc/raw/v1");
    mVideoView.setVideoURI(video);
    //mVideoView.setVideoPath("/mnt/sdcard/video.mp4");
    mVideoView.start();
    mVideoView.requestFocus();
    this.setVolumeControlStream(0);
  }

  @Override
  protected void onStop() {
    super.onStop();
    // Unbind from the service
    if (mBound) {
      mService.notifySound();
      unbindService(mConnection);
      mBound = false;
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    // Unbind from the service
    if (mBound) {
      mService.notifySound();
      unbindService(mConnection);
      mBound = false;
    } else if (mService != null) {
      mService.notifySound();
    }
  }

  private void setMediaButtonIcon(String playing) {
    if (playing.equals(START_STATE)) {
      mMediaPlayerButton.setImageResource(android.R.drawable.ic_media_pause);
    } else {
      mMediaPlayerButton.setImageResource(android.R.drawable.ic_media_play);
    }
    restoreInfo(playing);
  }

  private ServiceConnection mConnection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName className,
                                   IBinder service) {
      // We've bound to LocalService, cast the IBinder and get LocalService instance
      SoundService.LocalBinder binder = (SoundService.LocalBinder) service;
      mService = binder.getService();
      mServiceState = mService.getState();
      mService.undoNotify();
      mService.setSound(mSound);
      if (mServiceState.equals(START_STATE) || mServiceState.equals(PAUSE_STATE)) {
        if (!mSound.getType().equals(mService.getCurrentSound())) {
          mService.onStop();
          mServiceState = STOP_STATE;
        }
      }
      setMediaButtonIcon(mServiceState);
      mBound = true;
      Intent intent = getIntent();
      int message = intent.getIntExtra(SoundService.MENSAJE_EXTRA, 0);

      if (message == 1) {
        mService.onStop();
        mServiceState = STOP_STATE;
        setMediaButtonIcon(mServiceState);
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
      mBound = false;
    }
  };

  public void handleService(Intent intent) {
    if (mServiceState.equals(START_STATE)) {
      if (!mSound.getType().equals("Imagery")) {
        mService.onStop();
        mServiceState = STOP_STATE;
      } else {
        mService.onPause();
        mServiceState = PAUSE_STATE;
      }
    } else {
      startService(intent);
      startSoundService();
      mServiceState = START_STATE;
    }
    setMediaButtonIcon(mServiceState);
  }

  public void startSoundService() {
    mService.onSound(mSound.getType());
  }

  public MediaPlayer getMediaPlayer() {
    if (mService != null) {
      return mService.getMediaPlayer();
    }
    return null;
  }

  public SoundService getService() {
    return mService;
  }

  public Sound getSound() {
    return mSound;
  }

  public boolean getIsReleased() {
    return mService.getIsReleased();
  }

  public void restoreInfo(String status) {

  }

  public VideoView getVideoView() {
    return mVideoView;
  }

}

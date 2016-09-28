package cl.uc.saludestudiantiluc.imageries;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.sounds.SoundActivity;
import cl.uc.saludestudiantiluc.common.sounds.Sound;
import cl.uc.saludestudiantiluc.common.sounds.SoundService;

public class ImageryDisplayActivity extends SoundActivity {

  private MediaPlayer mPlayer;
  private SeekBar mSeekBar;
  private final Handler mHandler = new Handler();
  private RelativeLayout relativeLayout;
  private Runnable notification;
  private int delayMilis = 500;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSeekBar = new SeekBar(this);
    setUriString("android.resource://cl.uc.saludestudiantiluc/raw/lake");
    relativeLayout = (RelativeLayout) findViewById(R.id.sound_activity_relative_layout);
    RelativeLayout.LayoutParams rLSeekBarParams =
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    rLSeekBarParams.addRule(RelativeLayout.ABOVE, R.id.sound_activity_stop);
    relativeLayout.addView(mSeekBar, rLSeekBarParams);
    Sound sound = getSound();
    if (sound.name.equals("Imagery")) {
      MediaPlayer mAuxPlayer = MediaPlayer.create(this, R.raw.imagineria);
      mSeekBar.setMax(mAuxPlayer.getDuration());
      mAuxPlayer.release();
    }

    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mPlayer = getMediaPlayer();
        if (mPlayer != null && fromUser && !getIsReleased()) {
          mPlayer.seekTo(progress);
        }
      }
    });
  }

  @Override
  protected void onPause(){
    super.onPause();
    mHandler.removeCallbacks(notification);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mHandler.removeCallbacks(notification);
    Log.d("Handler", "Handler Destroyed");
  }

  @Override
  protected void onResume() {
    super.onResume();
  }


  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Saliendo de imaginería")
        .setCancelable(false)
        .setMessage("¿Estás seguro que deseas salir?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            finish();
          }

        })
        .setNegativeButton("No", null)
        .show();
  }

  @Override
  public void startSoundService() {
    SoundService mService = getService();
    mService.onModifiedSound(getSound().getType(), mSeekBar.getProgress());
  }

  @Override
  public void restoreInfo(String released) {
    mPlayer = getMediaPlayer();
    if (mPlayer != null && !released.equals(STOP_STATE)) {
      int mCurrentPosition = mPlayer.getCurrentPosition();
      mSeekBar.setProgress(mCurrentPosition);
      if (mPlayer.isPlaying()) {
        notification = new Runnable() {
          public void run() {
            startPlayProgressUpdate();
            mHandler.postDelayed(notification, delayMilis);
          }
        };
        mHandler.post(notification);
      }
    } else {
      if (mHandler != null) {
        Log.d("Handler", "Handler Destroyed");
        mHandler.removeCallbacks(notification);
      }
    }
  }

  public void startPlayProgressUpdate() {
    int currentPosition;
    if (mPlayer != null) {
      currentPosition = mPlayer.getCurrentPosition();
    } else {
      currentPosition = 0;
    }
    mSeekBar.setProgress(currentPosition);
  }

}
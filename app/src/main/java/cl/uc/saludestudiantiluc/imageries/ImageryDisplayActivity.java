package cl.uc.saludestudiantiluc.imageries;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.sounds.AmbientalSoundActivity;
import cl.uc.saludestudiantiluc.common.sounds.Sound;
import cl.uc.saludestudiantiluc.common.sounds.SoundService;

public class ImageryDisplayActivity extends AmbientalSoundActivity {

  public static final String SOUND = "Sound";

  private MediaPlayer mPlayer;
  private SeekBar mSeekBar;
  private final Handler mHandler = new Handler();
  private ImageButton mediaPlayerButton;
  private ImageView mImageView;
  private ImageView mMediaPlayerButton;
  private Animation mFadeIn;
  private Animation mFadeOut;
  private FrameLayout frameLayout;
  private Runnable notification;
  boolean isPlay = true;
  private int count = 0;
  private int delayMilis = 500;
  private int firstImageConstant = 0;
  private int secondImageConstant = 1;
  private int thirdImageConstant = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_imagery_display);

    //mPlayer = MediaPlayer.create(this, R.raw.imagineria);
    mSeekBar = new SeekBar(this);
    //mImageView = (ImageView)findViewById(R.id.imageView);

    mImageView = new ImageView(this);
    mImageView.setBackgroundResource(R.drawable.forest);
    mFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
    mFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
    mFadeIn.setRepeatCount(Animation.INFINITE);
    mImageView.startAnimation(mFadeIn);

    frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

    //getVideoView()

    frameLayout.addView(mSeekBar);
    mImageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT));

    mSeekBar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.WRAP_CONTENT));

    mMediaPlayerButton = getImageButton();

    Sound sound = getSound();
    if (sound.getName().equals("Imagery")){
      MediaPlayer mAuxPlayer = MediaPlayer.create(this, R.raw.imagineria);
      mSeekBar.setMax(mAuxPlayer.getDuration());
      mAuxPlayer.release();
    }
    //Sound mSelectedSound = this.getIntent().getParcelableExtra(SOUND);
    /*mediaPlayerButton = (ImageButton)findViewById(R.id.mediaPlayerButton);
    mediaPlayerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isPlay){
          mediaPlayerButton.setImageResource(android.R.drawable.ic_media_play);
          mPlayer.pause();
        }
        else{
          mediaPlayerButton.setImageResource(android.R.drawable.ic_media_pause);
          mPlayer.start();
        }
        isPlay = !isPlay;
      }
    });*/



    mFadeIn.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
      }
      @Override
      public void onAnimationEnd(Animation animation) {

        mImageView.startAnimation(mFadeOut);
      }
      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });

    mFadeOut.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
      }
      @Override
      public void onAnimationEnd(Animation animation) {
        if (count == secondImageConstant){
          mImageView.setBackgroundResource(R.drawable.sky);
        }
        else if(count == thirdImageConstant){
          mImageView.setBackgroundResource(R.drawable.forest);
        }
        else{
          mImageView.setBackgroundResource(R.drawable.ocean);
        }
        count ++;
        if (count > thirdImageConstant){
          count = firstImageConstant;
        }

        mImageView.startAnimation(mFadeIn);
      }
      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });


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
        if(mPlayer != null && fromUser && !getIsReleased()){
          mPlayer.seekTo(progress);
        }
      }
    });
  }




  @Override
  protected void onDestroy() {
    super.onDestroy();
    mHandler.removeCallbacks(notification);
    Log.d("Handler", "Handler Destroyed");
    //mPlayer.stop();
    //mPlayer.release();
    //mPlayer = null;
  }

  @Override
  protected void onResume() {
    super.onResume();

  }

  @Override
  public void setVideo(){

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
  public void startSoundService(){
    SoundService mService = getService();
    mService.onModifiedSound(getSound().getType(), mSeekBar.getProgress());
  }

  @Override
  public void restoreInfo(String released){
    mPlayer = getMediaPlayer();
    if(mPlayer != null && !released.equals(STOP_STATE)){
      int mCurrentPosition = mPlayer.getCurrentPosition();
      mSeekBar.setProgress(mCurrentPosition);
      if (mPlayer.isPlaying()) {
        notification = new Runnable() {
          public void run() {
            startPlayProgressUpdate();
            mHandler.postDelayed(notification,delayMilis);
          }
        };
        mHandler.post(notification);
      }
    }
    else{
      if (mHandler != null){
        Log.d("Handler", "Handler Destroyed");
        mHandler.removeCallbacks(notification);
      }
    }
  }

  public void startPlayProgressUpdate(){
    int currentPosition;
    if (mPlayer != null){
      currentPosition = mPlayer.getCurrentPosition();
    }
    else{
      currentPosition = 0;
    }
    mSeekBar.setProgress(currentPosition);
  }

}
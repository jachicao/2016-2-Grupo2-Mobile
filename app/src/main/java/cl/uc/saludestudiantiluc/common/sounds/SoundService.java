package cl.uc.saludestudiantiluc.common.sounds;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;

public class SoundService extends Service implements MediaPlayer.OnPreparedListener {


  private boolean mIsReleased = true;
  private String mState; //paused; played; stopped
  private String nName;
  private String nDuration;
  RemoteViews contentView;
  NotificationCompat.Builder mNotifyBuilder ;
  NotificationManager mNotificationManager;
  public static final String MENSAJE_EXTRA = "com.app.algo";
  public static final String START_STATE = "playing";
  public static final String STOP_STATE = "stopped";
  public static final String PAUSE_STATE = "paused";
  private String mCurrentSound;


  /////////////////Comunication Section
  private final IBinder mBinder = new LocalBinder();
  public class LocalBinder extends Binder {
    SoundService getService() {
      // Return this instance of LocalService so clients can call public methods
      return SoundService.this;
    }
  }
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }
  ////End Comunication Section

  private static final String TAG = null;
  MediaPlayer mMediaPlayer;

  @Override
  public void onCreate() {
    super.onCreate();
    mState = STOP_STATE;
  }

  @Override
  public int onStartCommand (Intent intent, int flags, int startId) {

    if (intent != null) {
      int message = intent.getIntExtra(SoundService.MENSAJE_EXTRA,0);
      if(message == 1) {
        if(mState.equals(START_STATE)) {
          onPause();
          //contentView.setTextViewText(R.id.notifyButton, " a");
          contentView.setImageViewResource(R.id.notifyButton, android.R.drawable.ic_media_play);
          mNotifyBuilder.setContent(contentView);

          mNotificationManager.notify(0, mNotifyBuilder.build());
        } else {
          onResume();
          contentView.setImageViewResource(R.id.notifyButton, android.R.drawable.ic_media_pause);
          mNotifyBuilder.setContent(contentView);

          mNotificationManager.notify(0, mNotifyBuilder.build());
        }

      } else if(message ==2) {
        onDelete();
      }
    }

    return START_STICKY;
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {

    mState = STOP_STATE;
    if (mMediaPlayer != null) {
      mMediaPlayer.stop();
      mMediaPlayer.release();
    }
    if (mNotificationManager != null) {
      mNotificationManager.cancel(0);
    }

    stopSelf();
  }

  public void onSound(String origin) {
    if (mState.equals(STOP_STATE)){
      if (origin.equals("Imagery")){
        mMediaPlayer = MediaPlayer.create(this, R.raw.imagineria);
        mCurrentSound = "Imagery";
      }
      else{
        mMediaPlayer = MediaPlayer.create(this, R.raw.nature);
        mCurrentSound = "Ambiental";
      }
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }
    mMediaPlayer.start();
    mIsReleased = false;
    mState = START_STATE;
    String url = "http://www.soundjay.com/nature/sounds/rain-01.mp3"; // your URL here
    nName = "Rain";
    nDuration = "1:30";
  }

  public void onModifiedSound(String origin, int start){
    if (mState.equals(STOP_STATE)){
      if (origin.equals("Imagery")){
        mMediaPlayer = MediaPlayer.create(this, R.raw.imagineria);
        mCurrentSound = "Imagery";
      }
      else{
        mMediaPlayer = MediaPlayer.create(this, R.raw.nature);
        mCurrentSound = "Ambiental";
      }
      mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }
    mMediaPlayer.seekTo(start);
    mMediaPlayer.start();
    mIsReleased = false;
    mState = START_STATE;
  }

   // mMediaPlayer = new MediaPlayer();
    /*
    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    try {
      mMediaPlayer.setDataSource(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    mMediaPlayer.setOnPreparedListener(this);
    mMediaPlayer.prepareAsync(); // prepare async to not block main thread
*/
   // mMediaPlayer.start();
    //mMediaPlayer.setLooping(true); // Set looping
   // mMediaPlayer.setVolume(100,100);

  public void notifySound() {
    if(mState.equals(START_STATE)){
      int icon = R.drawable.notification_template_icon_bg;
      long when = System.currentTimeMillis();
      //notification = new Notification.builder(icon, "Custom Notification", when);
      //////////// setup contentVie
      contentView = new RemoteViews(getPackageName(), R.layout.sound_notification);
      contentView.setImageViewResource(R.id.image, R.drawable.notification_template_icon_bg);
      contentView.setTextViewText(R.id.title, nName);
      contentView.setTextViewText(R.id.text, "Duración: " + nDuration);
      contentView.setImageViewResource(R.id.notifyButton, android.R.drawable.ic_media_pause);
      contentView.setImageViewResource(R.id.close_notify, android.R.drawable.ic_delete);
      ///////////// end contentView
      //////setup intentn
      Intent notificationIntent = new Intent(this, MainActivity.class);
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
      ////// end intent
      mNotifyBuilder = new NotificationCompat.Builder(this)
         // .setContentTitle(nName)
         // .setContentText("You've received new messages.")
          .setContent(contentView)
          .setOngoing(true)
          .setSmallIcon(R.drawable.notification_template_icon_bg);


      mNotificationManager = (NotificationManager) getSystemService
          (NOTIFICATION_SERVICE);
      Intent pauseIntent = new Intent(this, SoundService.class);
      pauseIntent.putExtra(MENSAJE_EXTRA, 1); // 1 : Pause
      PendingIntent pendingPause = PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      contentView.setOnClickPendingIntent(R.id.notifyButton, pendingPause);
      Intent closeIntent = new Intent(this, SoundService.class);
      closeIntent.putExtra(MENSAJE_EXTRA, 2); // 2 : Close
      PendingIntent pendingClose = PendingIntent.getService(this, 2, closeIntent, PendingIntent
          .FLAG_UPDATE_CURRENT);
      contentView.setOnClickPendingIntent(R.id.close_notify, pendingClose);
      /*
      mNotifyBuilder.flags |= mNotifyBuilder.FLAG_NO_CLEAR; //Do not clear the notification
      mNotifyBuilder.defaults |= mNotifyBuilder.DEFAULT_SOUND; // Sound
      mNotifyBuilder.defaults |= mNotifyBuilder.DEFAULT_LIGHTS; // LED
      mNotifyBuilder.defaults |= mNotifyBuilder.DEFAULT_VIBRATE; //Vibration
*/
      //  mNotificationManager.notify(1, notification);

      mNotificationManager.notify(0, mNotifyBuilder.build());
    }
  }

  public void onPrepared(MediaPlayer player) {
    player.start();
    player.setLooping(true);
  }

  public void onStop() {
    mState = STOP_STATE;
    mMediaPlayer.stop();
    mMediaPlayer.release();
    mIsReleased = true;
  }

  public void onPause() {
    mState = PAUSE_STATE;
    mMediaPlayer.pause();
  }

  public void onResume() {
    mState = START_STATE;
    mMediaPlayer.start();
  }

  public void undoNotify() {
    if(mState.equals(START_STATE)) {
      mNotificationManager.cancel(0);
    }
  }

  public void onDelete() {
    mState = STOP_STATE;
    if (mMediaPlayer != null) {
      mMediaPlayer.stop();
      mMediaPlayer.release();
    }
    if (mNotificationManager != null) {
      mNotificationManager.cancel(0);
    }
    stopSelf();
  }

  public String getState(){
    return mState;
  }

  public String getCurrentSound(){
    return mCurrentSound;
  }

  public int getSoundLength(){
    return mMediaPlayer.getDuration();
  }

  public MediaPlayer getMediaPlayer(){
    return mMediaPlayer;
  }

  public boolean getIsReleased(){
    return mIsReleased;
  }

}



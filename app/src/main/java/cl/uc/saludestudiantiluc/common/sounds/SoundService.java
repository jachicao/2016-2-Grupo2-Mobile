package cl.uc.saludestudiantiluc.common.sounds;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;


public class SoundService extends Service implements MediaPlayer.OnPreparedListener {
  private BroadcastReceiver mReceiver;
  private boolean mIsReleased = true;
  private String mState = ""; //paused; played; stopped
  private String nName;
  private String nDuration;
  RemoteViews contentView;
  NotificationCompat.Builder mNotifyBuilder ;
  NotificationManager mNotificationManager;
  public static final String EXTRA_MESSAGE = "com.app.algo";
  public static final String START_STATE = "playing";
  public static final String STOP_STATE = "stopped";
  public static final String PAUSE_STATE = "paused";
  public static final String SWIPE_STATE = "swipped";
  public static final String CLOSE_STATE = "closed";
  public static final String NOTIFICATION_MESSAGE = "SoundNotificationMessage";
  public static final int MESSAGE_START_STOP = 1;
  public static final int MESSAGE_CLOSE = 2;
  public static final int MESSAGE_SWIPE = 3;
  private String mCurrentSound;
  private Sound mPlayingSound;

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
    mReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (context != null && intent != null) {
          String action = intent.getAction();
          if (action.equals(SWIPE_STATE)) {
            Log.v("SoundService", "swipe");
            unregisterReceiver(this);
            onDelete();
          } else if (action.equals(CLOSE_STATE)) {
            Log.v("SoundService", "close");
            unregisterReceiver(this);
            onDelete();
          } else if (action.equals(PAUSE_STATE)) {
            if(mState.equals(START_STATE)) {
              Log.v("SoundService", "pause");
              onPause();
              contentView.setImageViewResource(R.id.sound_notification_play_and_pause, R.drawable.ic_play_arrow_black_24dp);
              mNotifyBuilder.setContent(contentView);
              mNotificationManager.notify(0, mNotifyBuilder.build());
            } else {
              Log.v("SoundService", "start");
              onResume();
              contentView.setImageViewResource(R.id.sound_notification_play_and_pause, R.drawable.ic_pause_black_24dp);
              mNotifyBuilder.setContent(contentView);
              mNotificationManager.notify(0, mNotifyBuilder.build());
            }
          }
        }
      }
    };
    mState = STOP_STATE;
  }

  @Override
  public int onStartCommand (Intent intent, int flags, int startId) {

    if (intent != null) {
      /*
      int message = intent.getIntExtra(SoundService.EXTRA_MESSAGE, 0);
      if(message == MESSAGE_START_STOP) {

        if(mState.equals(START_STATE)) {
          onPause();
          contentView.setImageViewResource(R.id.sound_notification_play_and_pause, R.drawable.ic_pause_black_24dp);
          mNotifyBuilder.setContent(contentView);
          mNotificationManager.notify(0, mNotifyBuilder.build());
        } else {
          onResume();
          contentView.setImageViewResource(R.id.sound_notification_play_and_pause, R.drawable.ic_play_arrow_black_24dp);
          mNotifyBuilder.setContent(contentView);
          mNotificationManager.notify(0, mNotifyBuilder.build());
        }
      } else if(message == MESSAGE_CLOSE)  {
        onDelete();
      }
      */
    }

    return START_STICKY;
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    if (mMediaPlayer != null && !mState.equals(STOP_STATE)) {
      mMediaPlayer.stop();
      mMediaPlayer.release();
    }
    if (mNotificationManager != null) {
      mNotificationManager.cancel(0);
    }
    mState = STOP_STATE;
    stopSelf();
  }

  public void onSound(String origin) {
    if (mState.equals(STOP_STATE)) {
      if (origin.equals("Imagery")) {
        mMediaPlayer = MediaPlayer.create(this, R.raw.imagineria);
        mCurrentSound = "Imagery";
      } else {
        mMediaPlayer = MediaPlayer.create(this, R.raw.nature);
        mCurrentSound = "Ambiental";
      }
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }
    mMediaPlayer.start();
    mIsReleased = false;
    mState = START_STATE;
    String url = "http://www.soundjay.com/nature/sounds/rain-01.mp3"; // your URL here
    nName = mPlayingSound.name;
    nDuration = "1:30";
  }

  public void onModifiedSound(String origin, int start) {
    if (mState.equals(STOP_STATE)) {
      if (origin.equals("Imagery")) {
        mMediaPlayer = MediaPlayer.create(this, R.raw.imagineria);
        mCurrentSound = "Imagery";
      } else {
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
    if(mState.equals(START_STATE)) {
      contentView = new RemoteViews(getPackageName(), R.layout.sound_notification_collapsed);
      contentView.setImageViewResource(R.id.sound_notification_icon, R.drawable.ic_headset_black_24dp);
      contentView.setImageViewResource(R.id.sound_notification_play_and_pause, R.drawable.ic_pause_black_24dp);
      contentView.setImageViewResource(R.id.sound_notification_close, R.drawable.ic_close_black_24dp);
      contentView.setImageViewResource(R.id.sound_notification_previous, R.drawable.ic_skip_previous_black_24dp);
      contentView.setImageViewResource(R.id.sound_notification_next, R.drawable.ic_skip_next_black_24dp);
      contentView.setImageViewResource(R.id.sound_notification_thumbnail, R.drawable.ic_library_music_black_24dp);

      Intent notificationIntent = new Intent(this, MainActivity.class);
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

      mNotifyBuilder = new NotificationCompat.Builder(this)
          .setContent(contentView)
          //.setCustomBigContentView(contentView)
          //.setOngoing(true)
          .setSmallIcon(R.drawable.ic_headset_black_24dp)
      ;

      mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

      //Pause event
      registerReceiver(mReceiver, new IntentFilter(PAUSE_STATE));
      contentView.setOnClickPendingIntent(R.id.sound_notification_play_and_pause, PendingIntent.getBroadcast(this, MESSAGE_START_STOP, new Intent(PAUSE_STATE), 0));

      //Close event
      registerReceiver(mReceiver, new IntentFilter(CLOSE_STATE));
      contentView.setOnClickPendingIntent(R.id.sound_notification_close, PendingIntent.getBroadcast(this, MESSAGE_CLOSE, new Intent(CLOSE_STATE), 0));

      //Swipe event
      registerReceiver(mReceiver, new IntentFilter(SWIPE_STATE));
      mNotifyBuilder.setDeleteIntent(PendingIntent.getBroadcast(this, MESSAGE_SWIPE, new Intent(SWIPE_STATE), 0));

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

  public Sound getSound() {
    return mPlayingSound;
  }

  public void setSound(Sound sound) {
    mPlayingSound = sound;
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


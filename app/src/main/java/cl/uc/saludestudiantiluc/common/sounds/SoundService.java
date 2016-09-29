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
import android.view.View;
import android.widget.RemoteViews;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;


public class SoundService extends Service implements MediaPlayer.OnPreparedListener {
  private BroadcastReceiver mReceiver;
  private boolean mIsReleased = true;
  private String mState = ""; //paused; played; stopped
  private String mName;
  private String mDuration;
  NotificationCompat.Builder mNotifyBuilder ;
  NotificationManager mNotificationManager;
  public static final String EXTRA_MESSAGE = "com.app.algo";
  public static final String START_STATE = "playing";
  public static final String STOP_STATE = "stopped";
  public static final String PAUSE_STATE = "paused";
  public static final String SWIPE_STATE = "swipped";
  public static final String NOTIFICATION_MESSAGE = "SoundNotificationMessage";
  public static final int MESSAGE_START_STOP = 1;
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
          } else if (action.equals(PAUSE_STATE)) {
            if(mState.equals(START_STATE)) {
              Log.v("SoundService", "pause");
              onPause();
              setViews(false);
            } else {
              setViews(true);
              Log.v("SoundService", "start");
              onResume();
            }
          }
        }
      }
    };
    mState = STOP_STATE;
  }

  @Override
  public int onStartCommand (Intent intent, int flags, int startId) {
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
        mMediaPlayer = MediaPlayer.create(this, R.raw.imagineria_en);
        mCurrentSound = "Imagery";
      } else {
        mMediaPlayer = MediaPlayer.create(this, R.raw.rain_audio);
        mCurrentSound = "Ambiental";
      }
      mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }
    mName = mPlayingSound.name;
    Log.v("Sound", mName);
    mDuration = "1:30";
    mMediaPlayer.start();
    mIsReleased = false;
    mState = START_STATE;
  }

  public void onModifiedSound(String origin, int start) {
    if (mState.equals(STOP_STATE)) {
      if (origin.equals("Imagery")) {
        mMediaPlayer = MediaPlayer.create(this, R.raw.imagineria_en);
        mCurrentSound = "Imagery";
      } else {
        mMediaPlayer = MediaPlayer.create(this, R.raw.rain_audio);
        mCurrentSound = "Ambiental";
      }
      mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }
    mMediaPlayer.seekTo(start);
    mMediaPlayer.start();
    mIsReleased = false;
    mState = START_STATE;
  }

  private RemoteViews getView(boolean isCollapsed, boolean showPause) {
    RemoteViews contentView = new RemoteViews(getPackageName(), isCollapsed ? R.layout.sound_notification_collapsed : R.layout.sound_notification_expanded);
    contentView.setImageViewResource(R.id.sound_notification_icon, R.drawable.ic_headset_black_24dp);
    contentView.setImageViewResource(R.id.sound_notification_play_and_pause, showPause ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp);
    contentView.setImageViewResource(R.id.sound_notification_previous, R.drawable.ic_skip_previous_black_24dp);
    contentView.setImageViewResource(R.id.sound_notification_next, R.drawable.ic_skip_next_black_24dp);
    contentView.setImageViewResource(R.id.sound_notification_thumbnail, R.drawable.ic_library_music_black_24dp);
    contentView.setTextViewText(R.id.sound_notification_title, "Relajación"); // <- mName no está funcionando

    // TEMPORARY REMOVAL OF PREVIOUS / NEXT BUTTONS
    contentView.setViewVisibility(R.id.sound_notification_previous, View.INVISIBLE);
    contentView.setViewVisibility(R.id.sound_notification_next, View.INVISIBLE);
    return contentView;
  }

  public void setViews(boolean showPause) {
    RemoteViews collapsedView = getView(true, showPause);
    RemoteViews expandedView = getView(false, showPause);

    //Pause events
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, MESSAGE_START_STOP, new Intent(PAUSE_STATE), 0);
    collapsedView.setOnClickPendingIntent(R.id.sound_notification_play_and_pause, pendingIntent);
    expandedView.setOnClickPendingIntent(R.id.sound_notification_play_and_pause, pendingIntent);
    mNotifyBuilder.setCustomContentView(collapsedView).setCustomBigContentView(expandedView);

    mNotificationManager.notify(0, mNotifyBuilder.build());
  }

  public void notifySound() {
    if(mState.equals(START_STATE)) {


      Intent notificationIntent = new Intent(this, MainActivity.class);
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


      mNotifyBuilder = new NotificationCompat.Builder(this)
          .setSmallIcon(R.drawable.ic_headset_black_24dp);

      //Swipe event
      registerReceiver(mReceiver, new IntentFilter(SWIPE_STATE));
      mNotifyBuilder.setDeleteIntent(PendingIntent.getBroadcast(this, MESSAGE_SWIPE, new Intent(SWIPE_STATE), 0));

      mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

      //Register pause event
      registerReceiver(mReceiver, new IntentFilter(PAUSE_STATE));

      setViews(true);
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

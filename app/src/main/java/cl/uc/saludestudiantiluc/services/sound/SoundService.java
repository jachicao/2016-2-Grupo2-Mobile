package cl.uc.saludestudiantiluc.services.sound;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import cl.uc.saludestudiantiluc.R;

public class SoundService extends Service {

  public static final String TAG = SoundService.class.getSimpleName();
  public static final String PLAY_STATE = "playing";
  public static final String STOP_STATE = "stopped";
  public static final String PAUSE_STATE = "paused";
  public static final String SWIPPED = "Swipped";
  public static final int MESSAGE_START_STOP = 1;
  public static final int MESSAGE_SWIPE = 3;

  private BroadcastReceiver mReceiver;
  private boolean mOnPrepareMediaPlayer = false;
  private String mState = STOP_STATE;
  private NotificationCompat.Builder mNotifyBuilder;
  private NotificationManager mNotificationManager;
  private MediaPlayer mMediaPlayer;
  private String mDisplayName = "";
  private boolean mNotificationVisible = false;
  private boolean mReceiverRegistered = false;
  private String mStringPath = "";
  private ArrayList<MediaPlayer.OnPreparedListener> mOnPreparedListeners = new ArrayList<>();
  private boolean mPlayNewSound = false;
  private int mMediaPlayerPosition = 0;

  /////////////////Comunication Section
  private final IBinder mBinder = new LocalBinder();

  public class LocalBinder extends Binder {
    public SoundService getService() {
      // Return this instance of LocalService so clients can call public methods
      return SoundService.this;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }
  ////End Comunication Section

  @Override
  public void onCreate() {
    super.onCreate();
    mReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (context != null && intent != null) {
          String action = intent.getAction();
          Log.v(TAG, "Notification: " + action);
          if (action.equals(SWIPPED)) {
            //unregisterReceiver(this);
            //destroyService();
            stopMediaPlayer();
          } else if (action.equals(PAUSE_STATE)) {
            if(mState.equals(PLAY_STATE)) {
              setNotificationView(false);
              pauseMediaPlayer();
            } else {
              setNotificationView(true);
              startMediaPlayer();
            }
          }
        }
      }
    };
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    Log.v(TAG, "onTaskRemoved");
    destroyService();
    super.onTaskRemoved(rootIntent);
  }

  private void unregisterReceiver() {
    if (mReceiverRegistered) {
      unregisterReceiver(mReceiver);
    }
  }

  private void destroyMediaPlayer() {
    if (mMediaPlayer != null) {
      mMediaPlayer.stop();
      mMediaPlayer.reset();
      mMediaPlayer.release();
      mMediaPlayer = null;
      mState = STOP_STATE;
    }
  }

  public void pauseSound() {
    pauseMediaPlayer();
  }

  public void startSound() {
    startMediaPlayer();
  }

  public void addOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
    mOnPreparedListeners.add(onPreparedListener);
  }

  public void newSound(final String filePath, String displayName, boolean play, final int playerPosition) {
    Log.v(TAG, "newSound" + " - " + (mMediaPlayer != null) + ", " + mOnPrepareMediaPlayer);
    if (mOnPrepareMediaPlayer) {
      return;
    }
    if (!filePath.equals(mStringPath)) {
      mPlayNewSound = play;
      mStringPath = filePath;
      mDisplayName = displayName;
      mMediaPlayerPosition = playerPosition;
      initMediaPlayer();
    }
  }

  private void initMediaPlayer() {
    if (mStringPath.equals("") || mDisplayName.equals("")) {
      return;
    }
    destroyMediaPlayer();
    mOnPrepareMediaPlayer = true;
    try {
      MediaPlayer mediaPlayer = new MediaPlayer();
      mediaPlayer
          .setDataSource(new FileInputStream(new File(mStringPath)).getFD());
      mediaPlayer.setLooping(true);
      mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
      mediaPlayer.prepareAsync();
      mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
          mOnPrepareMediaPlayer = false;
          mMediaPlayer = mp;
          mMediaPlayer.seekTo(mMediaPlayerPosition);
          if (mPlayNewSound) {
            startMediaPlayer();
          } else {
            pauseMediaPlayer();
          }
          onPrepareListeners();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void onPrepareListeners() {
    if (mOnPreparedListeners.size() > 0 && !mNotificationVisible) {
      for(MediaPlayer.OnPreparedListener onPreparedListener : mOnPreparedListeners) {
        if (onPreparedListener != null) {
          onPreparedListener.onPrepared(mMediaPlayer);
        }
      }
    }
  }

  private void startMediaPlayer() {
    Log.v(TAG, "startMediaPlayer");
    if (mMediaPlayer != null) {
      mState = PLAY_STATE;
      if (!mMediaPlayer.isPlaying()) {
        mMediaPlayer.start();
      }
      if (mNotificationVisible) {
        showNotification();
      }
    }
  }

  private void pauseMediaPlayer() {
    Log.v(TAG, "pauseMediaPlayer");
    if (mMediaPlayer != null) {
      mState = PAUSE_STATE;
      if (mMediaPlayer.isPlaying()) {
        mMediaPlayer.pause();
      }
      if (mNotificationVisible) {
        showNotification();
      }
    }
  }

  private void stopMediaPlayer() {
    Log.v(TAG, "stopMediaPlayer");
    if (mMediaPlayer != null) {
      destroyMediaPlayer();
      if (mNotificationVisible) {
        destroyNotification();
      }
    }
  }

  private void destroyService() {
    Log.v(TAG, "destroyService");
    mState = STOP_STATE;
    mStringPath = "";
    destroyMediaPlayer();
    destroyNotification();
    unregisterReceiver();
    stopSelf();
  }

  public String getMediaPlayerState() {
    if (mOnPrepareMediaPlayer) {
      if (mPlayNewSound) {
        return PLAY_STATE;
      } else {
        return PAUSE_STATE;
      }
    }
    return mState;
  }

  public MediaPlayer getMediaPlayer() {
    return mMediaPlayer;
  }

  public void onResume() {
    Log.v(TAG, "onResume");
    destroyNotification();
    if (mState.equals(STOP_STATE) && mMediaPlayer == null) {
      initMediaPlayer();
    }
  }

  public void onStop() {
    Log.v(TAG, "onStop");
    if (mMediaPlayer != null) {
      if (mMediaPlayer.isPlaying()) {
        showNotification();
      } else {
        destroyService();
      }
    }
  }

  private void destroyNotification() {
    if (mNotificationManager != null) {
      mNotificationManager.cancel(0);
      mNotificationVisible = false;
    }
  }

  private void showNotification() {
    mNotifyBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_headset_black_24dp);
    mNotifyBuilder
        .setDeleteIntent(
            PendingIntent.getBroadcast(this, MESSAGE_SWIPE, new Intent(SWIPPED), 0));

    mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    //Swipe event
    registerReceiver(mReceiver, new IntentFilter(SWIPPED));

    //Register pauseMediaPlayer event
    registerReceiver(mReceiver, new IntentFilter(PAUSE_STATE));

    mReceiverRegistered = true;


    //Set onClickEvents
    //Pause events
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, MESSAGE_START_STOP, new Intent(PAUSE_STATE), 0);

    //Collapsed view
    RemoteViews collapsedView = setNotificationView(true);
    collapsedView.setOnClickPendingIntent(R.id.sound_notification_play_and_pause, pendingIntent);
    mNotifyBuilder.setCustomContentView(collapsedView);

    //expandedView
    /*
    RemoteViews expandedView = setNotificationView(false);
    expandedView.setOnClickPendingIntent(R.id.sound_notification_play_and_pause, pendingIntent);
    mNotifyBuilder.setCustomBigContentView(expandedView);
    */

    mNotificationManager.notify(0, mNotifyBuilder.build());
    mNotificationVisible = true;
  }

  private RemoteViews setNotificationView(boolean isCollapsed) {
    RemoteViews contentView = new RemoteViews(getPackageName(), isCollapsed ? R.layout.sound_notification_collapsed : R.layout.sound_notification_expanded);
    contentView.setImageViewResource(R.id.sound_notification_icon, R.drawable.ic_headset_black_24dp);
    contentView.setImageViewResource(R.id.sound_notification_play_and_pause, getMediaPlayerState().equals(PLAY_STATE) ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp);
    contentView.setImageViewResource(R.id.sound_notification_previous, R.drawable.ic_skip_previous_black_24dp);
    contentView.setImageViewResource(R.id.sound_notification_next, R.drawable.ic_skip_next_black_24dp);
    contentView.setImageViewResource(R.id.sound_notification_thumbnail, R.drawable.ic_library_music_black_24dp);
    contentView.setTextViewText(R.id.sound_notification_title, mDisplayName);

    // TODO ADD PREVIOUS / NEXT BUTTONS
    contentView.setViewVisibility(R.id.sound_notification_previous, View.INVISIBLE);
    contentView.setViewVisibility(R.id.sound_notification_next, View.INVISIBLE);


    return contentView;
  }
}

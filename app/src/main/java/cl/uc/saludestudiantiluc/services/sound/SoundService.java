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
import android.os.Build;
import android.os.Bundle;
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

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;

public class SoundService extends Service implements MediaPlayer.OnPreparedListener {

  public static final int MEDIA_PLAYER_STATE_PLAY = 0;
  public static final int MEDIA_PLAYER_STATE_PAUSE = 1;
  public static final int MEDIA_PLAYER_STATE_STOP = 2;

  private static final String TAG = SoundService.class.getSimpleName();
  private static final String ACTION = "ActionService";
  private static final String ACTION_PLAY_PAUSE = "PlayPause";
  private static final String ACTION_SWIPE = "Swipe";
  private static final String ACTION_CLICK = "Click";
  private static final int MESSAGE_PLAY_PAUSE = 1;
  private static final int MESSAGE_SWIPE = 3;

  private BroadcastReceiver mReceiver;
  private boolean mOnPrepareMediaPlayer = false;
  private int mMediaPlayerState = MEDIA_PLAYER_STATE_STOP;
  private NotificationManager mNotificationManager;
  private MediaPlayer mMediaPlayer;
  private String mDisplayName = "";
  private String mStringPath = "";
  private boolean mPlayNewSound = false;
  private int mMediaPlayerPosition = 0;
  private boolean mLoop = false;
  private boolean mNotificationVisible = false;
  private boolean mReceiverRegistered = false;
  private boolean mOnSaveInstanceState = false;
  private ArrayList<MediaPlayer.OnPreparedListener> mOnPreparedListeners = new ArrayList<>();

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
          switch (action) {
            case ACTION:
              boolean playPause = intent.getBooleanExtra(ACTION_PLAY_PAUSE, false);
              if (playPause) {
                if (mMediaPlayerState == MEDIA_PLAYER_STATE_PLAY) {
                  setNotificationView(false);
                  pauseMediaPlayer();
                } else {
                  setNotificationView(true);
                  startMediaPlayer();
                }
              } else {
                boolean swipe = intent.getBooleanExtra(ACTION_SWIPE, false);
                if (swipe) {
                  //unregisterReceiver(this);
                  //destroyService();
                  stopMediaPlayer();
                } else {
                  boolean click = intent.getBooleanExtra(ACTION_CLICK, false);
                  if (click) {
                    Intent clickIntent = new Intent(SoundService.this, MainActivity.class);
                    //SoundService.this.startActivity(clickIntent); //TODO: Open activity when clicking
                  }
                }
              }
              break;
            default:
              break;
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
      mMediaPlayerState = MEDIA_PLAYER_STATE_STOP;
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

  public void newSound(String filePath,
                       String displayName,
                       boolean play,
                       int playerPosition,
                       boolean loop
                       ) {
    Log.v(TAG, "newSound");
    if (mOnPrepareMediaPlayer) {
      Log.v(TAG, "mOnPrepareMediaPlayer");
      return;
    }
    Log.v(TAG, "oldFile: " + mStringPath);
    Log.v(TAG, "newFile: " + filePath);
    Log.v(TAG, "state: " + getMediaPlayerState());
    if (filePath.equals(mStringPath)) {
      switch (getMediaPlayerState()) {
        case MEDIA_PLAYER_STATE_PLAY:
          Log.v(TAG, "return");
          return;
        case MEDIA_PLAYER_STATE_PAUSE:
          Log.v(TAG, "return");
          return;
        default:
          break;
      }
    }
    mStringPath = filePath;
    mDisplayName = displayName;
    mPlayNewSound = play;
    mMediaPlayerPosition = playerPosition;
    mLoop = loop;
    initMediaPlayer();
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
      if (mLoop) {
        mediaPlayer.setLooping(true);
      }
      mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
      mediaPlayer.prepareAsync();
      mediaPlayer.setOnPreparedListener(this);
    } catch (IOException error) {
      error.printStackTrace();
    }
  }


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

  private void onPrepareListeners() {
    if (mOnPreparedListeners.size() > 0 && !mNotificationVisible) {
      for (MediaPlayer.OnPreparedListener onPreparedListener : mOnPreparedListeners) {
        if (onPreparedListener != null) {
          onPreparedListener.onPrepared(mMediaPlayer);
        }
      }
    }
  }

  private void startMediaPlayer() {
    Log.v(TAG, "startMediaPlayer");
    if (mMediaPlayer != null) {
      mMediaPlayerState = MEDIA_PLAYER_STATE_PLAY;
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
      mMediaPlayerState = MEDIA_PLAYER_STATE_PAUSE;
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
    mMediaPlayerState = MEDIA_PLAYER_STATE_STOP;
    mStringPath = "";
    destroyMediaPlayer();
    destroyNotification();
    unregisterReceiver();
    stopSelf();
  }

  public int getMediaPlayerState() {
    if (mOnPrepareMediaPlayer) {
      if (mPlayNewSound) {
        return MEDIA_PLAYER_STATE_PLAY;
      } else {
        return MEDIA_PLAYER_STATE_PAUSE;
      }
    }
    return mMediaPlayerState;
  }

  public MediaPlayer getMediaPlayer() {
    return mMediaPlayer;
  }

  public void onServiceConnected() {
    destroyNotification();
  }

  public void onResume() {
    Log.v(TAG, "onResume");
    destroyNotification();
  }

  public void onStop() {
    Log.v(TAG, "onStop");
    switch (getMediaPlayerState()) {
      case MEDIA_PLAYER_STATE_PLAY:
        showNotification();
        break;
      case MEDIA_PLAYER_STATE_PAUSE:
        if (!mOnSaveInstanceState) {
          destroyService();
        }
        break;
      default:
        destroyService();
        break;
    }
    mOnSaveInstanceState = false;
  }

  public void onSaveInstanceState(Bundle outState) {
    mOnSaveInstanceState = true;
  }

  private void destroyNotification() {
    if (mNotificationManager != null) {
      mNotificationManager.cancel(0);
      mNotificationVisible = false;
    }
  }

  private void showNotification() {

    Intent swipeIntent = new Intent(ACTION).putExtra(ACTION_SWIPE, true);

    Intent playPauseIntent = new Intent(ACTION).putExtra(ACTION_PLAY_PAUSE, true);

    Intent clickIntent = new Intent(ACTION).putExtra(ACTION_CLICK, true);

    NotificationCompat.Builder notifyBuilder
        = new NotificationCompat.Builder(this);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      notifyBuilder.setSmallIcon(R.drawable.ic_headset_black_24dp);
    } else {
      notifyBuilder.setSmallIcon(R.drawable.ic_headset_white_24dp);
    }
    
    notifyBuilder
        .setContentIntent(
            PendingIntent.getBroadcast(this, 0, clickIntent, 0));

    notifyBuilder
        .setDeleteIntent(
            PendingIntent.getBroadcast(this, MESSAGE_SWIPE, swipeIntent, 0));

    mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    //Set onClickEvents
    //Pause events
    PendingIntent pendingIntent
        = PendingIntent.getBroadcast(this, MESSAGE_PLAY_PAUSE, playPauseIntent, 0);

    //Collapsed view
    RemoteViews collapsedView = setNotificationView(true);
    collapsedView.setOnClickPendingIntent(R.id.sound_notification_play_and_pause, pendingIntent);
    notifyBuilder.setCustomContentView(collapsedView);

    //expandedView
    /*
    RemoteViews expandedView = setNotificationView(false);
    expandedView.setOnClickPendingIntent(R.id.sound_notification_play_and_pause, pendingIntent);
    mNotifyBuilder.setCustomBigContentView(expandedView);
    */


    //register events
    registerReceiver(mReceiver, new IntentFilter(ACTION));

    mReceiverRegistered = true;

    mNotificationManager.notify(0, notifyBuilder.build());
    mNotificationVisible = true;
  }

  private RemoteViews setNotificationView(boolean isCollapsed) {
    RemoteViews contentView = new RemoteViews(
        getPackageName(),
        isCollapsed
            ? R.layout.sound_notification_collapsed
            : R.layout.sound_notification_expanded
    );
    contentView.setImageViewResource(
        R.id.sound_notification_icon,
        R.drawable.ic_headset_black_24dp
    );
    contentView.setImageViewResource(
        R.id.sound_notification_play_and_pause,
        getMediaPlayerState() == MEDIA_PLAYER_STATE_PLAY
            ? R.drawable.ic_pause_black_24dp
            : R.drawable.ic_play_arrow_black_24dp
    );
    contentView.setImageViewResource(
        R.id.sound_notification_previous,
        R.drawable.ic_skip_previous_black_24dp
    );
    contentView.setImageViewResource(
        R.id.sound_notification_next,
        R.drawable.ic_skip_next_black_24dp
    );
    contentView.setImageViewResource(
        R.id.sound_notification_thumbnail,
        R.drawable.ic_library_music_black_24dp
    );
    contentView.setTextViewText(
        R.id.sound_notification_title,
        mDisplayName
    );

    // TODO ADD PREVIOUS / NEXT BUTTONS
    contentView.setViewVisibility(R.id.sound_notification_previous, View.INVISIBLE);
    contentView.setViewVisibility(R.id.sound_notification_next, View.INVISIBLE);
    return contentView;
  }
}

package cl.uc.saludestudiantiluc.sos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.breathingexcercises.squarebreathing.SquareBreathingActivity;

public class SosService extends Service {

  public static final String SOS = "sos";
  public static final int MESSAGE_START_STOP = 1;


  private NotificationCompat.Builder mNotifyBuilder;
  private NotificationManager mNotificationManager;
  private BroadcastReceiver mReceiver;
  private boolean mNotificated;


  public SosService() {
  }

  /////////////////Comunication Section
  private final IBinder mBinder = new LocalBinder();

  public class LocalBinder extends Binder {
    public SosService getService() {
      // Return this instance of LocalService so clients can call public methods
      return SosService.this;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }
  ////End Comunication Section

  @Override
  public void onDestroy() {
    super.onDestroy();
    undoNotification();
  }

  @Override
  public void onCreate() {

    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    boolean notifyPref = sharedPref.getBoolean("notification", true);
    if (notifyPref){
      mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          if (context != null && intent != null) {
            String action = intent.getAction();
            if (action.equals(SOS)) {
              //unregisterReceiver(this);
              //destroyService();
              Intent notificationIntent = new Intent(context, SquareBreathingActivity.class);
              notificationIntent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(notificationIntent);

              Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
              context.sendBroadcast(it);
            } else {
              setNotificationView(true);
            }
          }
        }
      };
      mNotificated = true;
      showNotification();
    } else {
      if(mNotificated) {
        mNotificated = false;
        undoNotification();
      }

    }
  }

  private void undoNotification() {
    try {
      mNotificationManager.cancel(1);
    }
    catch (Exception e) {

    }
  }


  private void showNotification() {
    mNotifyBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable
        .ic_favorite_black_24dp);

    mNotifyBuilder.setOngoing(true);
    mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    RemoteViews collapsedView = setNotificationView(true);
    mNotifyBuilder.setCustomContentView(collapsedView);

    //SOS event
    registerReceiver(mReceiver, new IntentFilter(SOS));

    //Set onClickEvents
    //SOS events
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, MESSAGE_START_STOP, new Intent
        (SOS), PendingIntent.FLAG_UPDATE_CURRENT);

    collapsedView.setOnClickPendingIntent(R.id.sos_notification_play_and_pause, pendingIntent);
    mNotificationManager.notify(1, mNotifyBuilder.build());
  }

  private RemoteViews setNotificationView(boolean isCollapsed) {
    RemoteViews contentView = new RemoteViews(getPackageName(), isCollapsed ? R.layout
        .sos_notification_collapsed : R
        .layout.sos_notification_collapsed);
    contentView.setImageViewResource(R.id.sos_notification_icon, R.drawable
        .ic_favorite_black_24dp);

    contentView.setImageViewResource(R.id.sos_notification_play_and_pause, R.drawable.ic_relaxing);
    return contentView;
  }
}

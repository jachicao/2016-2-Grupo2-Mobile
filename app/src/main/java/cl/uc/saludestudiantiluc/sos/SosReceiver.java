package cl.uc.saludestudiantiluc.sos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

 public class SosReceiver extends BroadcastReceiver {
  Intent myIntent;
  Context mContext;
  public SosReceiver() {
  }

  @Override
  public void onReceive(Context context, Intent intent) {

    myIntent = new Intent(context, SosService.class);
    mContext = context;

    context.startService(myIntent);

  }

  public void stopService() {
    mContext.stopService(myIntent);
  }
}

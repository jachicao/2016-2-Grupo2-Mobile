package cl.uc.saludestudiantiluc.sos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class SosReceiver extends BroadcastReceiver {
  public SosReceiver() {
  }

  @Override
  public void onReceive(Context context, Intent intent) {

    Intent myIntent = new Intent(context, SosService.class);
    context.startService(myIntent);

  }
}

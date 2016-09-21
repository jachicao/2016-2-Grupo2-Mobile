package cl.uc.saludestudiantiluc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Static utility methods useful when setting the views.
 */
public final class ViewUtils {

  private ViewUtils() {
    // Prevent the instantiation of this class.
  }

  public static float convertPixelsToDp(int px, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }

  /**
   * Returns the screen width in pixels.
   */
  public static int getScreenWidth(Activity activity) {
    DisplayMetrics metrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    return metrics.widthPixels;
  }

}

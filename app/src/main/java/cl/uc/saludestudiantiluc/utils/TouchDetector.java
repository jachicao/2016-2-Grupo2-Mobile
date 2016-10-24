package cl.uc.saludestudiantiluc.utils;

import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jchicao on 10/23/16.
 */

public class TouchDetector {

  public static final int ON_SINGLE_TAP_UP = 0;
  public static final int ON_LONG_PRESS = 1;
  public static final int ON_SINGLE_TAP_CONFIRMED = 2;
  public static final int ON_DOUBLE_TAP = 3;

  public static void register(View view, final TouchListener touchListener) {
    final GestureDetectorCompat detectorCompat = new GestureDetectorCompat(view.getContext(), new android.view.GestureDetector.OnGestureListener() {

      @Override
      public boolean onDown(MotionEvent e) {
        return true;
      }

      @Override
      public void onShowPress(MotionEvent e) {
      }

      @Override
      public boolean onSingleTapUp(MotionEvent e) {
        if (touchListener != null) {
          touchListener.onTouch(ON_SINGLE_TAP_UP);
        }
        return true;
      }

      @Override
      public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
      }

      @Override
      public void onLongPress(MotionEvent e) {
        if (touchListener != null) {
          touchListener.onTouch(ON_LONG_PRESS);
        }
      }

      @Override
      public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
      }
    });
    detectorCompat.setOnDoubleTapListener(new android.view.GestureDetector.OnDoubleTapListener() {
      @Override
      public boolean onSingleTapConfirmed(MotionEvent e) {
        if (touchListener != null) {
          touchListener.onTouch(ON_SINGLE_TAP_CONFIRMED);
        }
        return true;
      }

      @Override
      public boolean onDoubleTap(MotionEvent e) {
        if (touchListener != null) {
          touchListener.onTouch(ON_DOUBLE_TAP);
        }
        return true;
      }

      @Override
      public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
      }
    });
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (detectorCompat != null) {
          detectorCompat.onTouchEvent(event);
        }
        return true;
      }
    });
  }
}

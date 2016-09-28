package cl.uc.saludestudiantiluc.design;

/**
 * Created by jchicao on 9/24/16.
 */

public class BottomSheetItem {

  private int mDrawableRes;
  private String mTitle;
  private BottomSheetItemListener mListener;
  public BottomSheetItem(int drawable, String title, BottomSheetItemListener listener) {
    mDrawableRes = drawable;
    mTitle = title;
    mListener = listener;
  }
  public int getDrawableResource() {
    return mDrawableRes;
  }

  public String getTitle() {
    return mTitle;
  }
  public void onClick() {
    if (mListener != null) {
      mListener.onClick(this);
    }
  }
}
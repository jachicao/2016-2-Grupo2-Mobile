package cl.uc.saludestudiantiluc.calendar.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by camilo on 05-11-16.
 */

public class CancelResponse implements Parcelable {
  boolean canceled;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.canceled ? (byte) 1 : (byte) 0);
  }

  public CancelResponse() {
  }

  public boolean getCanceled () {
    return canceled;
  }

  protected CancelResponse(Parcel in) {
    this.canceled = in.readByte() != 0;
  }

  public static final Parcelable.Creator<CancelResponse> CREATOR = new Parcelable.Creator<CancelResponse>() {
    @Override
    public CancelResponse createFromParcel(Parcel source) {
      return new CancelResponse(source);
    }

    @Override
    public CancelResponse[] newArray(int size) {
      return new CancelResponse[size];
    }
  };
}

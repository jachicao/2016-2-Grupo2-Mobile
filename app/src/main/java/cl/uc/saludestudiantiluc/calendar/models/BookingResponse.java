package cl.uc.saludestudiantiluc.calendar.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by camilo on 06-11-16.
 */
public class BookingResponse implements Parcelable {
  boolean available;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.available ? (byte) 1 : (byte) 0);
  }

  public BookingResponse() {
  }

  public boolean getAvailability() {
    return available;
  }

  protected BookingResponse(Parcel in) {
    this.available = in.readByte() != 0;
  }

  public static final Parcelable.Creator<BookingResponse> CREATOR = new Parcelable.Creator<BookingResponse>() {
    @Override
    public BookingResponse createFromParcel(Parcel source) {
      return new BookingResponse(source);
    }

    @Override
    public BookingResponse[] newArray(int size) {
      return new BookingResponse[size];
    }
  };
}

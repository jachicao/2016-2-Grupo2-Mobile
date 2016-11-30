package cl.uc.saludestudiantiluc.exerciseplans.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by camilo on 27-11-16.
 */

public class ExerciseResponse implements Parcelable {
  int current;
  boolean success;

  public ExerciseResponse(int current, boolean success) {
    this.current = current;
    this.success = success;
  }

  public boolean isSuccessful() {
    return success;
  }

  public int getCurrent() {
    return current;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.current);
    dest.writeByte(this.success ? (byte) 1 : (byte) 0);
  }

  protected ExerciseResponse(Parcel in) {
    this.current = in.readInt();
    this.success = in.readByte() != 0;
  }

  public static final Parcelable.Creator<ExerciseResponse> CREATOR = new Parcelable.Creator<ExerciseResponse>() {
    @Override
    public ExerciseResponse createFromParcel(Parcel source) {
      return new ExerciseResponse(source);
    }

    @Override
    public ExerciseResponse[] newArray(int size) {
      return new ExerciseResponse[size];
    }
  };
}

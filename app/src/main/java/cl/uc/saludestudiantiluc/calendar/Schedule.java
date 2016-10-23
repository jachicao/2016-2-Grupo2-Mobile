package cl.uc.saludestudiantiluc.calendar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by camilo on 18-10-16.
 */

public class Schedule implements Parcelable {
  String professional;
  String timestamp;
  String activity;
  String campus;
  //String location;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.professional);
    dest.writeString(this.timestamp);
    dest.writeString(this.activity);
    dest.writeString(this.campus);
    //dest.writeString(this.location);
  }

  public Schedule() {
  }

  protected Schedule(Parcel in) {
    this.professional = in.readString();
    this.timestamp = in.readString();
    this.activity = in.readString();
    this.campus = in.readString();
    //this.location = in.readString();
  }

  public String getTimestamp(){
    return timestamp;
  }

  public String getProfessional(){
    return professional;
  }

  public String getService(){
    return activity;
  }

  public String getCampus(){
    return campus;
  }

 /* public String getLocation(){
    return location;
  }*/

  public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
    @Override
    public Schedule createFromParcel(Parcel source) {
      return new Schedule(source);
    }

    @Override
    public Schedule[] newArray(int size) {
      return new Schedule[size];
    }
  };
}

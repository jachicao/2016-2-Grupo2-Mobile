package cl.uc.saludestudiantiluc.calendar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by camilo on 18-10-16.
 */

public class Schedule implements Parcelable {
  int id;
  int host_id;
  String start_date;
  String event_type;
  String faculty;
  String location;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeInt(this.host_id);
    dest.writeString(this.start_date);
    dest.writeString(this.event_type);
    dest.writeString(this.faculty);
    dest.writeString(this.location);
  }

  public Schedule() {
  }

  protected Schedule(Parcel in) {
    this.id = in.readInt();
    this.host_id = in.readInt();
    this.start_date = in.readString();
    this.event_type = in.readString();
    this.faculty = in.readString();
    this.location = in.readString();
  }

  public String getTimestamp(){
    return start_date;
  }

  public int getProfessional(){
    return host_id;
  }

  public String getEvent_type(){
    return event_type;
  }

  public String getCampus(){
    return faculty;
  }

  public int getId() { return id; }

  public String getLocation(){
    return location;
  }

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

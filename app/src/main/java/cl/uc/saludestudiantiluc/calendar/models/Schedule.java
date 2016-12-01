package cl.uc.saludestudiantiluc.calendar.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by camilo on 18-10-16.
 */

public class Schedule implements Parcelable {
  int id;
  String host;
  String start_date;
  String event_type;
  String faculty;
  String location;
  @SerializedName("user_booked")
  private boolean booked;


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.host);
    dest.writeString(this.start_date);
    dest.writeString(this.event_type);
    dest.writeString(this.faculty);
    dest.writeString(this.location);
    if (booked) {
      dest.writeInt(1);
    } else {
      dest.writeInt(0);
    }
  }

  public Schedule(int id, String host, String start, String event, String faculty, String loc) {
    this.id = id;
    this.host = host;
    this.start_date = start;
    this.event_type = event;
    this.faculty = faculty;
    this.location = loc;
  }

  protected Schedule(Parcel in) {
    this.id = in.readInt();
    this.host = in.readString();
    this.start_date = in.readString();
    this.event_type = in.readString();
    this.faculty = in.readString();
    this.location = in.readString();
    if (in.readInt() == 1) {
      booked = true;
    } else {
      booked = false;
    }
  }

  public String getTimestamp(){
    return start_date;
  }

  public String getProfessional(){
    return host;
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

  public boolean isBooked() {
    return booked;
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

package cl.uc.saludestudiantiluc.common.sounds;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by camilo on 15-09-16.
 */
public class Sound implements Parcelable {
  private int id = 0;
  private String name = "";
  private String description = "";
  private String type = "";
  private int duration = 0;


  public Sound(int i, String n, String desc, String type, int duration) {
    id = i;
    name = n;
    description = desc;
    this.type = type;
    this.duration = duration;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.name);
    dest.writeString(this.description);
    dest.writeString(this.type);
    dest.writeInt(this.duration);
  }

  protected Sound(Parcel in) {
    this.id = in.readInt();
    this.name = in.readString();
    this.description = in.readString();
    this.type = in.readString();
    this.duration = in.readInt();
  }

  public static final Creator<Sound> CREATOR = new Creator<Sound>() {
    @Override
    public Sound createFromParcel(Parcel source) {
      return new Sound(source);
    }

    @Override
    public Sound[] newArray(int size) {
      return new Sound[size];
    }
  };

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getType() {
    return type;
  }

  public int getDuration() {
    return duration;
  }
}
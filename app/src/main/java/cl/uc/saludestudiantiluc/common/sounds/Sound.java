package cl.uc.saludestudiantiluc.common.sounds;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by camilo on 15-09-16.
 */
public class Sound implements Parcelable {
  public int id = 0;
  public String name = "";
  public String description = "";
  public String type = "";
  public String preview = "";
  public String sound = "";
  private int duration = 0;


  public Sound(int i, String n, String desc, String type, int duration) {
    id = i;
    name = n;
    description = desc;
    this.type = type;
    this.duration = duration;
  }

  protected Sound(Parcel in) {
    id = in.readInt();
    name = in.readString();
    description = in.readString();
    type = in.readString();
    preview = in.readString();
    sound = in.readString();
    duration = in.readInt();
  }

  public static final Creator<Sound> CREATOR = new Creator<Sound>() {
    @Override
    public Sound createFromParcel(Parcel in) {
      return new Sound(in);
    }

    @Override
    public Sound[] newArray(int size) {
      return new Sound[size];
    }
  };

  public String getType() {
    return type;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(description);
    dest.writeString(type);
    dest.writeString(preview);
    dest.writeString(sound);
    dest.writeInt(duration);
  }
}
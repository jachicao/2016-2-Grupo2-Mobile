package cl.uc.saludestudiantiluc.exerciseplans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.services.download.FileRequest;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by camilo on 10-11-16.
 */

public class ExerciseSound implements Parcelable {

  private int program_id;
  private int order;
  private int unlocked;

  @SerializedName("sound")
  private ExerciseSoundData mExerciseSoundData;


  protected ExerciseSound(Parcel in) {
    program_id = in.readInt();
    order = in.readInt();
    unlocked = in.readInt();
    mExerciseSoundData = in.readParcelable(getClass().getClassLoader());

  }

  public static final Parcelable.Creator<ExerciseSound> CREATOR = new Parcelable.Creator<ExerciseSound>() {
    @Override
    public ExerciseSound createFromParcel(Parcel in) {
      return new ExerciseSound(in);
    }

    @Override
    public ExerciseSound[] newArray(int size) {
      return new ExerciseSound[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(program_id);
    dest.writeInt(order);
    dest.writeInt(unlocked);
    dest.writeParcelable(mExerciseSoundData, flags);
  }

  public int getProgram_id() {
    return program_id;
  }

  public int getOrder() { return order; }

  public int getUnlocked() {
    return unlocked;
  }

  public void setUnlocked(int x) {
    unlocked = x;
  }


  public ExerciseSoundData getExerciseSoundData() {
    return mExerciseSoundData;
  }

}

package cl.uc.saludestudiantiluc.exerciseplans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by camilo on 06-11-16.
 */

public class ExercisePlan implements ParentObject, Parcelable {

  private List<Object> mChildrenList;
  private String name;
  private String description;
  private List<ExerciseSound> sound_sequences;

  public ExercisePlan(String s, List<Object> list){
    name = s;
    mChildrenList = list;
  }

  public static final Creator<ExercisePlan> CREATOR = new Creator<ExercisePlan>() {
    @Override
    public ExercisePlan createFromParcel(Parcel in) {
      return new ExercisePlan(in);
    }

    @Override
    public ExercisePlan[] newArray(int size) {
      return new ExercisePlan[size];
    }
  };

  @Override
  public List<Object> getChildObjectList() {
    return mChildrenList;
  }

  public String getTitle() {
    return name;
  }

  public List<ExerciseSound> getExercises() {
    return sound_sequences;
  }

  protected ExercisePlan(Parcel in) {
    this.name = in.readString();
    this.description = in.readString();
    this.sound_sequences = new ArrayList<>();
    in.readList(this.sound_sequences, getClass().getClassLoader());
  }

  @Override
  public void setChildObjectList(List<Object> list) {
    mChildrenList = list;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.description);
    dest.writeTypedList(this.sound_sequences);
  }
}

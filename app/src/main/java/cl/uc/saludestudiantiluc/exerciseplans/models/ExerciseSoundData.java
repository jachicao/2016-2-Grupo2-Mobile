package cl.uc.saludestudiantiluc.exerciseplans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import cl.uc.saludestudiantiluc.services.download.FileRequest;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by camilo on 15-11-16.
 */

public class ExerciseSoundData implements Parcelable {

  public static final String EXERCISE_SOUNDS_CACHE_PATH = "/exercise/sounds/";

  @SerializedName("id")
  private int mId = 0;

  @SerializedName("name")
  private String mName = "";

  @SerializedName("description")
  private String mDescription = "";

  @SerializedName("duration")
  private int mDuration = 0;

  @SerializedName("sound_file_file_name")
  private String mAudioFileName;

  @SerializedName("sound_file")
  private String mAudioUrl;


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.mId);
    dest.writeString(this.mName);
    dest.writeString(this.mDescription);
    dest.writeInt(this.mDuration);
    dest.writeString(this.mAudioFileName);
    dest.writeString(this.mAudioUrl);
  }

  public ExerciseSoundData() {
  }

  protected ExerciseSoundData(Parcel in) {
    this.mId = in.readInt();
    this.mName = in.readString();
    this.mDescription = in.readString();
    this.mDuration = in.readInt();
    this.mAudioFileName = in.readString();
    this.mAudioUrl = in.readString();
  }

  public static final Parcelable.Creator<ExerciseSoundData> CREATOR = new Parcelable.Creator<ExerciseSoundData>() {
    @Override
    public ExerciseSoundData createFromParcel(Parcel source) {
      return new ExerciseSoundData(source);
    }

    @Override
    public ExerciseSoundData[] newArray(int size) {
      return new ExerciseSoundData[size];
    }
  };

  public String getName() {
    return mName;
  }

  public int getId() {
    return mId;
  }

  public FileRequest getSoundRequest() {
    return new FileRequest(mAudioUrl, EXERCISE_SOUNDS_CACHE_PATH + mAudioFileName);
  }

  public FilesRequest getFilesRequest() {
    FilesRequest filesRequest = new FilesRequest();
    filesRequest.addRequest(getSoundRequest());
    return filesRequest;
  }
}

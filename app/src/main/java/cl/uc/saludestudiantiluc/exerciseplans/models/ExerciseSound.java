package cl.uc.saludestudiantiluc.exerciseplans.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.common.models.BaseFragmentListModel;
import cl.uc.saludestudiantiluc.services.download.FileRequest;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by camilo on 10-11-16.
 */

public class ExerciseSound extends BaseFragmentListModel implements Parcelable {
  private static final String EXERCISE_SOUNDS_CACHE_PATH = "/exercise_plan/sounds/";

  private static final String EXERCISE_VIDEO_CACHE_PATH = "/exercise_plan/videos/";

  @SerializedName("video_file_file_name")
  private String mVideoFileName;

  @SerializedName("audio_file_file_name")
  private String mAudioFileName;

  @SerializedName("video_url")
  private String mVideoUrl;


  @SerializedName("audio_url")
  private String mAudioUrl;

  protected ExerciseSound(Parcel in) {
    super(in);
    mVideoFileName = in.readString();
    mAudioFileName = in.readString();
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
    super.writeToParcel(dest, flags);
    dest.writeString(mVideoFileName);
    dest.writeString(mAudioFileName);
  }

  public FileRequest getSoundRequest() {
    return new FileRequest(mAudioUrl, EXERCISE_SOUNDS_CACHE_PATH + mAudioFileName);
  }

  public FileRequest getVideoRequest() {
    return new FileRequest(mVideoUrl, EXERCISE_VIDEO_CACHE_PATH + mVideoFileName);
  }

  public FilesRequest getFilesRequest() {
    FilesRequest filesRequest = new FilesRequest();
    filesRequest.addRequest(getVideoRequest());
    filesRequest.addRequest(getSoundRequest());
    return filesRequest;
  }
}

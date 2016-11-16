package cl.uc.saludestudiantiluc.imageries.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.services.download.FileRequest;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by camilo on 15-09-16.
 */

public class Imagery extends Media implements Parcelable {

  private static final String IMAGERY_PREVIEW_CACHE_PATH = "/imagery/previews/";

  private static final String IMAGERY_SOUNDS_CACHE_PATH = "/imagery/sounds/";

  private static final String IMAGERY_VIDEOS_CACHE_PATH = "/imagery/sounds/";

  @SerializedName("sound_file_file_name")
  private String mSoundFileName = "";

  @SerializedName("sound_url")
  private String mSoundUrl = "";

  @SerializedName("video_file_file_name")
  private String mVideoFileName = "";

  @SerializedName("video_url")
  private String mVideoUrl = "";

  protected Imagery(Parcel in) {
    super(in);
    mSoundFileName = in.readString();
    mSoundUrl = in.readString();
    mVideoFileName = in.readString();
    mVideoUrl = in.readString();
  }

  public static final Creator<Imagery> CREATOR = new Creator<Imagery>() {
    @Override
    public Imagery createFromParcel(Parcel in) {
      return new Imagery(in);
    }

    @Override
    public Imagery[] newArray(int size) {
      return new Imagery[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(mSoundFileName);
    dest.writeString(mSoundUrl);
    dest.writeString(mVideoFileName);
    dest.writeString(mVideoUrl);
  }

  public FileRequest getSoundRequest() {
    return new FileRequest(mSoundUrl, IMAGERY_SOUNDS_CACHE_PATH + mSoundFileName);
  }

  public FileRequest getVideoRequest() {
    return new FileRequest(mVideoUrl, IMAGERY_VIDEOS_CACHE_PATH + mVideoFileName);
  }

  @Override
  public FileRequest getPreviewRequest() {
    return new FileRequest(mPreviewUrl, IMAGERY_PREVIEW_CACHE_PATH + mPreviewName);
  }

  @Override
  public FilesRequest getFilesRequest() {
    FilesRequest filesRequest = new FilesRequest();
    filesRequest.addRequest(getVideoRequest());
    filesRequest.addRequest(getSoundRequest());
    return filesRequest;
  }
}
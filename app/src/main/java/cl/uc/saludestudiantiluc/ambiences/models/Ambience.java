package cl.uc.saludestudiantiluc.ambiences.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.download.FileRequest;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by jchicao on 10/20/16.
 */

public class Ambience extends Media implements Parcelable {

  private static final String AMBIENCE_PREVIEW_CACHE_PATH = "/nature/previews/";

  private static final String AMBIANCE_SOUNDS_CACHE_PATH = "/nature/sounds/";

  private static final String AMBIENCE_VIDEOS_CACHE_PATH = "/nature/videos/";

  @SerializedName("video_file_file_name")
  private String mVideoFileName;

  @SerializedName("audio_file_file_name")
  private String mAudioFileName;

  @SerializedName("video_url")
  private String mVideoUrl;


  @SerializedName("audio_url")
  private String mAudioUrl;

  protected Ambience(Parcel in) {
    super(in);
    mVideoFileName = in.readString();
    mAudioFileName = in.readString();
  }

  public static final Creator<Ambience> CREATOR = new Creator<Ambience>() {
    @Override
    public Ambience createFromParcel(Parcel in) {
      return new Ambience(in);
    }

    @Override
    public Ambience[] newArray(int size) {
      return new Ambience[size];
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

  @Override
  public FileRequest getPreviewRequest() {
    return new FileRequest(mPreviewUrl, AMBIENCE_PREVIEW_CACHE_PATH + mPreviewName);
  }

  public FileRequest getSoundRequest() {
    return new FileRequest(mAudioUrl, AMBIANCE_SOUNDS_CACHE_PATH + mAudioFileName);
  }

  public FileRequest getVideoRequest() {
    return new FileRequest(mVideoUrl, AMBIENCE_VIDEOS_CACHE_PATH + mVideoFileName);
  }

  @Override
  public FilesRequest getFilesRequest() {
    FilesRequest filesRequest = new FilesRequest();
    filesRequest.addRequest(getVideoRequest());
    filesRequest.addRequest(getSoundRequest());
    return filesRequest;
  }
}

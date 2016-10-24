package cl.uc.saludestudiantiluc.ambiences.models;

import android.os.Parcel;
import android.os.Parcelable;

import cl.uc.saludestudiantiluc.ambiences.api.AmbienceApi;
import cl.uc.saludestudiantiluc.common.models.BaseFragmentListModel;
import cl.uc.saludestudiantiluc.services.download.FileRequest;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by jchicao on 10/20/16.
 */

public class Ambience extends BaseFragmentListModel implements Parcelable {

  private String video;
  private String sound;

  protected Ambience(Parcel in) {
    super(in);
    video = in.readString();
    sound = in.readString();
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
    dest.writeString(video);
    dest.writeString(sound);
  }

  public FileRequest getSoundRequest() {
    return new FileRequest(AmbienceApi.BASE_URL, sound);
  }

  public FileRequest getVideoRequest() {
    return new FileRequest(AmbienceApi.BASE_URL, video);
  }

  public FilesRequest getFilesRequest() {
    FilesRequest filesRequest = new FilesRequest();
    filesRequest.addRequest(getVideoRequest());
    filesRequest.addRequest(getSoundRequest());
    return filesRequest;
  }
}

package cl.uc.saludestudiantiluc.imageries.models;

import android.os.Parcel;
import android.os.Parcelable;

import cl.uc.saludestudiantiluc.common.models.BaseFragmentListModel;
import cl.uc.saludestudiantiluc.services.download.FileRequest;
import cl.uc.saludestudiantiluc.imageries.api.ImageryApi;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by camilo on 15-09-16.
 */
public class Imagery extends BaseFragmentListModel implements Parcelable {

  public String sound = "";
  public String video = "";

  protected Imagery(Parcel in) {
    super(in);
    sound = in.readString();
    video = in.readString();
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
    dest.writeString(sound);
    dest.writeString(video);
  }

  public FileRequest getSoundRequest() {
    return new FileRequest(ImageryApi.BASE_URL, sound);
  }

  public FileRequest getVideoRequest() {
    return new FileRequest(ImageryApi.BASE_URL, video);
  }

  public FilesRequest getFilesRequest() {
    FilesRequest filesRequest = new FilesRequest();
    filesRequest.addRequest(getVideoRequest());
    filesRequest.addRequest(getSoundRequest());
    return filesRequest;
  }
}
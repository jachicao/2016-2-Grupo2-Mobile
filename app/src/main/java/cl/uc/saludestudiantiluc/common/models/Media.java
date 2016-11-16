package cl.uc.saludestudiantiluc.common.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.download.FileRequest;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by jchicao on 10/20/16.
 */

public class Media implements Parcelable {

  private static final String PREVIEW_CACHE_PATH = "/preview/";

  @SerializedName("id")
  protected int mId = 0;

  @SerializedName("name")
  protected String mName = "";

  @SerializedName("description")
  protected String mDescription = "";

  @SerializedName("image_file_file_name")
  protected String mPreviewName = "";

  @SerializedName("image_url")
  protected String mPreviewUrl = "";

  protected Media(Parcel in) {
    mId = in.readInt();
    mName = in.readString();
    mDescription = in.readString();
    mPreviewName = in.readString();
    mPreviewUrl = in.readString();
  }

  public static final Creator<Media> CREATOR = new Creator<Media>() {
    @Override
    public Media createFromParcel(Parcel in) {
      return new Media(in);
    }

    @Override
    public Media[] newArray(int size) {
      return new Media[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mId);
    dest.writeString(mName);
    dest.writeString(mDescription);
    dest.writeString(mPreviewName);
    dest.writeString(mPreviewUrl);
  }

  public FileRequest getPreviewRequest() {
    return new FileRequest(mPreviewUrl, PREVIEW_CACHE_PATH + mPreviewName);
  }

  public int getId() {
    return mId;
  }

  public FilesRequest getFilesRequest() {
    return new FilesRequest();
  }

  public boolean isDownloaded(Context context) {
    return DownloadService.containsFiles(context, getFilesRequest());
  }

  public String getName() {
    return mName;
  }

  public String getDescription() {
    return mDescription;
  }
}

package cl.uc.saludestudiantiluc.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import cl.uc.saludestudiantiluc.ambiences.api.AmbienceApi;
import cl.uc.saludestudiantiluc.services.download.FileRequest;

/**
 * Created by jchicao on 10/20/16.
 */

public class BaseFragmentListModel implements Parcelable {
  public int id = 0;
  public String name = "";
  public String description = "";
  public String preview = "";

  protected BaseFragmentListModel(Parcel in) {
    id = in.readInt();
    name = in.readString();
    description = in.readString();
    preview = in.readString();
  }

  public static final Creator<BaseFragmentListModel> CREATOR = new Creator<BaseFragmentListModel>() {
    @Override
    public BaseFragmentListModel createFromParcel(Parcel in) {
      return new BaseFragmentListModel(in);
    }

    @Override
    public BaseFragmentListModel[] newArray(int size) {
      return new BaseFragmentListModel[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(description);
    dest.writeString(preview);
  }

  public FileRequest getPreviewRequest() {
    return new FileRequest(AmbienceApi.BASE_URL, preview);
  }

}

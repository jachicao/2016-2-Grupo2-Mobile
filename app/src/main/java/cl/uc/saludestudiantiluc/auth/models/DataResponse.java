package cl.uc.saludestudiantiluc.auth.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jchicao on 21-09-16.
 */

class DataResponse {

  @SerializedName("id")
  private int mId;

  @SerializedName("email")
  private String mEmail;

  int getId() {
    return mId;
  }

  String getEmail() {
    return mEmail;
  }
}

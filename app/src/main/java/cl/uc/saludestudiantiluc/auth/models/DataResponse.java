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

  @SerializedName("academic_type")
  private String mAcademicType;

  @SerializedName("name")
  private String mName = "";


  int getId() {
    return mId;
  }

  String getEmail() {
    return mEmail;
  }

  String getAcademicType() {
    return mAcademicType;
  }

  String getName() {
    return mName;
  }
}

package cl.uc.saludestudiantiluc.auth.models;

/**
 * Created by jchicao on 21-09-16.
 */

public class LoginResponse {

  public int getId() {
    return data.getId();
  }

  public String getEmail() {
    return data.getEmail();
  }

  public String getAcademicType() {
    return data.getAcademicType();
  }

  public DataResponse data;
}
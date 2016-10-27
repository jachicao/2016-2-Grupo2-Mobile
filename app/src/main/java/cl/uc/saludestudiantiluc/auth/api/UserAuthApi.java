package cl.uc.saludestudiantiluc.auth.api;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.auth.models.LoginResponse;
import cl.uc.saludestudiantiluc.auth.models.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jchicao on 21-09-16.
 */

public interface UserAuthApi {

  String BASE_URL = BuildConfig.HOST + "/api/v1/";

  @FormUrlEncoded
  @POST("auth")
  Call<RegisterResponse> register(@Field("email") String email,
                                  @Field("password") String password,
                                  @Field("password_confirmation") String password_confirmation);

  @FormUrlEncoded
  @POST("auth/sign_in")
  Call<LoginResponse> logIn(@Field("email") String email,
                            @Field("password") String password);

}

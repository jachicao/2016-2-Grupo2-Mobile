package cl.uc.saludestudiantiluc.auth;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by jchicao on 21-09-16.
 */

interface UserAuthApi {

  String BASE_URL = "http://private-83f2b-test10256.apiary-mock.com/api/v1/";

  @FormUrlEncoded
  @Headers("Content-Type: application/json")
  @POST("auth")
  Call<RegisterResponse> register(@Field("email") String email,
                                  @Field("password") String password,
                                  @Field("password_confirmation") String password_confirmation);

  @FormUrlEncoded
  @Headers("Content-Type: application/json")
  @POST("auth/sign_in")
  Call<LoginResponse> logIn(@Field("email") String email,
                            @Field("password") String password);

}

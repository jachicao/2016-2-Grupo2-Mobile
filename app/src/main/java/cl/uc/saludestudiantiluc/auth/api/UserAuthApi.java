package cl.uc.saludestudiantiluc.auth.api;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.auth.models.LoginResponse;
import cl.uc.saludestudiantiluc.auth.models.RegisterResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
                                  @Field("password_confirm") String password_confirm,
                                  @Field("name") String name,
                                  @Field("rut") String rut,
                                  @Field("age") Integer age,
                                  @Field("academic_type") String academic_type,
                                  @Field("sex") String sex,
                                  @Field("school") String school,
                                  @Field("year") Integer year
                                  );



  @FormUrlEncoded
  @POST("auth/sign_in")
  Call<LoginResponse> logIn(@Field("email") String email,
                            @Field("password") String password
                            );

  @GET("auth/validate_token")
  Call<ResponseBody> validateToken();

}

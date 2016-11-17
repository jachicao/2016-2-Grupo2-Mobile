package cl.uc.saludestudiantiluc.calendar.api;

import java.util.List;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.calendar.models.BookingResponse;
import cl.uc.saludestudiantiluc.calendar.models.CancelResponse;
import cl.uc.saludestudiantiluc.calendar.models.Schedule;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by camilo on 16-10-16.
 */

public interface CalendarApi {
  static final String BASE_URL =  BuildConfig.HOST + "/api/v1/calendar/";

  @GET("event_dates")
  Call<List<Schedule>> getAvailableHours(@Query("event_type") int activity, @Query("campus") int
      campus, @Header("email") String email);


  @GET("user_events")
  Call<List<Schedule>> getUserHours(@Header("email") String email);

  @FormUrlEncoded
  @POST("book")
  Call<BookingResponse> booking(@Field("id-event") int eventId, @Header("email") String email);


  @FormUrlEncoded
  @POST("cancel_book")
  Call<CancelResponse> cancel(@Field("id-event") int id_event, @Header("email") String email);

}
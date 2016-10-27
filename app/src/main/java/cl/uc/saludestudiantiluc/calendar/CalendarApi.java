package cl.uc.saludestudiantiluc.calendar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by camilo on 16-10-16.
 */

public interface CalendarApi {
  String BASE_URL = "https://apihorario.herokuapp.com/api/";

  @GET("horarios/filter")
  Call<List<Schedule>> getAvailableHours(@Query("activity") String activity, @Query("campus") String campus);

  @GET("horarios")
  Call<List<Schedule>> getHours();


}
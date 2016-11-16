package cl.uc.saludestudiantiluc.calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.auth.data.UserRepository;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleActivity extends BaseActivity {

  private ListView mListView;
  private LinearLayout mLinearLayout;

  private LayoutInflater mInflater;

  private ArrayList<Schedule> mSchedule;

  private CalendarApi mApiInstance;

  private String mSource;

  private boolean mLoaded;
  private boolean mIsDialogShown;

  private Schedule mDialogSchedule;
  private int mCardLayout;

  private static final String SERVICE_SELECTION = "Service";
  private static final String CAMPUS_SELECTION = "Campus";
  private static final String SAVE_REQUESTED = "Requested";
  private static final String SAVE_SELECTED_SCHEDULE = "Selected schedule";
  private static final String SAVE_SCHEDULE_LIST = "Schedule list";
  private static final String SAVE_DIALOG_STATE = "Dialog state";
  private static final String SOURCE = "Source";
  private static final String AVAILABLE_HOURS = "Available hours";
  private static final String USER_HOURS = "User hours";

  private UserRepository mUserRepository;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.available_hours_list);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
    getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });
    Glide
        .with(this)
        .load(R.drawable.main_background)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .centerCrop()
        .into((ImageView) findViewById(R.id.main_background_image));

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(CalendarApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    mApiInstance = retrofit.create(CalendarApi.class);

    mLoaded = false;
    mIsDialogShown = false;
    Bundle extras = getIntent().getExtras();
    int service;
    int campus;
    if (extras != null) {
      mSource = extras.getString(SOURCE);
      if (mSource.equals(AVAILABLE_HOURS)) {
        service = extras.getInt(SERVICE_SELECTION);
        campus = extras.getInt(CAMPUS_SELECTION);
      } else {
        service = -1;
        campus = -1;
      }

    } else {
      mSource = null;
      service = -1;
      campus = -1;
    }
    if (savedInstanceState != null) {
      mLoaded = savedInstanceState.getBoolean(SAVE_REQUESTED);
      mSchedule = savedInstanceState.getParcelableArrayList(SAVE_SCHEDULE_LIST);
      mDialogSchedule = savedInstanceState.getParcelable(SAVE_SELECTED_SCHEDULE);
      mIsDialogShown = savedInstanceState.getBoolean(SAVE_DIALOG_STATE);
    }
    mListView = new ListView(this);
    mLinearLayout = (LinearLayout) findViewById(R.id.hourContainer);
    mInflater = (LayoutInflater) this
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (!mLoaded) {
      mUserRepository = getUserRepository();
      String a = mUserRepository.getUserAccessToken();
      String b = mUserRepository.getUserAccessTokenClient();
      String d = mUserRepository.getUserPassword();
      String c = "jiji";
      Call<List<Schedule>> callInstance;
      if (mSource.equals(AVAILABLE_HOURS)) {
        callInstance = mApiInstance.getAvailableHours(service, campus,
            mUserRepository.getUserEmail(), mUserRepository.getUserPassword(),
            mUserRepository.getUserAccessTokenClient(), mUserRepository.getUserAccessToken(),
            mUserRepository.getUid());
      } else {
        callInstance = mApiInstance.getUserHours(mUserRepository.getUserEmail());
      }

      callInstance.enqueue(new Callback<List<Schedule>>() {
        @Override
        public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
          if (response.isSuccessful()) {
            List<Schedule> scheduleList = response.body();
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(scheduleList);
            mSchedule = gson.fromJson(json, new TypeToken<List<Schedule>>() {
            }.getType());
          }
          loadHours();
        }

        @Override
        public void onFailure(Call<List<Schedule>> call, Throwable t) {
          loadHours();
        }
      });
    } else {
      if (mSchedule != null) {
        if (mSchedule.size() > 0) {
          loadListAdapter();
          if (mIsDialogShown) {
            if (mSource.equals(AVAILABLE_HOURS)) {
              showConfirmationDialog(mDialogSchedule.getId());
            } else {
              showCancelDialog(mDialogSchedule.getId());
            }
          }
        } else {
          loadEmptyMessage();
        }
      } else {
        loadEmptyMessage();
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putBoolean(SAVE_REQUESTED, mLoaded);
    savedInstanceState.putParcelableArrayList(SAVE_SCHEDULE_LIST, mSchedule);
    savedInstanceState.putParcelable(SAVE_SELECTED_SCHEDULE, mDialogSchedule);
    savedInstanceState.putBoolean(SAVE_DIALOG_STATE, mIsDialogShown);
    super.onSaveInstanceState(savedInstanceState);
  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, ScheduleActivity.class).putExtra(SOURCE, USER_HOURS);
  }

  public void loadHours() {
    mLoaded = true;
    if (mSchedule != null) {
      if (mSchedule.size() > 0) {
        Collections.sort(mSchedule, new Comparator<Schedule>() {
          SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
          @Override
          public int compare(Schedule o1, Schedule o2) {
            Date date1;
            Date date2;
            if (o1.getProfessional() > o2.getProfessional()) {
              return 1;
            } else if (o1.getProfessional() > o2.getProfessional()) {
              return 0;
            } else {
              try {
                date1 = dateFormat.parse(o1.getTimestamp());
                date2 = dateFormat.parse(o2.getTimestamp());
                return date1.compareTo(date2);
              } catch (ParseException e) {
                e.printStackTrace();
              }
              return 0;
            }
          }
        });

        loadListAdapter();
      } else {
        loadEmptyMessage();
      }
    } else {
      loadEmptyMessage();
    }
  }


  public void loadListAdapter() {

    //Eliminate progress bar
    View circleView = findViewById(R.id.progressBarLayout);
    mLinearLayout.removeView(circleView);

    //Create CardView
    View loadView = mInflater.inflate(
        R.layout.available_hour_list, mLinearLayout);

    //Obtain ListLayout
    LinearLayout listViewLayout = (LinearLayout) loadView.findViewById(R.id.listLayout);

    //Set adapter and add the list
    mListView.setAdapter(new YourAdapter(this, mSchedule));
    listViewLayout.addView(mListView);

  }

  public void loadEmptyMessage() {
    View circleView = findViewById(R.id.progressBarLayout);
    mLinearLayout.removeView(circleView);
    View v = mInflater.inflate(
        R.layout.empty_list_message, mLinearLayout);
    TextView t = (TextView) v.findViewById(R.id.cardMessage);
    if (mSource.equals(AVAILABLE_HOURS)) {
      t.setText(getResources().getString(R.string.empty_message));
    } else {
      t.setText(getResources().getString(R.string.empty_schedule));
    }
  }

  public void showConfirmationDialog(final int eventId) {
    mIsDialogShown = true;
    final Dialog dialog = new Dialog(ScheduleActivity.this);
    dialog.setContentView(R.layout.activity_confirmation);
    dialog.setTitle(this.getText(R.string.booking_confirmation));
    Button reserve = (Button) dialog.findViewById(R.id.confirmReservationButton);
    Button cancel = (Button) dialog.findViewById(R.id.cancelReservationButton);
    TextView date = (TextView) dialog.findViewById(R.id.confirmation_date);
    TextView professional = (TextView) dialog.findViewById(R.id.confirmation_professional);
    TextView service = (TextView) dialog.findViewById(R.id.confirmation_service);
    TextView campus = (TextView) dialog.findViewById(R.id.confirmation_campus);
    String dateText = this.getString(R.string.date_start) + mDialogSchedule.getTimestamp();
    String professionalText = this.getString(R.string.professional_start) + mDialogSchedule.getProfessional();
    String serviceText = this.getString(R.string.service_start) + mDialogSchedule.getEvent_type();
    String campusText = this.getString(R.string.campus_start) + mDialogSchedule.getCampus();
    date.setText(dateText);
    professional.setText(professionalText);
    service.setText(serviceText);
    campus.setText(campusText);
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        mIsDialogShown = false;
      }
    });
    reserve.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Call<BookingResponse> callInstance;
        callInstance = mApiInstance.booking(eventId, mUserRepository.getUserEmail());
        callInstance.enqueue(new Callback<BookingResponse>() {
          @Override
          public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
            if (response.isSuccessful()) {
              Gson gson = new GsonBuilder().create();
              String json = gson.toJson(response.body());
              BookingResponse bookingResponse = gson.fromJson(json, new TypeToken<BookingResponse>() {
              }.getType());
              System.out.println(bookingResponse.getAvailability());
            }
            mIsDialogShown = false;
            dialog.dismiss();
            Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          }

          @Override
          public void onFailure(Call<BookingResponse> call, Throwable t) {
            mIsDialogShown = false;
            dialog.dismiss();
            Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          }
        });

      }
    });
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mIsDialogShown = false;
        dialog.dismiss();
      }
    });
  }

  public void showCancelDialog(final int eventId) {
    mIsDialogShown = true;
    AlertDialog dialog = new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Cancelación de hora")
        .setCancelable(false)
        .setMessage("¿Está seguro que desea cancelar su hora?")
        .setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Call<CancelResponse> callInstance;
            callInstance = mApiInstance.cancel(eventId, mUserRepository.getUserEmail());
            callInstance.enqueue(new Callback<CancelResponse>() {
              @Override
              public void onResponse(Call<CancelResponse> call, Response<CancelResponse> response) {
                if (response.isSuccessful()) {
                  Gson gson = new GsonBuilder().create();
                  String json = gson.toJson(response.body());
                  CancelResponse cancelResponse = gson.fromJson(json, new TypeToken<CancelResponse>() {
                  }.getType());
                  System.out.println(cancelResponse.getCanceled());
                }
                mIsDialogShown = false;
                finish();
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
              }

              @Override
              public void onFailure(Call<CancelResponse> call, Throwable t) {
                mIsDialogShown = false;
                finish();
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
              }
            });
            //finish();
          }

        })
        .setNegativeButton("No", null)
        .show();
    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        mIsDialogShown = false;
      }
    });
  }




  private class YourAdapter extends BaseAdapter {

    private Context mContext;
    private List<Schedule> mSchedule;

    public YourAdapter(Context context, List<Schedule> scheduleList) {
      mContext = context;
      mSchedule = scheduleList;
    }

    @Override
    public int getCount() {
      return mSchedule.size();
    }

    @Override
    public Object getItem(int position) {
      return mSchedule.get(position);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int cardLayout;
        if (mSource.equals(AVAILABLE_HOURS)) {
          cardLayout = R.layout.schedule_list_elem;
        } else {
          cardLayout = R.layout.schedule_reserved_hours;
        }
        convertView = (View) inflater.inflate(
            cardLayout, null);
      }

      final TextView date = (TextView) convertView.findViewById(R.id.dateText);
      final TextView professional = (TextView) convertView.findViewById(R.id.profText);
      final TextView location = (TextView) convertView.findViewById(R.id.locationText);
      Button reserve = (Button) convertView.findViewById(R.id.appointmentButton);
      if (mSource.equals(AVAILABLE_HOURS)) {
        reserve.setText(getResources().getString(R.string.reserve_button));
        reserve.setTextColor(getResources().getColor(R.color.cyan_700));
      } else {
        reserve.setText(getResources().getString(R.string.cancel_button));
        final TextView service = (TextView) convertView.findViewById(R.id.serviceText);
        final TextView campus = (TextView) convertView.findViewById(R.id.campusText);
        service.setText(mSchedule.get(position).getEvent_type());
        campus.setText(mSchedule.get(position).getCampus());
        reserve.setTextColor(getResources().getColor(R.color.red_700));
      }
      int p1 = mSchedule.get(position).getTimestamp().indexOf(':');
      int ind = mSchedule.get(position).getTimestamp().indexOf(':', p1+1);
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm");
      Date d;
      try {
        d = dateFormat.parse(mSchedule.get(position).getTimestamp().replace("T", " ").substring(0, ind));
        dateFormat.applyPattern("dd-MM-yyyy H:mm");
      } catch (ParseException e) {
        d = new Date();
        e.printStackTrace();
      }
      date.setText(dateFormat.format(d));
      professional.setText(String.valueOf(mSchedule.get(position).getProfessional()));
      location.setText(mSchedule.get(position).getLocation());
      reserve.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mDialogSchedule = mSchedule.get(position);
          if (mSource.equals(AVAILABLE_HOURS)) {
            showConfirmationDialog(mDialogSchedule.getId());
          } else {
            showCancelDialog(mDialogSchedule.getId());
          }
        }
      });
      return convertView;
    }
  }
}

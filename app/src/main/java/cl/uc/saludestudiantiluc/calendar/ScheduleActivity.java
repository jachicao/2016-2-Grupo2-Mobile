package cl.uc.saludestudiantiluc.calendar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.data;

public class ScheduleActivity extends BaseActivity {

  private ListView mListView;
  private LinearLayout mLinearLayout;

  private LayoutInflater mInflater;

  private ArrayList<Schedule> mSchedule;

  private String mDialogDate;
  private String mDialogProfessional;
  private String mDialogService;
  private String mDialogCampus;

  private boolean mLoaded;
  private boolean mIsDialogShown;

  private static final String SERVICE_SELECTION = "Service";
  private static final String CAMPUS_SELECTION = "Campus";
  private static final String SAVE_REQUESTED = "Requested";
  private static final String SAVE_SCHEDULE_LIST = "Schedule list";
  private static final String SAVE_DIALOG_STATE = "Dialog state";
  private static final String SAVE_DIALOG_DATE = "Dialog date";
  private static final String SAVE_DIALOG_PROFESSIONAL = "Dialog professional";
  private static final String SAVE_DIALOG_SERVICE = "Dialog service";
  private static final String SAVE_DIALOG_CAMPUS = "Dialog campus";


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
        .load(R.drawable.sunset_background)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .centerCrop()
        .into((ImageView) findViewById(R.id.main_background_image));

    mLoaded = false;
    mIsDialogShown = false;
    Bundle extras = getIntent().getExtras();
    String service;
    String campus;
    if (extras != null) {
      service = extras.getString(SERVICE_SELECTION);
      campus = extras.getString(CAMPUS_SELECTION);
    } else {
      service = null;
      campus = null;
    }
    if (savedInstanceState != null) {
      mLoaded = savedInstanceState.getBoolean(SAVE_REQUESTED);
      mSchedule = savedInstanceState.getParcelableArrayList(SAVE_SCHEDULE_LIST);
      mIsDialogShown = savedInstanceState.getBoolean(SAVE_DIALOG_STATE);
      mDialogDate = savedInstanceState.getString(SAVE_DIALOG_DATE);
      mDialogProfessional = savedInstanceState.getString(SAVE_DIALOG_PROFESSIONAL);
      mDialogService = savedInstanceState.getString(SAVE_DIALOG_SERVICE);
      mDialogCampus = savedInstanceState.getString(SAVE_DIALOG_CAMPUS);
    }
    mListView = new ListView(this);
    mLinearLayout = (LinearLayout) findViewById(R.id.hourContainer);
    mInflater = (LayoutInflater) this
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (!mLoaded) {
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(CalendarApi.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build();

      CalendarApi apiInstance = retrofit.create(CalendarApi.class);
      Call<List<Schedule>> callInstance = apiInstance.getAvailableHours(service, campus);
      callInstance.enqueue(new Callback<List<Schedule>>() {
        @Override
        public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
          if (response.isSuccessful()) {
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(response.body());
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
        loadListAdapter();
        if (mIsDialogShown) {
          showConfirmationDialog();
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
    savedInstanceState.putBoolean(SAVE_DIALOG_STATE, mIsDialogShown);
    savedInstanceState.putString(SAVE_DIALOG_DATE, mDialogDate);
    savedInstanceState.putString(SAVE_DIALOG_PROFESSIONAL, mDialogProfessional);
    savedInstanceState.putString(SAVE_DIALOG_SERVICE, mDialogService);
    savedInstanceState.putString(SAVE_DIALOG_CAMPUS, mDialogCampus);
    super.onSaveInstanceState(savedInstanceState);
  }

  public void loadHours() {
    mLoaded = true;
    if (mSchedule != null) {
      if (mSchedule.size() > 0) {
        mDialogCampus = mSchedule.get(0).getCampus();
        mDialogService = mSchedule.get(0).getService();
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
    mInflater.inflate(
        R.layout.empty_list_message, mLinearLayout);
  }

  public void showConfirmationDialog() {
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

    String dateText = this.getString(R.string.date_start) + mDialogDate;
    String professionalText = this.getString(R.string.professional_start) + mDialogProfessional;
    String serviceText = this.getString(R.string.service_start) + mDialogService;
    String campusText = this.getString(R.string.campus_start) + mDialogCampus;

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
        mIsDialogShown = false;
        dialog.dismiss();
        Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = (View) inflater.inflate(
            R.layout.schedule_list_elem, null);
      }

      final TextView date = (TextView) convertView.findViewById(R.id.dateText);
      final TextView professional = (TextView) convertView.findViewById(R.id.profText);
      Button reserve = (Button) convertView.findViewById(R.id.appointmentButton);
      date.setText(mSchedule.get(position).getTimestamp());
      professional.setText(mSchedule.get(position).getProfessional());
      reserve.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mDialogDate = date.getText().toString();
          mDialogProfessional = professional.getText().toString();
          showConfirmationDialog();
        }
      });
      return convertView;
    }
  }
}

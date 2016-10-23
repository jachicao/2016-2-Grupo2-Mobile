package cl.uc.saludestudiantiluc.calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

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
  private ArrayList<Schedule> mSchedule;
  private CalendarApi mApiInstance;
  private LinearLayout mLinearLayout;
  private View mLoadView;
  private boolean mLoaded;
  private LayoutInflater mInflater;
  private boolean mIsDialogShown;
  private String mDialogDate;
  private String mDialogProfessional;
  private String mDialogService;
  private String mDialogCampus;

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
      service = extras.getString("service");
      campus = extras.getString("campus");
    } else {
      service = null;
      campus = null;
    }

    if (savedInstanceState != null) {
      mLoaded = savedInstanceState.getBoolean("requested");
      mSchedule = savedInstanceState.getParcelableArrayList("schedule");
      mIsDialogShown = savedInstanceState.getBoolean("dialog");
      mDialogDate = savedInstanceState.getString("dialogDate");
      mDialogProfessional = savedInstanceState.getString("dialogProfessional");
      mDialogService = savedInstanceState.getString("dialogService");
      mDialogCampus = savedInstanceState.getString("dialogCampus");
    }

    mLinearLayout = (LinearLayout) findViewById(R.id.listLayout);
    mListView = new ListView(this);
    mInflater = (LayoutInflater) this
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mLoadView = (View) mInflater.inflate(
        R.layout.circle_progress_bar, null);
    mLoadView.setLayoutParams(new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT, 1f));
    mLinearLayout.addView(mLoadView);
    if (!mLoaded) {
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(CalendarApi.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build();

      mApiInstance = retrofit.create(CalendarApi.class);
      Call<List<Schedule>> callInstance = mApiInstance.getAvailableHours(service, campus);
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
    savedInstanceState.putBoolean("requested", mLoaded);
    savedInstanceState.putParcelableArrayList("schedule", mSchedule);
    savedInstanceState.putBoolean("dialog", mIsDialogShown);
    savedInstanceState.putString("dialogDate", mDialogDate);
    savedInstanceState.putString("dialogProfessional", mDialogProfessional);
    savedInstanceState.putString("dialogService", mDialogService);
    savedInstanceState.putString("dialogCampus", mDialogCampus);
    super.onSaveInstanceState(savedInstanceState);
  }

  public void loadHours(){
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
    mListView.setAdapter(new YourAdapter(this, mSchedule));
    mLinearLayout.removeView(mLoadView);
    mLinearLayout.addView(mListView);
  }

  public void loadEmptyMessage() {
    View view = (View) mInflater.inflate(
        R.layout.empty_list_message, null);
    view.setLayoutParams(new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT, 1f));
    mLinearLayout.removeView(mLoadView);
    mLinearLayout.addView(view);
  }

  public void showConfirmationDialog() {
    mIsDialogShown = true;
    final Dialog dialog = new Dialog(ScheduleActivity.this);
    dialog.setContentView(R.layout.activity_confirmation);
    dialog.setTitle("Confirmación de hora");
    Button reserve = (Button)dialog.findViewById(R.id.confirmReservationButton);
    Button cancel = (Button)dialog.findViewById(R.id.cancelReservationButton);
    TextView date = (TextView)dialog.findViewById(R.id.confirmation_date);
    TextView professional = (TextView)dialog.findViewById(R.id.confirmation_professional);
    TextView service = (TextView)dialog.findViewById(R.id.confirmation_service);
    TextView campus = (TextView)dialog.findViewById(R.id.confirmation_campus);
    date.setText("Fecha: " + mDialogDate);
    professional.setText("Profesional: " + mDialogProfessional);
    service.setText("Servicio: " + mDialogService);
    campus.setText("Campus: " + mDialogCampus);
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
        //Snackbar snackbar = Snackbar.make(android.R.layout.activity_main, "hola", Snackbar.LENGTH_LONG);
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

      final TextView date = (TextView)convertView.findViewById(R.id.dateText);
      final TextView professional = (TextView)convertView.findViewById(R.id.profText);
      Button reserve = (Button)convertView.findViewById(R.id.appointmentButton);
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

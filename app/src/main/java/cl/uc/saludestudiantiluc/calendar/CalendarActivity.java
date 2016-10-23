package cl.uc.saludestudiantiluc.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;

public class CalendarActivity extends BaseActivity{

  private Spinner mServiceSpinner;
  private Spinner mCampusSpinner;
  private Button mButton;

  private final String SERVICE_SELECTION = "Service";
  private final String CAMPUS_SELECTION = "Campus";

  private boolean mServiceSelected;
  private boolean mCampusSelected;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.service_select);
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

    mButton = (Button) findViewById(R.id.button);

    mServiceSpinner =  (Spinner) findViewById(R.id.spinner);
    ArrayAdapter<CharSequence> serviceAdapter = ArrayAdapter.createFromResource(this,
        R.array.service_array, android.R.layout.simple_spinner_item);
    serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mServiceSpinner.setAdapter( new NothingSelectedSpinnerAdapter(serviceAdapter, R.layout.contact_spinner_row_nothing_selected, this));
    mServiceSpinner.setOnItemSelectedListener(new OnItemSelectedListener(SERVICE_SELECTION));

    mCampusSpinner =  (Spinner) findViewById(R.id.spinner3);
    ArrayAdapter<CharSequence> campusAdapter = ArrayAdapter.createFromResource(this,
        R.array.campus_array, android.R.layout.simple_spinner_item);
    campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mCampusSpinner.setAdapter( new NothingSelectedSpinnerAdapter(campusAdapter, R.layout.contact_spinner_row_nothing_selected, this));
    mCampusSpinner.setOnItemSelectedListener(new OnItemSelectedListener(CAMPUS_SELECTION));

    mCampusSpinner.setEnabled(false);
    mButton.setEnabled(false);
    mButton.setTextColor(Color.parseColor("#C0C0C0"));

    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mButton.isEnabled() && mServiceSelected && mCampusSelected) {
          Intent intent = new Intent(CalendarActivity.this, ScheduleActivity.class);
          intent.putExtra("service", mServiceSpinner.getSelectedItem().toString());
          intent.putExtra("campus", mCampusSpinner.getSelectedItem().toString());
          startActivity(intent);
        }

      }
    });

  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, CalendarActivity.class);
  }

  class OnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private String mSource;

    public OnItemSelectedListener(String s){
      mSource = s;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

      Object obj = parent.getItemAtPosition(pos);
      if (obj != null) {
        if (mSource.equals(SERVICE_SELECTION)) {
          mServiceSelected = true;
          mCampusSpinner.setEnabled(true);
        } else if (mSource.equals(CAMPUS_SELECTION)) {
          mCampusSelected = true;
        }

        if (mServiceSelected && mCampusSelected) {
          mButton.setTextColor(Color.parseColor("#000000"));
          mButton.setEnabled(true);
        }
      }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }


  }

  public class NothingSelectedSpinnerAdapter implements SpinnerAdapter, ListAdapter {

    protected static final int EXTRA = 1;
    protected SpinnerAdapter adapter;
    protected Context context;
    protected int nothingSelectedLayout;
    protected int nothingSelectedDropdownLayout;
    protected LayoutInflater layoutInflater;


    public NothingSelectedSpinnerAdapter(
        SpinnerAdapter spinnerAdapter,
        int nothingSelectedLayout, Context context) {

      this(spinnerAdapter, nothingSelectedLayout, -1, context);
    }

    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter,
                                         int nothingSelectedLayout, int nothingSelectedDropdownLayout, Context context) {
      this.adapter = spinnerAdapter;
      this.context = context;
      this.nothingSelectedLayout = nothingSelectedLayout;
      this.nothingSelectedDropdownLayout = nothingSelectedDropdownLayout;
      layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
      // This provides the View for the Selected Item in the Spinner, not
      // the dropdown (unless dropdownView is not set).
      if (position == 0) {
        return getNothingSelectedView(parent);
      }
      return adapter.getView(position - EXTRA, null, parent); // Could re-use
      // the convertView if possible.
    }

    protected View getNothingSelectedView(ViewGroup parent) {
      return layoutInflater.inflate(nothingSelectedLayout, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
      if (position == 0) {
        return nothingSelectedDropdownLayout == -1 ?
            new View(context) :
            getNothingSelectedDropdownView(parent);
      }
      return adapter.getDropDownView(position - EXTRA, null, parent);
    }

    protected View getNothingSelectedDropdownView(ViewGroup parent) {
      return layoutInflater.inflate(nothingSelectedDropdownLayout, parent, false);
    }

    @Override
    public int getCount() {
      int count = adapter.getCount();
      return count == 0 ? 0 : count + EXTRA;
    }

    @Override
    public Object getItem(int position) {
      return position == 0 ? null : adapter.getItem(position - EXTRA);
    }

    @Override
    public int getItemViewType(int position) {
      return 0;
    }

    @Override
    public int getViewTypeCount() {
      return 1;
    }

    @Override
    public long getItemId(int position) {
      return position >= EXTRA ? adapter.getItemId(position - EXTRA) : position - EXTRA;
    }

    @Override
    public boolean hasStableIds() {
      return adapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
      return adapter.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
      adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
      adapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
      return false;
    }

    @Override
    public boolean isEnabled(int position) {
      return position != 0; // Don't allow the 'nothing selected'
      // item to be picked.
    }

  }

}

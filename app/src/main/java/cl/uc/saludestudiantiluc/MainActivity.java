package cl.uc.saludestudiantiluc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cl.uc.saludestudiantiluc.ambiences.AmbiencesListFragment;
import cl.uc.saludestudiantiluc.breathingexcercises.BreathingExcercisesFragment;
import cl.uc.saludestudiantiluc.calendar.CalendarActivity;
import cl.uc.saludestudiantiluc.calendar.ScheduleActivity;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.evaluations.HomeEvaluation;
import cl.uc.saludestudiantiluc.exerciseplans.ExercisePlanMenu;
import cl.uc.saludestudiantiluc.imageries.ImageriesListFragment;
import cl.uc.saludestudiantiluc.sequences.SequencesListFragment;
import cl.uc.saludestudiantiluc.settings.SettingsActivity;
import cl.uc.saludestudiantiluc.sos.SosService;
import cl.uc.saludestudiantiluc.utils.ViewUtils;

public class MainActivity extends BaseActivity {

  public static final String TAG = MainActivity.class.getSimpleName();

  private static final double NAV_DRAWER_HEADER_HEIGHT_RATIO = 9.0 / 16.0;

  private boolean mSos;

  Intent mSosIntent;

  private NavigationView mNavigationView;
  private DrawerLayout mDrawerLayout;
  SharedPreferences.OnSharedPreferenceChangeListener listener;

  private int mCurrentFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mSos = false;
    mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
    loadMainBackground();
    setupNavigationDrawer();
    setupSettings();
    changeFragment(HomeFragment.newInstance());
    mCurrentFragment = R.id.drawer_home;
  }

  private void setupSettings(){
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);


    listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
      public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        // Implementation
        String a = key;
        if (key.equals("notification")) {

          if (!mSos) {
            mSos = true;
            mSosIntent = new Intent(MainActivity.this , SosService.class);
            startService(mSosIntent);

          } else {
            mSos = false;
            stopService(mSosIntent);
          }

        } else {
        }

      }
    };

    sharedPref.registerOnSharedPreferenceChangeListener(listener);


  }

  private void changeFragment(@NonNull Fragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commit();
  }

  private void setupNavigationDrawer() {
    final int navViewWidth = getNavViewWidth();
    setupNavDrawerHeader(navViewWidth);
    setNavViewWidth(navViewWidth);
    setupNavDrawer();
  }

  private void setupNavDrawerHeader(int navViewWidth) {
    // Per Material Design guidelines. For more information see
    // https://material.google.com/layout/metrics-keylines.html#metrics-keylines-ratio-keylines
    final int navDrawerHeaderHeight = (int) (navViewWidth * NAV_DRAWER_HEADER_HEIGHT_RATIO);

    RelativeLayout header = (RelativeLayout) mNavigationView.inflateHeaderView(
        R.layout.navigation_drawer_header);

    // Set user information
    TextView userEmailTextView = (TextView) header.findViewById(R.id.header_email);
    userEmailTextView.setText(getUserRepository().getUserEmail());
    TextView userNameTextView = (TextView) header.findViewById(R.id.header_name);
    userNameTextView.setText(getUserRepository().getName());

    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) header.getLayoutParams();
    params.height = navDrawerHeaderHeight;
    header.setLayoutParams(params);
  }

  private void setupNavDrawer() {
    mNavigationView.setBackgroundColor(
        getResources().getColor(R.color.transparent_black_background));
    mNavigationView.setNavigationItemSelectedListener(new NavigationView
        .OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        mCurrentFragment = item.getItemId();
        switch (item.getItemId()) {
          case R.id.drawer_home:
            changeFragment(HomeFragment.newInstance());
            break;
          case R.id.drawer_excercises:
            changeFragment(BreathingExcercisesFragment.newInstance());
            break;
          case R.id.drawer_sequences:
            changeFragment(SequencesListFragment.newInstance());
            break;
          case R.id.drawer_imaginery:
            changeFragment(ImageriesListFragment.newInstance());
            break;
          case R.id.drawer_ambience:
            changeFragment(AmbiencesListFragment.newInstance());
            break;
          case R.id.drawer_evaluations:
            startActivity(HomeEvaluation.getIntent(MainActivity.this));
            break;
          case R.id.drawer_exercise_plan:
            changeFragment(ExercisePlanMenu.newInstance());
            break;
          case R.id.drawer_request_appointment:
            startActivity(CalendarActivity.getIntent(MainActivity.this));
            break;
          case R.id.drawer_settings:
            startActivity(SettingsActivity.getIntent(MainActivity.this));
            break;
          case R.id.drawer_check_appointment:
            startActivity(ScheduleActivity.getIntent(MainActivity.this));
            break;
          case R.id.drawer_logout:
            logOut();
            break;
          default:
            mCurrentFragment = R.id.drawer_home;
            break;
        }
        mDrawerLayout.closeDrawers();
        return true;
      }
    });
    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
        getToolbar(), R.string.open_drawer, R.string.close_drawer) {

      @Override
      public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
      }
    };

    mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
    actionBarDrawerToggle.syncState();
  }

  private int setNavViewWidth(int navViewWidth) {
    DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mNavigationView
        .getLayoutParams();
    params.width = navViewWidth;
    return navViewWidth;
  }

  private int getNavViewWidth() {
    int screenWidth = ViewUtils.getScreenWidth(this);
    int actionBarHeight = getResources().getDimensionPixelSize(R.dimen.actionbar_height);
    int navViewWidth = screenWidth - actionBarHeight;
    int navDrawerMaxWidth = getResources().getDimensionPixelSize(R.dimen.nav_drawer_max_width);
    return Math.min(navViewWidth, navDrawerMaxWidth);
  }

  public static Intent getIntent(Activity callerActivity) {
    return new Intent(callerActivity, MainActivity.class);
  }
}

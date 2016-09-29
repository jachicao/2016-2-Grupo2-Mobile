package cl.uc.saludestudiantiluc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cl.uc.saludestudiantiluc.auth.AuthActivity;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.common.sounds.SoundSelectionFragment;
import cl.uc.saludestudiantiluc.profile.ProfileActivity;
import cl.uc.saludestudiantiluc.sequences.SequencesListFragment;
import cl.uc.saludestudiantiluc.squarebreathing.SquareBreathingActivity;
import cl.uc.saludestudiantiluc.utils.ViewUtils;

public class MainActivity extends BaseActivity {

  public static final String TAG = MainActivity.class.getSimpleName();

  private static final double NAV_DRAWER_HEADER_HEIGHT_RATIO = 9.0 / 16.0;

  private NavigationView mNavigationView;
  private DrawerLayout mDrawerLayout;
  private ImageView mBackgroundView;

  private int mCurrentFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mBackgroundView = (ImageView) findViewById(R.id.main_background_image);
    mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
    setupBackground();
    setupNavigationDrawer();
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, HomeFragment.newInstance())
        .commit();
    mCurrentFragment = R.id.drawer_home;
  }

  private void changeFragment(@NonNull Fragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commit();
  }

  private void setupBackground() {
    Glide
        .with(this)
        .load(R.drawable.sunset_background)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .centerCrop()
        .into(mBackgroundView);
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
    TextView userNameTextView = (TextView) header.findViewById(R.id.header_name);
    TextView userEmailTextView = (TextView) header.findViewById(R.id.header_email);
    userEmailTextView.setText(getUserRepository().getUserName());
    userNameTextView.setText(getUserRepository().getUserEmail());

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
            startActivity(SquareBreathingActivity.getIntent(MainActivity.this));
            break;
          case R.id.drawer_sequences:
            changeFragment(SequencesListFragment.newInstance());
            break;
          case R.id.drawer_imaginery:
            changeFragment(SoundSelectionFragment.newInstance(
                SoundSelectionFragment.IMAGERY_CONSTANT));
            break;
          case R.id.drawer_sounds:
            changeFragment(SoundSelectionFragment.newInstance(
                SoundSelectionFragment.AMBIENTAL_CONSTANT));
            break;
          case R.id.drawer_profile:
            startActivity(ProfileActivity.getIntent(MainActivity.this));
            break;
          case R.id.drawer_logout:
            getUserRepository().logOut();
            startActivity(AuthActivity.getIntent(MainActivity.this));
            finish();
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

package cl.uc.saludestudiantiluc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;

import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.common.sounds.SoundSelectionFragment;
import cl.uc.saludestudiantiluc.sequences.SequencesListFragment;
import cl.uc.saludestudiantiluc.squarebreathing.SquareBreathingActivity;
import cl.uc.saludestudiantiluc.utils.ViewUtils;

public class MainActivity extends BaseActivity {

  public static final String TAG = MainActivity.class.getSimpleName();

  private static final double NAV_DRAWER_HEADER_HEIGHT_RATIO = 9.0 / 16.0;


  private NavigationView mNavigationView;
  private DrawerLayout mDrawerLayout;
  private ImageView mBackgroundView;
  private ArrayList<String> mFragmentTags = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFragmentTags.add(HomeFragment.TAG);
    mFragmentTags.add(SequencesListFragment.TAG);
    mFragmentTags.add(SoundSelectionFragment.TAG);
    setContentView(R.layout.activity_main);
    mBackgroundView = (ImageView) findViewById(R.id.main_background_image);
    mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
    setupBackground();
    setupNavigationDrawer();
    Fragment previousFragment = null;
    String previousTag = "";
    if (savedInstanceState != null) {
      for(String tag : mFragmentTags) {
        previousFragment = getSupportFragmentManager().getFragment(savedInstanceState, tag);
        if (previousFragment != null) {
          previousTag = tag;
          break;
        }
      }
    }
    Fragment fragmentToOpen = previousFragment != null ? previousFragment : HomeFragment.newInstance();
    String tagToOpen = previousFragment != null ? previousTag : HomeFragment.TAG;
    changeFragment(fragmentToOpen, tagToOpen);
  }

  private void changeFragment(@NonNull Fragment fragment, String tag) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_container, fragment, tag)
        .commit();
  }

  private void setupBackground() {
    Glide
        .with(this)
        .load(R.drawable.norway)
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
        switch (item.getItemId()) {
          case R.id.drawer_home:
            changeFragment(HomeFragment.newInstance(), HomeFragment.TAG);
            break;
          case R.id.drawer_excercises:
            startActivity(SquareBreathingActivity.getIntent(MainActivity.this));
            break;
          case R.id.drawer_sequences:
            changeFragment(SequencesListFragment.newInstance(), SequencesListFragment.TAG);
            break;
          case R.id.drawer_imaginery:
            changeFragment(SoundSelectionFragment.newInstance(
                SoundSelectionFragment.IMAGERY_CONSTANT), SoundSelectionFragment.TAG);
            break;
          case R.id.drawer_sounds:
            changeFragment(SoundSelectionFragment.newInstance(
                SoundSelectionFragment.AMBIENTAL_CONSTANT), SoundSelectionFragment.TAG);
            break;
          default:
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

  @Override
  public void onBackPressed() {
    FragmentManager fManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fManager.beginTransaction();
    boolean bool = false;
    for(String tag : mFragmentTags) {
      Fragment fragment = fManager.findFragmentByTag(tag);
      if (fragment != null && fragment.isVisible()) {
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commitAllowingStateLoss();
        bool = true;
      }
    }
    if (!bool) {
      super.onBackPressed();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    FragmentManager fragmentManager = getSupportFragmentManager();
    for(String tag : mFragmentTags) {
      Fragment fragment = fragmentManager.findFragmentByTag(tag);
      if (fragment != null && fragment.isVisible()) {
        fragmentManager.putFragment(outState, tag, fragment);
      }
    }
  }
}

package cl.uc.saludestudiantiluc;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import cl.uc.saludestudiantiluc.auth.AuthFragment;
import cl.uc.saludestudiantiluc.auth.AuthListener;
import cl.uc.saludestudiantiluc.auth.DataResponse;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.common.BaseFragment;
import cl.uc.saludestudiantiluc.common.FragmentListener;
import cl.uc.saludestudiantiluc.design.BottomSheetGridMenu;
import cl.uc.saludestudiantiluc.design.BottomSheetItem;
import cl.uc.saludestudiantiluc.design.BottomSheetItemListener;
import cl.uc.saludestudiantiluc.squarebreathing.SquareBreathingActivity;
import cl.uc.saludestudiantiluc.utils.ViewUtils;

public class MainActivity extends BaseActivity implements AuthListener {

  private static final String TAG = MainActivity.class.getSimpleName();

  private static final double NAV_DRAWER_HEADER_HEIGHT_RATIO = 9.0 / 16.0;

  private static final int NAV_DRAWER_ALPHA = 200;
  private NavigationView mNavigationView;
  private DrawerLayout mDrawerLayout;
  private ImageView mBackgroundView;
  private FloatingActionButton mFloatingActionButton;
  private BottomSheetGridMenu mBottomSheetGridMenu;
  private ArrayList<BottomSheetItem> mBottomSheetItems = new ArrayList<>();
  private BaseFragment mCurrentFragment;
  private DataResponse mUserData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mBackgroundView = (ImageView) findViewById(R.id.main_background_image);
    mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
    mFloatingActionButton = (FloatingActionButton) findViewById(R.id.main_floating_action_button);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
    setupBackground();
    if (savedInstanceState != null) {
      mUserData = savedInstanceState.getParcelable("mUserData");
      mCurrentFragment = (BaseFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mCurrentFragment");
      if (mCurrentFragment != null) {
        mCurrentFragment.mListener = new FragmentListener() {
          @Override
          public void onDismissed() {
            mCurrentFragment = null;
          }
        };
      }
    }
    startAuth();
  }

  private void startAuth() {
    mNavigationView.setVisibility(View.GONE);
    mFloatingActionButton.setVisibility(View.GONE);
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    if (mUserData != null) {
      authCompleted();
    } else {
      setNewFragment(new AuthFragment());
    }
  }

  private void authCompleted() {
    setupNavigationDrawer();
    setupFloatingActionButton();
    setupBottomSheet();
  }

  private void setNewFragment(BaseFragment fragment) {
    if (mCurrentFragment != null) {
      mCurrentFragment.dismiss();
    }
    if (fragment != null) {
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.add(R.id.fragment_container, fragment);
      fragmentTransaction.commit();
      mCurrentFragment = fragment;
      mCurrentFragment.mListener = new FragmentListener() {
        @Override
        public void onDismissed() {
          mCurrentFragment = null;
        }
      };
    }
  }

  private void setupBottomSheet() {
    mBottomSheetItems = new ArrayList<>();
    mBottomSheetItems.add(new BottomSheetItem(R.drawable.ic_favorite_black_24dp, getString(R.string.main_menu_exercises), new BottomSheetItemListener() {
      @Override
      public void onClick(BottomSheetItem item) {
        setNewFragment(new SquareBreathingActivity());
      }
    }));
    mBottomSheetItems.add(new BottomSheetItem(R.drawable.ic_collections_black_24dp, getString(R.string.main_menu_sequences), new BottomSheetItemListener() {
      @Override
      public void onClick(BottomSheetItem item) {
        setNewFragment(null);
      }
    }));
    mBottomSheetItems.add(new BottomSheetItem(R.drawable.ic_audiotrack_black_24dp, getString(R.string.main_menu_ambient_sounds), new BottomSheetItemListener() {
      @Override
      public void onClick(BottomSheetItem item) {
        setNewFragment(null);
      }
    }));
    mBottomSheetItems.add(new BottomSheetItem(R.drawable.ic_movie_creation_black_24dp, getString(R.string.main_menu_imaginary), new BottomSheetItemListener() {
      @Override
      public void onClick(BottomSheetItem item) {
        setNewFragment(null);
      }
    }));
    mBottomSheetGridMenu = new BottomSheetGridMenu(this, mBottomSheetItems, getResources().getInteger(R.integer.bottom_sheet_grid_width));
    mBottomSheetGridMenu.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialog) {
        mFloatingActionButton.show();
      }
    });
  }
  private void setupFloatingActionButton() {
    mFloatingActionButton.setVisibility(View.VISIBLE);
    mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBottomSheetGridMenu.expandAndShow();
        mFloatingActionButton.hide();
      }
    });
  }

  private void setupBackground() {
    Glide
        .with(this)
        .load(R.drawable.beach_gif)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .centerCrop()
        .into(mBackgroundView);
  }

  private void setupNavigationDrawer() {
    mNavigationView.setVisibility(View.VISIBLE);
    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) header.getLayoutParams();
    params.height = navDrawerHeaderHeight;
    header.setLayoutParams(params);
  }

  private void setupNavDrawer() {
    mNavigationView.setNavigationItemSelectedListener(new NavigationView
        .OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
          case R.id.home:
            break;
          case R.id.drawer_exercises:
            //startActivity(SquareBreathingActivity.getIntent(MainActivity.this));
            break;
          case R.id.drawer_imaginary:
            break;
          case R.id.drawer_sequences:
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
    int actionBarHeight = getResources().getDimensionPixelSize(R.dimen.margin_from_toolbar);

    // Per Material Design guidelines. For more information, see
    // https://material.google.com/layout/metrics-keylines.html#metrics-keylines-keylines-spacing
    int navViewWidth = screenWidth - actionBarHeight;

    // Per Material Design guidelines. For more information, see
    // https://material.google.com/patterns/navigation-drawer.html#navigation-drawer-specs
    int navDrawerMaxWidth = getResources().getDimensionPixelSize(R.dimen.nav_drawer_max_width);

    return Math.min(navViewWidth, navDrawerMaxWidth);
  }
  @Override
  public void onBackPressed() {
    if (mCurrentFragment != null) {
      mCurrentFragment.onBackPressed();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    if (mCurrentFragment != null) {
      getSupportFragmentManager().putFragment(outState, "mCurrentFragment", mCurrentFragment);
    }
    if (mUserData != null) {
      outState.putParcelable("mUserData", mUserData);
    }
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mBottomSheetGridMenu != null) {
      mBottomSheetGridMenu.dismiss();
    }
  }

  @Override
  public void onSignedIn(DataResponse dataResponse) {
    mUserData = dataResponse;
    authCompleted();
  }
}

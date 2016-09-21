package cl.uc.saludestudiantiluc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cl.uc.saludestudiantiluc.utils.ViewUtils;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private static final double NAV_DRAWER_HEADER_HEIGHT_RATIO = 9.0 / 16.0;

  private Toolbar mToolbar;
  private NavigationView mNavigationView;
  private DrawerLayout mDrawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);

    mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
    final int navViewWidth = getNavViewWidth();
    setupNavDrawerHeader(navViewWidth);
    setNavViewWidth(navViewWidth);
    setupNavDrawer();

    /*getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, ChatsFragment.newInstance())
        .commit();*/
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

    /*for (int i = 0; i < header.getChildCount(); i++) {
      View child = header.getChildAt(i);
      if (child.getId() == R.id.header_profile_image) {
        RelativeLayout.LayoutParams childParams = (RelativeLayout.LayoutParams) child
            .getLayoutParams();
        childParams.topMargin = childParams.topMargin + statusBarHeightPx;
        child.setLayoutParams(childParams);
      }
    }*/
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
          case R.id.drawer_ambient_sounds:
            break;
          case R.id.drawer_imaginary:
            break;
          default:
            break;
        }
        mDrawerLayout.closeDrawers();
        return true;
      }
    });

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
        mToolbar, R.string.open_drawer, R.string.close_drawer) {

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

    // Per Material Design guidelines. For more information, see
    // https://material.google.com/layout/metrics-keylines.html#metrics-keylines-keylines-spacing
    int navViewWidth = screenWidth - actionBarHeight;

    // Per Material Design guidelines. For more information, see
    // https://material.google.com/patterns/navigation-drawer.html#navigation-drawer-specs
    int navDrawerMaxWidth = getResources().getDimensionPixelSize(R.dimen.nav_drawer_max_width);

    return Math.min(navViewWidth, navDrawerMaxWidth);
  }
}

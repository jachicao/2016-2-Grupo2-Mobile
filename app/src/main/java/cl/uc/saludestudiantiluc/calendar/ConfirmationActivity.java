package cl.uc.saludestudiantiluc.calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;

public class ConfirmationActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_confirmation);
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

    loadMainBackground();
  }
}

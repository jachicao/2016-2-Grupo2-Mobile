package cl.uc.saludestudiantiluc.exerciseplans;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.calendar.CalendarActivity;
import cl.uc.saludestudiantiluc.common.BaseActivity;

public class ExercisePlanMenu extends BaseActivity {

  private RecyclerView mRecyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exercise_plan_menu);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.main_menu_exercise_plans);
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


    ArrayList<ParentObject> parentObjects = new ArrayList<>();

    ArrayList<Object> childList1 = new ArrayList<>();
    childList1.add(new ExerciseChild("Día 1",true));
    childList1.add(new ExerciseChild("Día 2",false));
    childList1.add(new ExerciseChild("Día 3",false));
    childList1.add(new ExerciseChild("Día 4",false));
    childList1.add(new ExerciseChild("Día 5",false));
    ExercisePlan e1 = new ExercisePlan("Plan de 5 días", childList1);
    parentObjects.add(e1);

    ArrayList<Object> childList2 = new ArrayList<>();
    childList2.add(new ExerciseChild("10 días",true));
    ExercisePlan e2 = new ExercisePlan("Plan de 10 días", childList2);
    parentObjects.add(e2);

    ExerciseExpandableAdapter exerciseExpandableAdapter = new ExerciseExpandableAdapter(this,
        parentObjects);
    exerciseExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
    exerciseExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
    exerciseExpandableAdapter.setParentAndIconExpandOnClick(true);

    mRecyclerView = (RecyclerView) findViewById(R.id.exercise_recycler_view);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setAdapter(exerciseExpandableAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, ExercisePlanMenu.class);
  }
}


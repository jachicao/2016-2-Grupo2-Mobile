package cl.uc.saludestudiantiluc.evaluations;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.evaluations.data.evaluationModel;
import cl.uc.saludestudiantiluc.evaluations.data.generateEvaluation;

public class BaseEvaluationActivity extends AppCompatActivity {

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  private SectionsPagerAdapter mSectionsPagerAdapter;
  protected evaluationModel mEvaluation;
  private int mQuantity;
  private String[] mResults;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private ViewPager mViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_evaluation);

    TextView toolbar = (TextView) findViewById(R.id.toolbar);
    //setSupportActionBar(toolbar);

    //Set the evaluation model
    generateEvaluation geval = new generateEvaluation() ;
    InputStream file = getResources().openRawResource(R.raw.t1);
    String str = geval.getStringFromJson(file);
    mEvaluation = geval.getEvaluation(str);
    String evaluationName = mEvaluation.evaluationName;
    int numberOptions = mEvaluation.numberOptions;
    mQuantity = mEvaluation.quantity;
    List<String> questions = new ArrayList<String>();
    questions.addAll(mEvaluation.questions);
    mResults = new String[mQuantity];

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mQuantity, questions);

    // Set up the ViewPager with the sections adapter.
    mViewPager = (ViewPager) findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);



    //Set the toolbar
    toolbar.setText(evaluationName);
    toolbar.setTextSize(20);
    toolbar.setTextColor(getResources().getColor(R.color.black));

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        publicar(view);
      }
    });

  }

  public void setResults(int i, String result) {
    mResults[i] = result;
  }

  public boolean areAllSelected() {
    for (int i = 0; i < mQuantity; i++) {
      if (mResults[i] == null) {
        return false;
      }
    }
    return true;
  }


  public void publicar(View view) {

    if (areAllSelected()) {
      int suma = 0;
      for (int i = 0; i<mResults.length ; i++) {
        suma += Integer.parseInt(mResults[i]);
      }
      Snackbar.make(view, "Haz Sumado " + suma, Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
    } else {
      Snackbar.make(view, "faltan algunos... ", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
    }


  }






  public static Intent getIntent(Activity activity) {
    return new Intent(activity, BaseEvaluationActivity.class);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_base_evaluation, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }




  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private int mQuantity;
    private List<String> mQuestions = new ArrayList<String>();

    public SectionsPagerAdapter(FragmentManager fm, int quantity, List<String> questions) {
      super(fm);
      mQuestions.addAll(questions);
      mQuantity = quantity;
    }

    @Override
    public Fragment getItem(int position) {
      // getItem is called to instantiate the fragment for the given page.
      // Return a PlaceholderFragment (defined as a static inner class below).
      PlaceholderFragment pc = PlaceholderFragment.newInstance(position + 1, mQuestions.get(position) );
      return pc;
    }


    @Override
    public int getCount() {
      // Show 3 total pages.
      return mQuantity;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "SECTION 1";
        case 1:
          return "SECTION 2";
        case 2:
          return "SECTION 3";
        case 3:
          return "SECTION 3";
        case 4:
          return "SECTION 4";
        case 5:
          return "SECTION 5";
        case 6:
          return "SECTION 6";
        case 7:
          return "SECTION 7";
        case 8:
          return "SECTION 8";
        case 9:
          return "SECTION 9";
        case 10:
          return "SECTION 10";
        case 11:
          return "SECTION 11";
        case 12:
          return "SECTION 12";
        case 13:
          return "SECTION 13";
        case 14:
          return "SECTION 14";
        case 15:
          return "SECTION 15";
        case 16:
          return "SECTION 16";
        case 17:
          return "SECTION 17";
        case 18:
          return "SECTION 18";
        case 19:
          return "SECTION 19";
        case 20:
          return "SECTION 20";
      }
      return null;
    }
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_QUESTION = "question";
    private int mQuestionNumber = 0;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, String question) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();


      args.putInt(ARG_SECTION_NUMBER, sectionNumber);

      args.putString(ARG_QUESTION, question);
      fragment.setArguments(args);
      return fragment;
    }


    public PlaceholderFragment() {
    }


    public String getSelection() {
      RadioGroup radiogroup = (RadioGroup) this.getView().findViewById(R.id.radioGroupOptions);
      int selectedId = radiogroup.getCheckedRadioButtonId();
      RadioButton radiobutton = (RadioButton) getView().findViewById(selectedId);
      String tag = radiobutton.getTag().toString();
      return tag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_base_evaluation, container, false);
      TextView textView = (TextView) rootView.findViewById(R.id.section_label);
      textView.setText(getArguments().getString(ARG_QUESTION));
      mQuestionNumber = getArguments().getInt((ARG_SECTION_NUMBER));

      RadioGroup radiogroup = (RadioGroup) rootView.findViewById(R.id.radioGroupOptions);
      radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {

          ((BaseEvaluationActivity) getActivity()).setResults(mQuestionNumber-1, getSelection());
          }
        });

      return rootView;
    }
  }
}

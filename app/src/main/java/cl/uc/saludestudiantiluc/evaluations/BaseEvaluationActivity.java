package cl.uc.saludestudiantiluc.evaluations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.evaluations.data.evaluationModel;
import cl.uc.saludestudiantiluc.evaluations.data.generateEvaluation;
import cl.uc.saludestudiantiluc.evaluations.models.EvaluationModel;
import me.relex.circleindicator.CircleIndicator;

public class BaseEvaluationActivity extends BaseActivity {

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
  private FloatingActionButton mButtonFloat;

  private LayoutInflater mInflater;
  private int mEvaluationType;
  public static final String TOTAL_SCORE = "com.example.relaxuc.evaluations.score";
  public static final String EVALUATION_TYPE = "com.example.relaxuc.evaluations.type";
  public static final String USER_ROLE = "com.example.relaxuc.evaluations.role";

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private ViewPager mViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_evaluation);
    TextView questionTitle = (TextView) findViewById(R.id.title_question) ;

    Intent intent = getIntent();
    mEvaluationType = intent.getIntExtra(HomeEvaluation.TEST_TYPE, 0) ;

    //setToolBar
    String[] types_array = getResources().getStringArray(R.array.evaluations_types);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(types_array[mEvaluationType-1]);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
    getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });

    //Set background
    loadMainBackground();

    mInflater = (LayoutInflater) this
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    //Set the evaluation model
    generateEvaluation geval = new generateEvaluation() ;
    InputStream file;
    if (mEvaluationType == EvaluationModel.EVALUATION_TYPE_STRESS) {
      file = getResources().openRawResource(R.raw.te);
    } else if (mEvaluationType == EvaluationModel.EVALUATION_TYPE_GAD7) {
      file = getResources().openRawResource(R.raw.ta);
    } else {
      file = getResources().openRawResource(R.raw.ts);
    }

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
    mButtonFloat = (FloatingActionButton) findViewById(R.id.fab);

    mButtonFloat.setVisibility(View.INVISIBLE);

    CircleIndicator indicator =
        (CircleIndicator) findViewById(R.id.evaluation_circle_indicator);
    // Set up the ViewPager with the sections adapter.
    mViewPager = (ViewPager) findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      public void onPageScrollStateChanged(int state) {
        if (mViewPager.getCurrentItem() == (mQuantity - 1 ) ) {
          mButtonFloat.setVisibility(View.VISIBLE);
        }
      }
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

      public void onPageSelected(int position) {
        // Check if this is the page you want.
      }
    });

    indicator.setViewPager(mViewPager);

    //Set the questionTitle
    questionTitle.setText(evaluationName);
    questionTitle.setTextSize(20);
    questionTitle.setTextColor(getResources().getColor(R.color.white));

    FloatingActionButton send = (FloatingActionButton) findViewById(R.id.fab);
    send.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
    send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        sendScore(view);
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

  public int getNotSelected() {
    int number = mResults.length - 1;
    for (int i = 0; i < mResults.length ; i++) {
      if (mResults[i] == null) {
        number = i;
      }
    }
    return number;
  }

  public void sendScore(View view) {

    if (areAllSelected()) {
      showResults();
    } else {
      mViewPager.setCurrentItem(getNotSelected() );
      Snackbar.make(view, "Debe responder todas las preguntas ", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
    }
  }

  public void showResults() {

    int score;
    score = calculateScore();
    Intent intent = new Intent(this, EvaluationResults.class);
    intent.putExtra(TOTAL_SCORE, score);
    intent.putExtra(EVALUATION_TYPE, mEvaluationType);

    String role = getUserRepository().getAcademicType();

    String[] academicTypes = getResources().getStringArray(R.array
        .auth_register_academic_type_array);
    if (role.equals(academicTypes[1])){
      intent.putExtra(USER_ROLE, 1);
    } else if (role.equals(academicTypes[2])){
      intent.putExtra(USER_ROLE, 2);
    } else {
      intent.putExtra(USER_ROLE, 1);
    }


    startActivity(intent);

  }

  public int calculateScore() {
    int score = 0;
    if (mEvaluationType == EvaluationModel.EVALUATION_TYPE_SLEEP) {
      int i = 0;
      double realTime = Double.parseDouble(mResults[0]);
      double idealTime = Double.parseDouble(mResults[1]);

      if ((realTime - idealTime) > 1.5 || (realTime - idealTime) < -1.5 ) {
        score += 1;
      } else {
        score += 0;
      }

      int sleepingPerception = Integer.parseInt(mResults[2]);
      if (sleepingPerception == 1) {
        score += 0;
      } else {
        score += 1;
      }

      int delay = Integer.parseInt(mResults[3]);
      if (delay == 3) {
        score += 1;
      } else {
        score += 0;
      }

      int timesWapeUp = Integer.parseInt(mResults[4]);
      int timeToSleep = Integer.parseInt(mResults[5]);
      if ((timesWapeUp == 4) || (timesWapeUp == 3 && timeToSleep == 4) || (timesWapeUp == 2 &&
          timeToSleep == 4 )) {
        score += 1;
      } else {
        score += 0;
      }

      int awake = Integer.parseInt(mResults[6]);
      if (awake <= 2) {
        score += 1;
      } else {
        score += 0;
      }

      int symptom = Integer.parseInt(mResults[7]);
      if (symptom >= 2) {
        score += 1;
      } else {
        score += 0;
      }

    } else {
      for (int i = 0; i < mResults.length; i++) {

        if (mEvaluationType == EvaluationModel.EVALUATION_TYPE_STRESS) { // If evaluation is for stress recomenation
          if (i == 3 || i == 4 || i == 5 || i == 6 || i == 8 || i == 9 || i == 12) {
            score += 4 - (Integer.parseInt(mResults[i]) - 1);
          } else {
            score += Integer.parseInt(mResults[i]) - 1;
          }
        }

        if (mEvaluationType == EvaluationModel.EVALUATION_TYPE_GAD7) {
          score += Integer.parseInt((mResults[i])) - 1;
        }

      }
    }
    return score;
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
      if (((BaseEvaluationActivity) getActivity()).mEvaluationType == EvaluationModel.EVALUATION_TYPE_SLEEP) {
        if(mQuestionNumber > 2) {
          RadioGroup radiogroup = (RadioGroup) this.getView().findViewById(R.id.radioGroupOptions);
          int selectedId = radiogroup.getCheckedRadioButtonId();
          RadioButton radiobutton = (RadioButton) getView().findViewById(selectedId);
          if(selectedId >= 0) {
            String tag = radiobutton.getTag().toString();
            return tag;
          } else {
            return "";
          }

        } else {
          EditText totalHours = (EditText) getView().findViewById(R.id.totalHours);
          String total = totalHours.getEditableText().toString();
          return total;

        }
      } else {
        RadioGroup radiogroup = (RadioGroup) this.getView().findViewById(R.id.radioGroupOptions);
        int selectedId = radiogroup.getCheckedRadioButtonId();
        RadioButton radiobutton = (RadioButton) getView().findViewById(selectedId);
        String tag = null;
        if(radiobutton != null) {
          tag = radiobutton.getTag().toString();

        }
        return tag;
      }
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

      EditText editText = (EditText) rootView.findViewById(R.id.totalHours);
      editText.addTextChangedListener(new TextWatcher() {
        public void afterTextChanged(Editable s) {
          ((BaseEvaluationActivity) getActivity()).setResults(mQuestionNumber-1, getSelection());
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
        public void onTextChanged(CharSequence s, int start, int before, int count){}
      });

      setUi(rootView, mQuestionNumber);

      return rootView;
    }

    public void setUi(View rootView, int questionNumber) {
      EditText totalHours = (EditText) rootView.findViewById(R.id.totalHours);
      totalHours.setVisibility(View.INVISIBLE);
     totalHours.setHeight(0);
      if (((BaseEvaluationActivity) getActivity()).mEvaluationType == 2) {

        View total = rootView.findViewById(R.id.totalHours);
        ((ViewGroup) total.getParent()).removeView(total);

        RadioButton radioButton5 = (RadioButton) rootView.findViewById(R.id.radioButton5);
        radioButton5.setVisibility(View.INVISIBLE);

        RadioButton radioButton1 = (RadioButton) rootView.findViewById(R.id.radioButton);
        radioButton1.setText(getResources().getString(R.string.answer_T2_a));

        RadioButton radioButton2 = (RadioButton) rootView.findViewById(R.id.radioButton2);
        radioButton2.setText(getResources().getString(R.string.answer_T2_b));

        RadioButton radioButton3 = (RadioButton) rootView.findViewById(R.id.radioButton3);
        radioButton3.setText(getResources().getString(R.string.answer_T2_c));

        RadioButton radioButton4 = (RadioButton) rootView.findViewById(R.id.radioButton4);
        radioButton4.setText(getResources().getString(R.string.answer_T2_d));

      } else if (((BaseEvaluationActivity) getActivity()).mEvaluationType == EvaluationModel.EVALUATION_TYPE_STRESS) {
        View total = rootView.findViewById(R.id.totalHours);
        ((ViewGroup) total.getParent()).removeView(total);

        RadioButton radioButton1 = (RadioButton) rootView.findViewById(R.id.radioButton);
        radioButton1.setText(getResources().getString(R.string.answer_T1_a));

        RadioButton radioButton2 = (RadioButton) rootView.findViewById(R.id.radioButton2);
        radioButton2.setText(getResources().getString(R.string.answer_T1_b));

        RadioButton radioButton3 = (RadioButton) rootView.findViewById(R.id.radioButton3);
        radioButton3.setText(getResources().getString(R.string.answer_T1_c));

        RadioButton radioButton4 = (RadioButton) rootView.findViewById(R.id.radioButton4);
        radioButton4.setText(getResources().getString(R.string.answer_T1_d));

        RadioButton radioButton5 = (RadioButton) rootView.findViewById(R.id.radioButton5);
        radioButton5.setText(getResources().getString(R.string.answer_T1_e));


      } else if (((BaseEvaluationActivity) getActivity()).mEvaluationType == EvaluationModel.EVALUATION_TYPE_SLEEP) {

        if(questionNumber == 1 || questionNumber==2) {
          totalHours.setHeight(230);

          RadioButton radioButton1 = (RadioButton) rootView.findViewById(R.id.radioButton);
          radioButton1.setVisibility(View.INVISIBLE);

          RadioButton radioButton2 = (RadioButton) rootView.findViewById(R.id.radioButton2);
          radioButton2.setVisibility(View.INVISIBLE);

          RadioButton radioButton3 = (RadioButton) rootView.findViewById(R.id.radioButton3);
          radioButton3.setVisibility(View.INVISIBLE);

          RadioButton radioButton4 = (RadioButton) rootView.findViewById(R.id.radioButton4);
          radioButton4.setVisibility(View.INVISIBLE);

          RadioButton radioButton5 = (RadioButton) rootView.findViewById(R.id.radioButton5);
          radioButton5.setVisibility(View.INVISIBLE);

          totalHours.setVisibility(View.VISIBLE);
        } else if (mQuestionNumber != 5 && mQuestionNumber != 6) {
          String option1 = "";
          String option2 = "";
          String option3 = "";
          if (mQuestionNumber == 3) {
            option1 = "Bueno";
            option2 = "Regular";
            option3 = "Malo";
          } else if (mQuestionNumber == 4) {
            option1 = "Menos de 5 minutos";
            option2 = "entre 5 y 30 minutos";
            option3 = "Más de 30 minutos";
          } else if (mQuestionNumber == 7) {
            option1 = "Cansado(a)";
            option2 = "Más o menos";
            option3 = "Descansado(a)";
          } else if (mQuestionNumber == 8) {
            option1 = "Ninguno";
            option2 = "Solo uno";
            option3 = "Dos o más";
          }


          RadioButton radioButton1 = (RadioButton) rootView.findViewById(R.id.radioButton);
          radioButton1.setText(option1);

          RadioButton radioButton2 = (RadioButton) rootView.findViewById(R.id.radioButton2);
          radioButton2.setText(option2);

          RadioButton radioButton3 = (RadioButton) rootView.findViewById(R.id.radioButton3);
          radioButton3.setText(option3);

          RadioButton radioButton4 = (RadioButton) rootView.findViewById(R.id.radioButton4);
          radioButton4.setVisibility(View.INVISIBLE);

          RadioButton radioButton5 = (RadioButton) rootView.findViewById(R.id.radioButton5);
          radioButton5.setVisibility(View.INVISIBLE);

        } else if (mQuestionNumber == 5 || mQuestionNumber == 6) {

          String option1 = "Ninguna vez";
          String option2 = "Una vez";
          String option3 = "Dos veces";
          String option4 = " Tres o más veces";

          if (mQuestionNumber == 6) {
            option1 = "No despierto";
            option2 = "Menos de 5 minutos";
            option3 = "entre 5 y 30 minutos";
            option4 = "Más de 30 minutos";
          }

          RadioButton radioButton1 = (RadioButton) rootView.findViewById(R.id.radioButton);
          radioButton1.setText(option1);

          RadioButton radioButton2 = (RadioButton) rootView.findViewById(R.id.radioButton2);
          radioButton2.setText(option2);

          RadioButton radioButton3 = (RadioButton) rootView.findViewById(R.id.radioButton3);
          radioButton3.setText(option3);

          RadioButton radioButton4 = (RadioButton) rootView.findViewById(R.id.radioButton4);
          radioButton4.setText(option4);

          RadioButton radioButton5 = (RadioButton) rootView.findViewById(R.id.radioButton5);
          radioButton5.setVisibility(View.INVISIBLE);

        }
      }
    }
  }
}

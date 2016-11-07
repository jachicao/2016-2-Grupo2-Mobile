package cl.uc.saludestudiantiluc.exerciseplans;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

/**
 * Created by camilo on 06-11-16.
 */

public class ExercisePlan implements ParentObject {

  private List<Object> mChildrenList;
  private String mTitle;

  public ExercisePlan(String s, List<Object> list){
    mTitle = s;
    mChildrenList = list;
  }

  @Override
  public List<Object> getChildObjectList() {
    return mChildrenList;
  }

  public String getTitle() {
    return mTitle;
  }

  @Override
  public void setChildObjectList(List<Object> list) {
    mChildrenList = list;
  }
}

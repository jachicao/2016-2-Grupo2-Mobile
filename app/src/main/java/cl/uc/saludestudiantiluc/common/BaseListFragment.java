package cl.uc.saludestudiantiluc.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.models.BaseFragmentListModel;

/**
 * Created by jchicao on 10/20/16.
 */

public class BaseListFragment extends BaseFragment {

  private View mThisView;
  private BaseListAdapter mAdapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mThisView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
    RecyclerView recyclerView = (RecyclerView) mThisView.findViewById(R.id.fragment_recycler_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setAdapter(mAdapter);
    return mThisView;
  }

  public void setAdapter(BaseListAdapter adapter) {
    mAdapter = adapter;
  }

  public BaseListAdapter getAdapter() {
    return mAdapter;
  }

  public List<BaseFragmentListModel> getModelList() {
    return new ArrayList<>();
  }
}

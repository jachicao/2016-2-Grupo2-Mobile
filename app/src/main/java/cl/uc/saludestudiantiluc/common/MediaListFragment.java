package cl.uc.saludestudiantiluc.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.models.Media;

/**
 * Created by jchicao on 10/20/16.
 */

public class MediaListFragment extends BaseFragment {

  private MediaListAdapter mAdapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.media_recycler_view, container, false);
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recycler_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setAdapter(mAdapter);
    return view;
  }

  public void setAdapter(MediaListAdapter adapter) {
    mAdapter = adapter;
  }

  public MediaListAdapter getAdapter() {
    return mAdapter;
  }

  public List<Media> getModelList() {
    return new ArrayList<>();
  }
}

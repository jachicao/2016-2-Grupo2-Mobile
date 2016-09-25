package cl.uc.saludestudiantiluc.design;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by jchicao on 9/24/16.
 */

public class BottomSheetGridMenu extends BottomSheetDialog {

  private int mGridWidth;
  private ArrayList<BottomSheetItem> mBottomSheetItems;
  private BottomSheetBehavior mBehavior;
  public BottomSheetGridMenu(@NonNull Context context, ArrayList<BottomSheetItem> list, int gridWidth) {
    super(context);
    mGridWidth = gridWidth;
    mBottomSheetItems = list;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      Log.v("MENU", "NOT NULL");
    }
    View view = getLayoutInflater().inflate(R.layout.main_bottom_sheet_dialog, null);
    setContentView(view);
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_bottom_sheet_dialog_recycler_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mGridWidth));
    BottomSheetItemAdapter adapter = new BottomSheetItemAdapter(mBottomSheetItems, new BottomSheetItemListener() {
      @Override
      public void onClick(BottomSheetItem item) {
        dismiss();
      }
    });

    mBehavior = BottomSheetBehavior.from((View)view.getParent());
    mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {
        switch (newState) {
          case BottomSheetBehavior.STATE_HIDDEN:
            Log.v("Behavior", "HIDDEN");
            dismiss();
            break;
          case BottomSheetBehavior.STATE_EXPANDED:
            Log.v("Behavior", "EXPANDED");
            break;
          case BottomSheetBehavior.STATE_COLLAPSED:
            Log.v("Behavior", "COLLAPSED");
            break;
          case BottomSheetBehavior.STATE_DRAGGING:
            Log.v("Behavior", "DRAGGING");
            break;
          case BottomSheetBehavior.STATE_SETTLING:
            Log.v("Behavior", "SETTLING");
            break;
        }
      }

      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {

      }
    });

    recyclerView.setAdapter(adapter);
  }

  public void expandAndShow() {
    if (mBehavior != null) {
      mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    show();
  }

}


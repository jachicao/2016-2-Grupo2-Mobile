package cl.uc.saludestudiantiluc.sequences;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.auth.AuthActivity;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.sequences.models.SequencesImage;
import cl.uc.saludestudiantiluc.utils.TouchDetector;
import cl.uc.saludestudiantiluc.utils.TouchListener;

public class ImageFragment extends Fragment {

  private static final String SEQUENCE_IMAGE_EXTRAS = "SequencesImage";

  public static ImageFragment newInstance(SequencesImage sequencesImage) {
    ImageFragment fragment = new ImageFragment();
    Bundle args = new Bundle();
    args.putParcelable(SEQUENCE_IMAGE_EXTRAS, sequencesImage);
    fragment.setArguments(args);
    return fragment;
  }

  private BaseActivity getThisActivity() {
    return (BaseActivity) getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup) inflater.inflate(
        R.layout.sequences_image_fragment, container, false);
    TouchDetector.register(rootView, new TouchListener() {
      @Override
      public void onTouch(int type) {
        switch (type) {
          case TouchDetector.ON_SINGLE_TAP_CONFIRMED:
            getThisActivity().onSingleTap();
            break;
        }
      }
    });
    SequencesImage sequenceImage = getArguments().getParcelable(SEQUENCE_IMAGE_EXTRAS);
    if (sequenceImage != null) {
      ImageView imageView = (ImageView) rootView.findViewById(R.id.sequences_image_fragment_view);
      View frameText = rootView.findViewById(R.id.sequences_image_fragment_text_frame);
      ImagesActivity activity = (ImagesActivity) getActivity();
      if (activity != null) {
        activity.getDownloadService().requestIntoImageView(getContext(), imageView, sequenceImage.getImageRequest());
      }

      String description = sequenceImage.getDescription();
      if (description != null && !TextUtils.isEmpty(description)) {
        frameText.setVisibility(View.VISIBLE);
        TextView textView = (TextView) rootView.findViewById(R.id.sequences_image_fragment_text);
        textView.setText(description);
      } else {
        frameText.setVisibility(View.GONE);
      }
    }
    return rootView;
  }
}

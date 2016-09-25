package cl.uc.saludestudiantiluc.sequences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cl.uc.saludestudiantiluc.R;

public class ImageFragment extends Fragment {
  private SequencesImage mSequencesImage;
  private ImageView mImageView;
  public static ImageFragment newInstance(SequencesImage sequencesImage) {
    ImageFragment fragment = new ImageFragment();
    Bundle args = new Bundle();
    args.putParcelable("SequencesImage", sequencesImage);
    fragment.setArguments(args);
    return fragment;
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup) inflater.inflate(
        R.layout.sequences_fragment_image, container, false);
    mImageView = (ImageView) rootView.findViewById(R.id.sequenceFragmentImageView);
    if (mImageView != null) {
      mSequencesImage = getArguments().getParcelable("SequencesImage");
      if (mSequencesImage != null) {
        mSequencesImage.loadImage(mImageView);
      }
    }
    return rootView;
  }
}

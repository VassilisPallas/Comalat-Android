package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sakaiproject.customviews.rich_textview.RichTextView;

/**
 * Created by vspallas on 20/02/16.
 */
public class TrainingFragment extends Fragment {

    public TrainingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_training, container, false);

        getActivity().setTitle(getContext().getResources().getString(R.string.training));

        RichTextView trainingMessage = (RichTextView) v.findViewById(R.id.training_message);
        trainingMessage.setContext(getContext());

        return v;
    }
}

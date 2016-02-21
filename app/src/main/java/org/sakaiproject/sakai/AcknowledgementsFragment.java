package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sakaiproject.customviews.rich_textview.RichTextView;

/**
 * Created by vspallas on 20/02/16.
 */
public class AcknowledgementsFragment extends Fragment {

    public AcknowledgementsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_acknowledgements, container, false);

        getActivity().setTitle(getContext().getResources().getString(R.string.acknowledgements));

        RichTextView acknowledgementsMessage = (RichTextView) v.findViewById(R.id.acknowledgements_message);
        acknowledgementsMessage.setContext(getContext());
        acknowledgementsMessage.setText(getContext().getResources().getString(R.string.acknowledgements_message));


        RichTextView acknowledgementsCopyright = (RichTextView) v.findViewById(R.id.acknowledgements_copyright);
        acknowledgementsCopyright.setContext(getContext());

        return v;
    }
}

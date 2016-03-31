package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vspallas on 20/02/16.
 */
public class AboutFragment extends Fragment {

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        getActivity().setTitle(getContext().getResources().getString(R.string.about));

        findViewsById(v);

        return v;
    }

    private void findViewsById(View v) {
        org.sakaiproject.customviews.rich_textview.RichTextView about = (org.sakaiproject.customviews.rich_textview.RichTextView) v.findViewById(R.id.about_message);
        about.setContext(getContext());
    }
}

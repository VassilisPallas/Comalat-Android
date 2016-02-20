package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vspallas on 20/02/16.
 */
public class FeaturesFragment extends Fragment {

    public FeaturesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_features, container, false);

        getActivity().setTitle(getContext().getResources().getString(R.string.features));

        return v;
    }

}

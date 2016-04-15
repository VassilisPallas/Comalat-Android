package org.sakaiproject.sakai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.api.user.profile.Profile;

import java.io.File;
import java.io.FileNotFoundException;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private CustomSwipeRefreshLayout swipeRefreshLayout;
    private ImageView profileImage;
    private TextView username, position;
    private Button about, friends;

    public ProfileFragment() {
    }

    /**
     * get the swipe refresh layout from activity
     *
     * @param swipeRefreshLayout the layout
     * @return the fragment with the data
     */
    public ProfileFragment getSwipeRefreshLayout(CustomSwipeRefreshLayout swipeRefreshLayout) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        profileFragment.setArguments(b);
        return profileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initialize(v);

        return v;
    }

    private void initialize(View v) {
        getActivity().setTitle(Profile.getDisplayName());

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");
        swipeRefreshLayout.setEnabled(false);

        profileImage = (ImageView) v.findViewById(R.id.profile_image);

        try {
            if (ActionsHelper.createDirIfNotExists(getContext(), User.getUserEid() + File.separator + "user"))
                profileImage.setImageBitmap(ActionsHelper.getImage(getContext(), "user_image", User.getUserEid() + File.separator + "user"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        username = (TextView) v.findViewById(R.id.username);
        username.setText(Profile.getDisplayName());

        position = (TextView) v.findViewById(R.id.position);

        if ((Profile.getPosition() != null && !Profile.getPosition().equals("")) && (Profile.getDepartment() != null && !Profile.getDepartment().equals(""))) {
            String pos = Profile.getPosition() + " " + getString(R.string.at) + " " + Profile.getDepartment();
            if (pos.length() > 65) {
                pos = pos.substring(0, 63) + "...";
            }
            position.setText(pos);
        }

        about = (Button) v.findViewById(R.id.about_btn);
        about.setOnClickListener(this);
        friends = (Button) v.findViewById(R.id.friends_btn);
        friends.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_btn:
                startActivity(new Intent(getActivity(), UserAboutActivity.class));
                break;
            case R.id.friends_btn:
                break;
        }
    }
}

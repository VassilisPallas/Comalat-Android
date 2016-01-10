package org.sakaiproject.sakai;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.customviews.ProfileScrollView;

import java.io.FileNotFoundException;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ImageView profileImage;
    private ProfileScrollView profileScrollview;
    private EditText saySomethingEditText, newPostEditText;
    private Button saySomethingButton, newPostButton;
    private TextView saySomethingCount;
    private LinearLayout about, photos, friends;
    /////////////////////////////////////////////////////////////////////
    private LinearLayout newSubcomment;
    private FrameLayout comment, deletePost;
    private LinearLayout subcomment;
    ///////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * get the swipe refresh layout from activity
     *
     * @param swipeRefreshLayout the layout
     * @return the fragment with the data
     */
    public ProfileFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
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
        getActivity().setTitle(Profile.getDisplayName());
        findViewsById(v);
        return v;
    }

    private void findViewsById(View v) {
        profileImage = (ImageView) v.findViewById(R.id.profile_image);

        try {
            profileImage.setImageBitmap(Actions.getImage(getContext(), "user_image"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        profileScrollview = (ProfileScrollView) v.findViewById(R.id.profile_scrollview);
        profileScrollview.setSwipeRefreshLayout((org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh"));

        saySomethingCount = (TextView) v.findViewById(R.id.say_something_count);

        saySomethingEditText = (EditText) v.findViewById(R.id.say_something_EditText);
        saySomethingEditText.addTextChangedListener(saySomethingWatcher);

        saySomethingButton = (Button) v.findViewById(R.id.say_something_button);
        saySomethingButton.setOnClickListener(this);

        about = (LinearLayout) v.findViewById(R.id.about_button);
        about.setOnClickListener(this);

        photos = (LinearLayout) v.findViewById(R.id.photos_button);
        photos.setOnClickListener(this);

        friends = (LinearLayout) v.findViewById(R.id.friends_button);
        friends.setOnClickListener(this);

        newPostEditText = (EditText)v.findViewById(R.id.new_post_EditText);

        newPostButton = (Button)v.findViewById(R.id.new_post_button);
        newPostButton.setOnClickListener(this);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        newSubcomment = (LinearLayout) v.findViewById(R.id.new_subcomment);

        comment = (FrameLayout) v.findViewById(R.id.comment_current_post);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newSubcomment.getVisibility() == View.VISIBLE)
                    newSubcomment.setVisibility(View.GONE);
                else
                    newSubcomment.setVisibility(View.VISIBLE);
            }
        });

        deletePost = (FrameLayout) v.findViewById(R.id.delete_current_post);
        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: delete post
            }
        });

        subcomment = (LinearLayout) v.findViewById(R.id.subcomment);
        if (subcomment.getVisibility() == View.VISIBLE) {
            comment.setVisibility(View.GONE);
        } else
            comment.setVisibility(View.VISIBLE);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    TextWatcher saySomethingWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int c = 140 - saySomethingEditText.getText().length();
            if (c <= 10) {
                saySomethingCount.setTextColor(Color.RED);
            } else {
                saySomethingCount.setTextColor(Color.parseColor("#808080"));
            }

            if (c < 0) {
                saySomethingEditText.setText(saySomethingEditText.getText().toString().substring(0, saySomethingEditText.getText().length() - 1));
                saySomethingEditText.setSelection(saySomethingEditText.getText().length());
            } else
                saySomethingCount.setText("" + c);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.say_something_button:
                // TODO: upload the "say something"
                break;
            case R.id.new_post_button:
                // TODO: upload the new post
                break;
            case R.id.about_button:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.photos_button:
                break;
            case R.id.friends_button:
                break;
        }
    }
}

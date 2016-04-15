package org.sakaiproject.helpers.user_info_helpers;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.helpers.user_info_helpers.IUserAbout;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 1/13/16.
 */
public class SocialNetworkInfoHelper implements IUserAbout {

    // Social Networking
    private LinearLayout fbLayout, linkedInLayout, mySpaceLayout, skypeLayout, twitterLayout;
    private TextView fbTextView, linkedInTextView, mySpaceTextView, skypeTextView, twitterTextView;

    /**
     * SocialNetworkInfoHelper constructor
     *
     * @param v activity
     */
    public SocialNetworkInfoHelper(AppCompatActivity v) {
        initialize(v);
        fillValues();
        checkVisibilities(v);
    }

    @Override
    public void initialize(AppCompatActivity v) {
        fbLayout = (LinearLayout) v.findViewById(R.id.fb_layout);
        linkedInLayout = (LinearLayout) v.findViewById(R.id.linked_in_layout);
        mySpaceLayout = (LinearLayout) v.findViewById(R.id.my_space_layout);
        skypeLayout = (LinearLayout) v.findViewById(R.id.skype_layout);
        twitterLayout = (LinearLayout) v.findViewById(R.id.twitter_layout);

        fbTextView = (TextView) v.findViewById(R.id.fb_value);
        linkedInTextView = (TextView) v.findViewById(R.id.linked_in_value);
        mySpaceTextView = (TextView) v.findViewById(R.id.my_space_value);
        skypeTextView = (TextView) v.findViewById(R.id.skype_value);
        twitterTextView = (TextView) v.findViewById(R.id.twitter_value);
    }

    @Override
    public void fillValues() {
        if (Profile.getSocialInfo().getFacebookUrl() != null && !Profile.getSocialInfo().getFacebookUrl().equals("")) {
            fbTextView.setText(Profile.getSocialInfo().getFacebookUrl());
        } else {
            fbLayout.setVisibility(View.GONE);
        }

        if (Profile.getSocialInfo().getLinkedinUrl() != null && !Profile.getSocialInfo().getLinkedinUrl().equals("")) {
            linkedInTextView.setText(Profile.getSocialInfo().getLinkedinUrl());
        } else {
            linkedInLayout.setVisibility(View.GONE);
        }

        if (Profile.getSocialInfo().getMyspaceUrl() != null && !Profile.getSocialInfo().getMyspaceUrl().equals("")) {
            mySpaceTextView.setText(Profile.getSocialInfo().getMyspaceUrl());
        } else {
            mySpaceLayout.setVisibility(View.GONE);
        }

        if (Profile.getSocialInfo().getSkypeUsername() != null && !Profile.getSocialInfo().getSkypeUsername().equals("")) {
            skypeTextView.setText(Profile.getSocialInfo().getSkypeUsername());
        } else {
            skypeLayout.setVisibility(View.GONE);
        }

        if (Profile.getSocialInfo().getTwitterUrl() != null && !Profile.getSocialInfo().getTwitterUrl().equals("")) {
            twitterTextView.setText(Profile.getSocialInfo().getTwitterUrl());
        } else {
            twitterLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(AppCompatActivity v) {
        if (fbLayout.getVisibility() == View.GONE && linkedInLayout.getVisibility() == View.GONE && mySpaceLayout.getVisibility() == View.GONE
                && skypeLayout.getVisibility() == View.GONE && twitterLayout.getVisibility() == View.GONE) {
            v.findViewById(R.id.no_social_information).setVisibility(View.VISIBLE);
        }
    }
}

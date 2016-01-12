package org.sakaiproject.sakai;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.helpers.IUserAbout;

/**
 * Created by vasilis on 1/13/16.
 */
public class SocialNetworkInfoHelper implements IUserAbout {

    private View.OnClickListener clickListener;

    private Drawable editDrawable;

    // Social Networking
    private LinearLayout fbLayout, linkedInLayout, mySpaceLayout, skypeLayout, twitterLayout;
    private TextView fbTextView, linkedInTextView, mySpaceTextView, skypeTextView, twitterTextView;
    private ImageView editSocialInformationImageView;
    private EditText fbEditText, linkedInEditText, mySpaceEditText, skypeEditText, twitterEditText;

    public SocialNetworkInfoHelper(Activity activity, Drawable editDrawable, View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        this.editDrawable = editDrawable;

        initialize(activity);
        fillValues();
        checkVisibilities(activity);
    }

    @Override
    public void initialize(Activity activity) {
        fbLayout = (LinearLayout) activity.findViewById(R.id.fb_layout);
        linkedInLayout = (LinearLayout) activity.findViewById(R.id.linked_in_layout);
        mySpaceLayout = (LinearLayout) activity.findViewById(R.id.my_space_layout);
        skypeLayout = (LinearLayout) activity.findViewById(R.id.skype_layout);
        twitterLayout = (LinearLayout) activity.findViewById(R.id.twitter_layout);

        fbTextView = (TextView) activity.findViewById(R.id.fb_value);
        linkedInTextView = (TextView) activity.findViewById(R.id.linked_in_value);
        mySpaceTextView = (TextView) activity.findViewById(R.id.my_space_value);
        skypeTextView = (TextView) activity.findViewById(R.id.skype_value);
        twitterTextView = (TextView) activity.findViewById(R.id.twitter_value);

        editSocialInformationImageView = (ImageView) activity.findViewById(R.id.edit_social_info);
        editSocialInformationImageView.setImageDrawable(editDrawable);

        fbEditText = (EditText) activity.findViewById(R.id.fb_change);
        linkedInEditText = (EditText) activity.findViewById(R.id.linked_in_change);
        mySpaceEditText = (EditText) activity.findViewById(R.id.my_space_change);
        skypeEditText = (EditText) activity.findViewById(R.id.skype_change);
        twitterEditText = (EditText) activity.findViewById(R.id.twitter_change);

        activity.findViewById(R.id.edit_social_button).setOnClickListener(clickListener);
    }

    @Override
    public void fillValues() {
        if (!Profile.getSocialInfo().getFacebookUrl().equals("")) {
            fbTextView.setText(Profile.getSocialInfo().getFacebookUrl());
        } else {
            fbLayout.setVisibility(View.GONE);
        }

        if (!Profile.getSocialInfo().getLinkedinUrl().equals("")) {
            linkedInTextView.setText(Profile.getSocialInfo().getLinkedinUrl());
        } else {
            linkedInLayout.setVisibility(View.GONE);
        }

        if (!Profile.getSocialInfo().getMyspaceUrl().equals("")) {
            mySpaceTextView.setText(Profile.getSocialInfo().getMyspaceUrl());
        } else {
            mySpaceLayout.setVisibility(View.GONE);
        }

        if (!Profile.getSocialInfo().getSkypeUsername().equals("")) {
            skypeTextView.setText(Profile.getSocialInfo().getSkypeUsername());
        } else {
            skypeLayout.setVisibility(View.GONE);
        }

        if (!Profile.getSocialInfo().getTwitterUrl().equals("")) {
            twitterTextView.setText(Profile.getSocialInfo().getTwitterUrl());
        } else {
            twitterLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(Activity activity) {
        if (fbLayout.getVisibility() == View.GONE && linkedInLayout.getVisibility() == View.GONE && mySpaceLayout.getVisibility() == View.GONE
                && skypeLayout.getVisibility() == View.GONE && twitterLayout.getVisibility() == View.GONE) {
            activity.findViewById(R.id.no_social_information).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void enableEdit() {
        fbLayout.setVisibility(View.VISIBLE);
        linkedInLayout.setVisibility(View.VISIBLE);
        mySpaceLayout.setVisibility(View.VISIBLE);
        skypeLayout.setVisibility(View.VISIBLE);
        twitterLayout.setVisibility(View.VISIBLE);

        fbTextView.setVisibility(View.GONE);
        fbEditText.setVisibility(View.VISIBLE);
        fbEditText.setText(Profile.getSocialInfo().getFacebookUrl());

        linkedInTextView.setVisibility(View.GONE);
        linkedInEditText.setVisibility(View.VISIBLE);
        linkedInEditText.setText(Profile.getSocialInfo().getLinkedinUrl());

        mySpaceTextView.setVisibility(View.GONE);
        mySpaceEditText.setVisibility(View.VISIBLE);
        mySpaceEditText.setText(Profile.getSocialInfo().getMyspaceUrl());

        skypeTextView.setVisibility(View.GONE);
        skypeEditText.setVisibility(View.VISIBLE);
        skypeEditText.setText(Profile.getSocialInfo().getSkypeUsername());

        twitterTextView.setVisibility(View.GONE);
        twitterEditText.setVisibility(View.VISIBLE);
        twitterEditText.setText(Profile.getSocialInfo().getTwitterUrl());
    }

    @Override
    public void saveEdit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancelEdit() {
        throw new UnsupportedOperationException();
    }
}

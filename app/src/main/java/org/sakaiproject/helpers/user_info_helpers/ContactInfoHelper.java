package org.sakaiproject.helpers.user_info_helpers;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 1/12/16.
 */
public class ContactInfoHelper implements IUserAbout {

    private LinearLayout emailLayout, homePageLayout, workPhoneLayout, homePhoneLayout, mobilePhoneLayout, facsimileLayout;
    private TextView emailTextView, homePageTextView, workPhoneTextView, homePhoneTextView, mobilePhoneTextView, facsimileTextView;

    /**
     * ContactInfoHelper constructor
     *
     * @param v      activity
     */
    public ContactInfoHelper(AppCompatActivity v) {
        initialize(v);
        fillValues();
        checkVisibilities(v);
    }

    @Override
    public void initialize(AppCompatActivity v) {
        emailLayout = (LinearLayout) v.findViewById(R.id.email_layout);
        homePageLayout = (LinearLayout) v.findViewById(R.id.home_page_layout);
        workPhoneLayout = (LinearLayout) v.findViewById(R.id.work_phone_layout);
        homePhoneLayout = (LinearLayout) v.findViewById(R.id.home_phone_layout);
        mobilePhoneLayout = (LinearLayout) v.findViewById(R.id.mobile_phone_layout);
        facsimileLayout = (LinearLayout) v.findViewById(R.id.facsimile_layout);

        emailTextView = (TextView) v.findViewById(R.id.email_value);
        homePageTextView = (TextView) v.findViewById(R.id.home_page_value);
        workPhoneTextView = (TextView) v.findViewById(R.id.work_phone_value);
        homePhoneTextView = (TextView) v.findViewById(R.id.home_phone_value);
        mobilePhoneTextView = (TextView) v.findViewById(R.id.mobile_phone_value);
        facsimileTextView = (TextView) v.findViewById(R.id.facsimile_value);
    }

    @Override
    public void fillValues() {
        if (User.getEmail() != null && !User.getEmail().equals("")) {
            emailTextView.setText(User.getEmail());
        } else {
            emailLayout.setVisibility(View.GONE);
        }

        if (Profile.getHomepage() != null) {
            homePageTextView.setText(Profile.getHomepage());
        } else {
            homePageLayout.setVisibility(View.GONE);
        }

        if (Profile.getWorkphone() != null) {
            workPhoneTextView.setText(Profile.getWorkphone());
        } else {
            workPhoneLayout.setVisibility(View.GONE);
        }

        if (Profile.getHomephone() != null) {
            homePhoneTextView.setText(Profile.getHomephone());
        } else {
            homePhoneLayout.setVisibility(View.GONE);
        }

        if (Profile.getMobilephone() != null) {
            mobilePhoneTextView.setText(Profile.getMobilephone());
        } else {
            mobilePhoneLayout.setVisibility(View.GONE);
        }

        if (Profile.getFacsimile() != null) {
            facsimileTextView.setText(Profile.getFacsimile());
        } else {
            facsimileLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(AppCompatActivity v) {
        if (emailLayout.getVisibility() == View.GONE && homePageLayout.getVisibility() == View.GONE && workPhoneLayout.getVisibility() == View.GONE
                && homePhoneLayout.getVisibility() == View.GONE && mobilePhoneLayout.getVisibility() == View.GONE && facsimileLayout.getVisibility() == View.GONE) {
            v.findViewById(R.id.no_contact_information).setVisibility(View.VISIBLE);
        }
    }
}

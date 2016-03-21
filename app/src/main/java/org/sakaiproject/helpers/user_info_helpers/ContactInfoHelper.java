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

    private View.OnClickListener clickListener;

    private LinearLayout emailLayout, homePageLayout, workPhoneLayout, homePhoneLayout, mobilePhoneLayout, facsimileLayout;
    private TextView emailTextView, homePageTextView, workPhoneTextView, homePhoneTextView, mobilePhoneTextView, facsimileTextView;
    private ImageView editContactInformationImageView;
    private EditText emailEdiText, homePageEditText, workPhoneEditText, homePhoneEditText, mobilePhoneEditText, facsimileEditText;

    private Drawable editDrawable;

    /**
     * ContactInfoHelper constructor
     *
     * @param activity      the activity
     * @param editDrawable  the custom color Edit image
     * @param clickListener the onClickListener listener
     */
    public ContactInfoHelper(AppCompatActivity activity, Drawable editDrawable, View.OnClickListener clickListener) {
        this.editDrawable = editDrawable;
        this.clickListener = clickListener;
        initialize(activity);
        fillValues();
        checkVisibilities(activity);
    }

    @Override
    public void initialize(AppCompatActivity activity) {
        emailLayout = (LinearLayout) activity.findViewById(R.id.email_layout);
        homePageLayout = (LinearLayout) activity.findViewById(R.id.home_page_layout);
        workPhoneLayout = (LinearLayout) activity.findViewById(R.id.work_phone_layout);
        homePhoneLayout = (LinearLayout) activity.findViewById(R.id.home_phone_layout);
        mobilePhoneLayout = (LinearLayout) activity.findViewById(R.id.mobile_phone_layout);
        facsimileLayout = (LinearLayout) activity.findViewById(R.id.facsimile_layout);

        emailTextView = (TextView) activity.findViewById(R.id.email_value);
        homePageTextView = (TextView) activity.findViewById(R.id.home_page_value);
        workPhoneTextView = (TextView) activity.findViewById(R.id.work_phone_value);
        homePhoneTextView = (TextView) activity.findViewById(R.id.home_phone_value);
        mobilePhoneTextView = (TextView) activity.findViewById(R.id.mobile_phone_value);
        facsimileTextView = (TextView) activity.findViewById(R.id.facsimile_value);

        editContactInformationImageView = (ImageView) activity.findViewById(R.id.edit_contact_info);
        editContactInformationImageView.setImageDrawable(editDrawable);

        emailEdiText = (EditText) activity.findViewById(R.id.email_change);
        homePageEditText = (EditText) activity.findViewById(R.id.home_page_change);
        workPhoneEditText = (EditText) activity.findViewById(R.id.work_phone_change);
        homePhoneEditText = (EditText) activity.findViewById(R.id.home_phone_change);
        mobilePhoneEditText = (EditText) activity.findViewById(R.id.mobile_phone_change);
        facsimileEditText = (EditText) activity.findViewById(R.id.facsimile_change);

        // FrameLayout
        activity.findViewById(R.id.edit_contact_button).setOnClickListener(clickListener);
    }

    @Override
    public void fillValues() {
        if (User.getEmail() != null) {
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
    public void checkVisibilities(AppCompatActivity activity) {
        if (emailLayout.getVisibility() == View.GONE && homePageLayout.getVisibility() == View.GONE && workPhoneLayout.getVisibility() == View.GONE
                && homePhoneLayout.getVisibility() == View.GONE && mobilePhoneLayout.getVisibility() == View.GONE && facsimileLayout.getVisibility() == View.GONE) {
            activity.findViewById(R.id.no_contact_information).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void enableEdit() {
        emailLayout.setVisibility(View.VISIBLE);
        homePageLayout.setVisibility(View.VISIBLE);
        workPhoneLayout.setVisibility(View.VISIBLE);
        homePhoneLayout.setVisibility(View.VISIBLE);
        mobilePhoneLayout.setVisibility(View.VISIBLE);
        facsimileLayout.setVisibility(View.VISIBLE);

        emailTextView.setVisibility(View.GONE);
        emailEdiText.setVisibility(View.VISIBLE);
        emailEdiText.setText(User.getEmail());

        homePageTextView.setVisibility(View.GONE);
        homePageEditText.setVisibility(View.VISIBLE);
        homePageEditText.setText(Profile.getHomepage());

        workPhoneTextView.setVisibility(View.GONE);
        workPhoneEditText.setVisibility(View.VISIBLE);
        workPhoneEditText.setText(Profile.getWorkphone());

        homePhoneTextView.setVisibility(View.GONE);
        homePhoneEditText.setVisibility(View.VISIBLE);
        homePhoneEditText.setText(Profile.getHomephone());

        mobilePhoneTextView.setVisibility(View.GONE);
        mobilePhoneEditText.setVisibility(View.VISIBLE);
        mobilePhoneEditText.setText(Profile.getMobilephone());

        facsimileTextView.setVisibility(View.GONE);
        facsimileEditText.setVisibility(View.VISIBLE);
        facsimileEditText.setText(Profile.getFacsimile());
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

package org.sakaiproject.helpers.user_info_helpers;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 1/12/16.
 */
public class StaffInfoHelper implements IUserAbout {

    private LinearLayout positionLayout, departmentLayout, schoolLayout, roomLayout, staffProfileLayout, universityUrlLayout, academicUrlLayout, publicationsLayout;
    private TextView positionTextView, departmentTextView, schoolTextView, roomTextView, staffProfileTextView, universityUrlTextView, academicUrlTextView, publicationsTextView;

    /**
     * StaffInfoHelper constructor
     *
     * @param v activity
     */
    public StaffInfoHelper(AppCompatActivity v) {
        initialize(v);
        fillValues();
        checkVisibilities(v);
    }

    @Override
    public void initialize(AppCompatActivity v) {
        positionLayout = (LinearLayout) v.findViewById(R.id.position_layout);
        departmentLayout = (LinearLayout) v.findViewById(R.id.department_layout);
        schoolLayout = (LinearLayout) v.findViewById(R.id.school_layout);
        roomLayout = (LinearLayout) v.findViewById(R.id.room_layout);
        staffProfileLayout = (LinearLayout) v.findViewById(R.id.staff_profile_layout);
        universityUrlLayout = (LinearLayout) v.findViewById(R.id.university_url_layout);
        academicUrlLayout = (LinearLayout) v.findViewById(R.id.academic_url_layout);
        publicationsLayout = (LinearLayout) v.findViewById(R.id.publication_layout);

        positionTextView = (TextView) v.findViewById(R.id.position_value);
        departmentTextView = (TextView) v.findViewById(R.id.department_value);
        schoolTextView = (TextView) v.findViewById(R.id.school_value);
        roomTextView = (TextView) v.findViewById(R.id.room_value);
        staffProfileTextView = (TextView) v.findViewById(R.id.staff_profile_value);
        universityUrlTextView = (TextView) v.findViewById(R.id.university_url_value);
        academicUrlTextView = (TextView) v.findViewById(R.id.academic_url_value);
        publicationsTextView = (TextView) v.findViewById(R.id.publication_value);
    }

    @Override
    public void fillValues() {
        if (Profile.getPosition() != null && !Profile.getPosition().equals("")) {
            positionTextView.setText(Profile.getPosition());
        } else {
            positionLayout.setVisibility(View.GONE);
        }

        if (Profile.getDepartment() != null && !Profile.getDepartment().equals("")) {
            departmentTextView.setText(Profile.getDepartment());
        } else {
            departmentLayout.setVisibility(View.GONE);
        }

        if (Profile.getSchool() != null && !Profile.getSchool().equals("")) {
            schoolTextView.setText(Profile.getSchool());
        } else {
            schoolLayout.setVisibility(View.GONE);
        }

        if (Profile.getRoom() != null && !Profile.getRoom().equals("")) {
            roomTextView.setText(Profile.getRoom().trim());
        } else {
            roomLayout.setVisibility(View.GONE);
        }

        if (Profile.getStaffProfile() != null) {
            staffProfileTextView.setText(Html.fromHtml(Profile.getStaffProfile()));
        } else {
            staffProfileLayout.setVisibility(View.GONE);
        }

        if (Profile.getUniversityProfileUrl() != null) {
            universityUrlTextView.setText(Profile.getUniversityProfileUrl());
        } else {
            universityUrlLayout.setVisibility(View.GONE);
        }

        if (Profile.getAcademicProfileUrl() != null) {
            academicUrlTextView.setText(Profile.getAcademicProfileUrl());
        } else {
            academicUrlLayout.setVisibility(View.GONE);
        }

        if (Profile.getPublications() != null) {
            publicationsTextView.setText(Html.fromHtml(Profile.getPublications()));
        } else {
            publicationsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(AppCompatActivity v) {
        if (positionLayout.getVisibility() == View.GONE && departmentLayout.getVisibility() == View.GONE && schoolLayout.getVisibility() == View.GONE
                && roomLayout.getVisibility() == View.GONE && staffProfileLayout.getVisibility() == View.GONE && universityUrlLayout.getVisibility() == View.GONE
                && academicUrlLayout.getVisibility() == View.GONE && publicationsLayout.getVisibility() == View.GONE) {
            v.findViewById(R.id.no_staff_information).setVisibility(View.VISIBLE);
        }
    }
}

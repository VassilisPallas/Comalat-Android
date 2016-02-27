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

    private View.OnClickListener clickListener;
    private Drawable editDrawable;

    private LinearLayout positionLayout, departmentLayout, schoolLayout, roomLayout, staffProfileLayout, universityUrlLayout, academicUrlLayout, publicationsLayout;
    private TextView positionTextView, departmentTextView, schoolTextView, roomTextView, staffProfileTextView, universityUrlTextView, academicUrlTextView, publicationsTextView;
    private ImageView editStaffInformationImageView;
    private EditText positionEditText, departmentEditText, schoolEditText, roomEditText, staffProfileEditText, universityUrlEditText, academicUrlEditText, publicationsEditText;

    /**
     * StaffInfoHelper constructor
     * @param activity the activity
     * @param editDrawable the custom color Edit image
     * @param clickListener the onClickListener listener
     */
    public StaffInfoHelper(AppCompatActivity activity, Drawable editDrawable, View.OnClickListener clickListener) {
        this.editDrawable = editDrawable;
        this.clickListener = clickListener;

        initialize(activity);
        fillValues();
        checkVisibilities(activity);
    }

    @Override
    public void initialize(AppCompatActivity activity) {
        positionLayout = (LinearLayout) activity.findViewById(R.id.position_layout);
        departmentLayout = (LinearLayout) activity.findViewById(R.id.department_layout);
        schoolLayout = (LinearLayout) activity.findViewById(R.id.school_layout);
        roomLayout = (LinearLayout) activity.findViewById(R.id.room_layout);
        staffProfileLayout = (LinearLayout) activity.findViewById(R.id.staff_profile_layout);
        universityUrlLayout = (LinearLayout) activity.findViewById(R.id.university_url_layout);
        academicUrlLayout = (LinearLayout) activity.findViewById(R.id.academic_url_layout);
        publicationsLayout = (LinearLayout) activity.findViewById(R.id.publication_layout);

        positionTextView = (TextView) activity.findViewById(R.id.position_value);
        departmentTextView = (TextView) activity.findViewById(R.id.department_value);
        schoolTextView = (TextView) activity.findViewById(R.id.school_value);
        roomTextView = (TextView) activity.findViewById(R.id.room_value);
        staffProfileTextView = (TextView) activity.findViewById(R.id.staff_profile_value);
        universityUrlTextView = (TextView) activity.findViewById(R.id.university_url_value);
        academicUrlTextView = (TextView) activity.findViewById(R.id.academic_url_value);
        publicationsTextView = (TextView) activity.findViewById(R.id.publication_value);

        editStaffInformationImageView = (ImageView) activity.findViewById(R.id.edit_staff_info);
        editStaffInformationImageView.setImageDrawable(editDrawable);

        positionEditText = (EditText) activity.findViewById(R.id.position_change);
        departmentEditText = (EditText) activity.findViewById(R.id.department_change);
        schoolEditText = (EditText) activity.findViewById(R.id.school_change);
        roomEditText = (EditText) activity.findViewById(R.id.room_change);
        staffProfileEditText = (EditText) activity.findViewById(R.id.staff_profile_change);
        universityUrlEditText = (EditText) activity.findViewById(R.id.university_url_change);
        academicUrlEditText = (EditText) activity.findViewById(R.id.academic_url_change);
        publicationsEditText = (EditText) activity.findViewById(R.id.publication_change);

        // FrameLayout
        activity.findViewById(R.id.edit_staff_button).setOnClickListener(clickListener);
    }

    @Override
    public void fillValues() {
        if (Profile.getPosition() != null) {
            positionTextView.setText(Profile.getPosition());
        } else {
            positionLayout.setVisibility(View.GONE);
        }

        if (Profile.getDepartment() != null) {
            departmentTextView.setText(Profile.getDepartment());
        } else {
            departmentLayout.setVisibility(View.GONE);
        }

        if (Profile.getSchool() != null) {
            schoolTextView.setText(Profile.getSchool());
        } else {
            schoolLayout.setVisibility(View.GONE);
        }

        if (Profile.getRoom() != null) {
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
    public void checkVisibilities(AppCompatActivity activity) {
        if (positionLayout.getVisibility() == View.GONE && departmentLayout.getVisibility() == View.GONE && schoolLayout.getVisibility() == View.GONE
                && roomLayout.getVisibility() == View.GONE && staffProfileLayout.getVisibility() == View.GONE && universityUrlLayout.getVisibility() == View.GONE
                && academicUrlLayout.getVisibility() == View.GONE && publicationsLayout.getVisibility() == View.GONE) {
            activity.findViewById(R.id.no_staff_information).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void enableEdit() {
        positionLayout.setVisibility(View.VISIBLE);
        departmentLayout.setVisibility(View.VISIBLE);
        schoolLayout.setVisibility(View.VISIBLE);
        roomLayout.setVisibility(View.VISIBLE);
        staffProfileLayout.setVisibility(View.VISIBLE);
        universityUrlLayout.setVisibility(View.VISIBLE);
        academicUrlLayout.setVisibility(View.VISIBLE);
        publicationsLayout.setVisibility(View.VISIBLE);

        positionTextView.setVisibility(View.GONE);
        positionEditText.setVisibility(View.VISIBLE);
        positionEditText.setText(Profile.getPosition());

        departmentTextView.setVisibility(View.GONE);
        departmentEditText.setVisibility(View.VISIBLE);
        departmentEditText.setText(Profile.getDepartment());

        schoolTextView.setVisibility(View.GONE);
        schoolEditText.setVisibility(View.VISIBLE);
        schoolEditText.setText(Profile.getSchool());

        roomTextView.setVisibility(View.GONE);
        roomEditText.setVisibility(View.VISIBLE);
        roomEditText.setText(Profile.getRoom());

        staffProfileTextView.setVisibility(View.GONE);
        staffProfileEditText.setVisibility(View.VISIBLE);
        staffProfileEditText.setText(Profile.getStaffProfile());

        universityUrlTextView.setVisibility(View.GONE);
        universityUrlEditText.setVisibility(View.VISIBLE);
        universityUrlEditText.setText(Profile.getUniversityProfileUrl());

        academicUrlTextView.setVisibility(View.GONE);
        academicUrlEditText.setVisibility(View.VISIBLE);
        academicUrlEditText.setText(Profile.getAcademicProfileUrl());

        publicationsTextView.setVisibility(View.GONE);
        publicationsEditText.setVisibility(View.VISIBLE);
        publicationsEditText.setText(Profile.getPublications());
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

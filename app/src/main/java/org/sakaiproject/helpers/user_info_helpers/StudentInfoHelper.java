package org.sakaiproject.helpers.user_info_helpers;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
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
public class StudentInfoHelper implements IUserAbout {


    private LinearLayout degreeLayout, subjectsLayout;
    private TextView degreeTextView, subjectTextView;

    /**
     * StudentInfoHelper constructor
     *
     * @param v activity
     */
    public StudentInfoHelper(AppCompatActivity v) {
        initialize(v);
        fillValues();
        checkVisibilities(v);
    }

    @Override
    public void initialize(AppCompatActivity v) {
        degreeLayout = (LinearLayout) v.findViewById(R.id.degree_layout);
        subjectsLayout = (LinearLayout) v.findViewById(R.id.subjects_layout);

        degreeTextView = (TextView) v.findViewById(R.id.degree_value);
        subjectTextView = (TextView) v.findViewById(R.id.subjects_value);
    }

    @Override
    public void fillValues() {
        if (Profile.getCourse() != null) {
            degreeTextView.setText(Profile.getCourse());
        } else {
            degreeLayout.setVisibility(View.GONE);
        }

        if (Profile.getSubjects() != null) {
            subjectTextView.setText(Profile.getSubjects());
        } else {
            subjectsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(AppCompatActivity v) {
        if (degreeLayout.getVisibility() == View.GONE && subjectsLayout.getVisibility() == View.GONE) {
            v.findViewById(R.id.no_student_information).setVisibility(View.VISIBLE);
        }
    }

}

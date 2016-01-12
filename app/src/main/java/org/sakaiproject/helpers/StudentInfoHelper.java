package org.sakaiproject.helpers;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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

    private View.OnClickListener clickListener;

    private Drawable editDrawable;

    private LinearLayout degreeLayout, subjectsLayout;
    private TextView degreeTextView, subjectTextView;
    private ImageView editStudentInformationImageView;
    private EditText degreeEditText, subjectsEditText;

    public StudentInfoHelper(Activity activity, Drawable editDrawable, View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        this.editDrawable = editDrawable;

        initialize(activity);
        fillValues();
        checkVisibilities(activity);
    }

    @Override
    public void initialize(Activity activity) {
        degreeLayout = (LinearLayout) activity.findViewById(R.id.degree_layout);
        subjectsLayout = (LinearLayout) activity.findViewById(R.id.subjects_layout);

        degreeTextView = (TextView) activity.findViewById(R.id.degree_value);
        subjectTextView = (TextView) activity.findViewById(R.id.subjects_value);

        editStudentInformationImageView = (ImageView) activity.findViewById(R.id.edit_student_info);
        editStudentInformationImageView.setImageDrawable(editDrawable);

        degreeEditText = (EditText) activity.findViewById(R.id.degree_change);
        subjectsEditText = (EditText) activity.findViewById(R.id.subjects_change);

        activity.findViewById(R.id.edit_student_button).setOnClickListener(clickListener);
    }

    @Override
    public void fillValues() {
        if (!Profile.getCourse().equals("")) {
            degreeTextView.setText(Profile.getCourse());
        } else {
            degreeLayout.setVisibility(View.GONE);
        }

        if (!Profile.getSubjects().equals("")) {
            subjectTextView.setText(Profile.getSubjects());
        } else {
            subjectsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(Activity activity) {
        if (degreeLayout.getVisibility() == View.GONE && subjectsLayout.getVisibility() == View.GONE) {
            activity.findViewById(R.id.no_student_information).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void enableEdit() {
        degreeLayout.setVisibility(View.VISIBLE);
        subjectsLayout.setVisibility(View.VISIBLE);

        degreeTextView.setVisibility(View.GONE);
        degreeEditText.setVisibility(View.VISIBLE);
        degreeEditText.setText(Profile.getCourse());

        subjectTextView.setVisibility(View.GONE);
        subjectsEditText.setVisibility(View.VISIBLE);
        subjectsEditText.setText(Profile.getSubjects());

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

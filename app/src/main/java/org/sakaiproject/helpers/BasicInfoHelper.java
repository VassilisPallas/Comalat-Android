package org.sakaiproject.helpers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.sakai.R;

/**
 * Created by vasilis on 1/12/16.
 */
public class BasicInfoHelper implements IUserAbout {

    private View.OnClickListener clickListener;

    private int year;
    private int month;
    private int day;

    private LinearLayout nicknameLayout, birthdayLayout, personalSummaryLayout;
    private TextView nicknameTextView, birthdayTextView, personalSummaryTextView;
    private ImageView editBasicInformationImageView, datePickerImageView;
    private DatePicker birthdayPicker;
    private EditText nicknameEditText, personalSummaryEditText;

    private Drawable calendarDrawable, editDrawable;

    public BasicInfoHelper(Activity activity, Drawable editDrawable, View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        this.editDrawable = editDrawable;
        calendarDrawable = Actions.setCustomDrawableColor(activity.getApplicationContext(), R.mipmap.ic_today, Color.parseColor("#0083AF"));
        initialize(activity);
        fillValues();
        checkVisibilities(activity);
    }

    public DatePicker getBirthdayPicker() {
        return birthdayPicker;
    }

    @Override
    public void initialize(Activity activity) {
        nicknameLayout = (LinearLayout) activity.findViewById(R.id.nickname_layout);
        birthdayLayout = (LinearLayout) activity.findViewById(R.id.birthday_layout);
        personalSummaryLayout = (LinearLayout) activity.findViewById(R.id.personal_summary_layout);
        birthdayPicker = (DatePicker) activity.findViewById(R.id.birthday_picker);
        datePickerImageView = (ImageView) activity.findViewById(R.id.datepicker_imageview);
        datePickerImageView.setImageDrawable(calendarDrawable);
        datePickerImageView.setOnClickListener(clickListener);

        month = Profile.getDateOfBirth().getMonth();
        day = Profile.getDateOfBirth().getDate();
        year = Integer.valueOf(Profile.getDateOfBirth().getYear() > 20 ? "19" + Profile.getDateOfBirth().getYear() : "20" + Profile.getDateOfBirth().getYear());
        birthdayPicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
                month = monthOfYear;
                day = dayOfMonth;
                year = Year;
                birthdayTextView.setText(Actions.getMonthfromIndex(month) + " " + day + ", " + year);
                birthdayPicker.setVisibility(View.GONE);
            }
        });

        nicknameTextView = (TextView) activity.findViewById(R.id.nickname_value);
        birthdayTextView = (TextView) activity.findViewById(R.id.birthday_value);
        personalSummaryTextView = (TextView) activity.findViewById(R.id.personal_summary_value);

        editBasicInformationImageView = (ImageView) activity.findViewById(R.id.edit_basic_info);
        editBasicInformationImageView.setImageDrawable(editDrawable);

        nicknameEditText = (EditText) activity.findViewById(R.id.nickname_change);
        personalSummaryEditText = (EditText) activity.findViewById(R.id.personal_summary_change);

        activity.findViewById(R.id.edit_basic_button).setOnClickListener(clickListener);
    }

    @Override
    public void fillValues() {
        if (!Profile.getNickname().equals("")) {
            nicknameTextView.setText(Profile.getNickname());
        } else {
            nicknameLayout.setVisibility(View.GONE);
        }

        if (Profile.getDateOfBirth() != null) {// if year is > 20 eg 80 then 1980, else eg 04 then 2004
            birthdayTextView.setText(Actions.getMonthfromIndex(month));
            birthdayTextView.append(" " + day);
            birthdayTextView.append(", " + year);
        } else {
            birthdayLayout.setVisibility(View.GONE);
        }

        if (!Profile.getPersonalSummary().equals("")) {
            personalSummaryTextView.setText(Html.fromHtml(Profile.getPersonalSummary()));
        } else {
            personalSummaryLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(Activity activity) {
        if (nicknameLayout.getVisibility() == View.GONE && birthdayLayout.getVisibility() == View.GONE && personalSummaryLayout.getVisibility() == View.GONE) {
            activity.findViewById(R.id.no_basic_information).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void enableEdit() {
        nicknameLayout.setVisibility(View.VISIBLE);
        birthdayLayout.setVisibility(View.VISIBLE);
        personalSummaryLayout.setVisibility(View.VISIBLE);

        nicknameTextView.setVisibility(View.GONE);
        nicknameEditText.setVisibility(View.VISIBLE);
        nicknameEditText.setText(Profile.getNickname());

        datePickerImageView.setVisibility(View.VISIBLE);

        personalSummaryTextView.setVisibility(View.GONE);
        personalSummaryEditText.setVisibility(View.VISIBLE);
        personalSummaryEditText.setText(Profile.getPersonalSummary());
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

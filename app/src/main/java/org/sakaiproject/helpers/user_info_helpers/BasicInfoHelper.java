package org.sakaiproject.helpers.user_info_helpers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.profile.update.UpdateUserInfo;
import org.sakaiproject.general.Actions;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.sakai.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vasilis on 1/12/16.
 */
public class BasicInfoHelper implements IUserAbout {

    private AppCompatActivity activity;

    private View.OnClickListener clickListener;

    private int year;
    private int month;
    private int day;

    private LinearLayout nicknameLayout, birthdayLayout, personalSummaryLayout;
    private TextView nicknameTextView, birthdayTextView, personalSummaryTextView;
    private ImageView editBasicInformationImageView, datePickerImageView;
    private DatePicker birthdayPicker;
    private EditText nicknameEditText, personalSummaryEditText;
    private Button saveChangesButton, cancelChangesButton;

    private Drawable calendarDrawable, editDrawable;

    /**
     * BasicInfoHelper constructor
     *
     * @param activity      the activity
     * @param editDrawable  the custom color Edit image
     * @param clickListener the onClickListener listener
     */
    public BasicInfoHelper(AppCompatActivity activity, Drawable editDrawable, View.OnClickListener clickListener) {
        this.activity = activity;
        this.clickListener = clickListener;
        this.editDrawable = editDrawable;
        calendarDrawable = Actions.setCustomDrawableColor(activity.getApplicationContext(), R.mipmap.ic_today, Color.parseColor("#43C84E"));
        initialize(activity);
        fillValues();
        checkVisibilities(activity);
    }

    public DatePicker getBirthdayPicker() {
        return birthdayPicker;
    }

    @Override
    public void initialize(AppCompatActivity activity) {
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
                birthdayTextView.setText(Actions.getMonthfromIndex(month));
                birthdayTextView.append(" ");
                birthdayTextView.append(String.valueOf(day));
                birthdayTextView.append(", ");
                birthdayTextView.append(String.valueOf(year));
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

        saveChangesButton = (Button) activity.findViewById(R.id.basic_info_save_changes_button);
        saveChangesButton.setOnClickListener(clickListener);

        cancelChangesButton = (Button) activity.findViewById(R.id.basic_info_cancel_button);
        cancelChangesButton.setOnClickListener(clickListener);

        activity.findViewById(R.id.edit_basic_button).setOnClickListener(clickListener);
    }

    @Override
    public void fillValues() {
        if (Profile.getNickname() != null) {
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

        if (Profile.getPersonalSummary() != null) {
            personalSummaryTextView.setText(Html.fromHtml(Profile.getPersonalSummary()));
        } else {
            personalSummaryLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkVisibilities(AppCompatActivity activity) {
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

        activity.findViewById(R.id.basic_information_buttons).setVisibility(View.VISIBLE);
    }

    @Override
    public void saveEdit() {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity.getSupportActionBar().getThemedContext());

        adb.setTitle(activity.getApplicationContext().getResources().getString(R.string.save_edits));

        adb.setPositiveButton(activity.getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Profile.setNickname(nicknameEditText.getText().toString());

                String birthday = birthdayTextView.getText().toString();
                SimpleDateFormat birthdayFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                SimpleDateFormat yearFormat = new SimpleDateFormat("yy", Locale.US);
                try {
                    Date d = birthdayFormat.parse(birthday);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);

                    Profile.getDateOfBirth().setDate(cal.get(Calendar.DAY_OF_MONTH));
                    Profile.getDateOfBirth().setDay(cal.get(Calendar.DAY_OF_WEEK) - 1);
                    Profile.getDateOfBirth().setMonth(cal.get(Calendar.MONTH));
                    Profile.getDateOfBirth().setTime(d.getTime());
                    Profile.getDateOfBirth().setYear(Integer.valueOf(yearFormat.format(d)));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Profile.setPersonalSummary(personalSummaryEditText.getText().toString());


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateUserInfo updateUserInfo = new UpdateUserInfo(activity.getApplication());
                        updateUserInfo.updateInfo(activity.getApplicationContext().getResources().getString(R.string.url) + "/profile/" + User.getUserId() + ".json");
                    }
                }).start();

                activity.findViewById(R.id.basic_information_buttons).setVisibility(View.GONE);

                hideEditTexts();

                fillValues();
            }
        });


        adb.setNegativeButton(activity.getApplicationContext().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                activity.findViewById(R.id.basic_information_buttons).setVisibility(View.GONE);

                hideEditTexts();

                fillValues();
            }
        });

        adb.setNeutralButton(activity.getApplicationContext().getResources().getString(R.string.cancel), null);

        Dialog d = adb.show();
    }

    @Override
    public void cancelEdit() {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity.getSupportActionBar().getThemedContext());

        adb.setTitle(activity.getApplicationContext().getResources().getString(R.string.cancel_edits));

        adb.setPositiveButton(activity.getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                activity.findViewById(R.id.basic_information_buttons).setVisibility(View.GONE);

                hideEditTexts();

                fillValues();
            }
        });

        adb.setNegativeButton(activity.getApplicationContext().getResources().getString(R.string.no), null);

        Dialog d = adb.show();
    }

    private void hideEditTexts() {
        nicknameEditText.setVisibility(View.GONE);
        nicknameTextView.setVisibility(View.VISIBLE);
        datePickerImageView.setVisibility(View.INVISIBLE);
        personalSummaryEditText.setVisibility(View.GONE);
        personalSummaryTextView.setVisibility(View.VISIBLE);
    }

}

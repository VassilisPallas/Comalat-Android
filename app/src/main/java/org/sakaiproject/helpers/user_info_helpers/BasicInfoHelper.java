package org.sakaiproject.helpers.user_info_helpers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import org.sakaiproject.helpers.ActionsHelper;
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

    private int year;
    private int month;
    private int day;

    private LinearLayout nicknameLayout, birthdayLayout, personalSummaryLayout;
    private TextView nicknameTextView, birthdayTextView, personalSummaryTextView;

    /**
     * BasicInfoHelper constructor
     *
     * @param v activity
     */
    public BasicInfoHelper(AppCompatActivity v) {
        initialize(v);
        fillValues();
        checkVisibilities(v);
    }

    @Override
    public void initialize(AppCompatActivity v) {
        nicknameLayout = (LinearLayout) v.findViewById(R.id.nickname_layout);
        birthdayLayout = (LinearLayout) v.findViewById(R.id.birthday_layout);
        personalSummaryLayout = (LinearLayout) v.findViewById(R.id.personal_summary_layout);

        if (Profile.getDateOfBirth() != null) {
            month = Profile.getDateOfBirth().getMonth();
            day = Profile.getDateOfBirth().getDate();
            year = Integer.valueOf(Profile.getDateOfBirth().getYear() > 20 ? "19" + Profile.getDateOfBirth().getYear() : "20" + Profile.getDateOfBirth().getYear());
        }

        nicknameTextView = (TextView) v.findViewById(R.id.nickname_value);
        birthdayTextView = (TextView) v.findViewById(R.id.birthday_value);
        personalSummaryTextView = (TextView) v.findViewById(R.id.personal_summary_value);
    }

    @Override
    public void fillValues() {
        if (Profile.getNickname() != null && !Profile.getNickname().equals("")) {
            nicknameTextView.setText(Profile.getNickname());
        } else {
            nicknameLayout.setVisibility(View.GONE);
        }

        if (Profile.getDateOfBirth() != null) {// if year is > 20 eg 80 then 1980, else eg 04 then 2004
            birthdayTextView.setText(ActionsHelper.getMonthfromIndex(month));
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
    public void checkVisibilities(AppCompatActivity v) {
        if (nicknameLayout.getVisibility() == View.GONE && birthdayLayout.getVisibility() == View.GONE && personalSummaryLayout.getVisibility() == View.GONE) {
            v.findViewById(R.id.no_basic_information).setVisibility(View.VISIBLE);
        }
    }
}

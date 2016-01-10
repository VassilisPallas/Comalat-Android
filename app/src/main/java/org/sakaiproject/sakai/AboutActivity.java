package org.sakaiproject.sakai;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.session.RefreshSession;
import org.sakaiproject.api.session.Waiter;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.customviews.AboutScrollView;
import org.sakaiproject.customviews.ProfileScrollView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private Profile profile;
    private User user;
    private Waiter waiter;  //Thread which controls idle time
    private Connection connection = Connection.getInstance();

    private AboutScrollView scrollView;

    private int year;
    private int month;
    private int day;

    // Basic Information
    private LinearLayout nicknameLayout, birthdayLayout, personalSummaryLayout;
    private TextView nicknameTextView, birthdayTextView, personalSummaryTextView;
    private ImageView editBasicInformationImageView, datePickerImageView;
    private DatePicker birthdayPicker;
    private EditText nicknameEditText, personalSummaryEditText;

    // Contact Information
    private LinearLayout emailLayout, homePageLayout, workPhoneLayout, homePhoneLayout, mobilePhoneLayout, facsimileLayout;
    private TextView emailTextView, homePageTextView, workPhoneTextView, homePhoneTextView, mobilePhoneTextView, facsimileTextView;
    private ImageView editContactInformationImageView;

    // Staff Information
    private LinearLayout positionLayout, departmentLayout, schoolLayout, roomLayout, staffProfileLayout, universityUrlLayout, academicUrlLayout, publicationsLayout;
    private TextView positionTextView, departmentTextView, schoolTextView, roomTextView, staffProfileTextView, universityUrlTextView, academicUrlTextView, publicationsTextView;
    private ImageView editStaffInformationImageView;

    // Student Information
    private LinearLayout degreeLayout, subjectsLayout;
    private TextView degreeTextView, subjectTextView;
    private ImageView editStudentInformationImageView;

    // Social Networking
    private LinearLayout fbLayout, linkedInLayout, mySpaceLayout, skypeLayout, twitterLayout;
    private TextView fbTextView, linkedInTextView, mySpaceTextView, skypeTextView, twitterTextView;
    private ImageView editSocialInformationImageView;

    // Personal Information
    private LinearLayout booksLayout, tvShowsLayout, moviesLayout, quotesLayout;
    private TextView booksTextView, tvShowsTextView, moviesTextView, quotesTextView;
    private ImageView editPersonalInformationImageView;

    private Drawable editDrawable, calendarDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        editDrawable = Actions.setCustomDrawableColor(this, R.mipmap.ic_create, Color.parseColor("#046a8c"));
        calendarDrawable = Actions.setCustomDrawableColor(this, R.mipmap.ic_today, Color.parseColor("#FFBF00"));

        /* if device is connected on the internet that means that the user made login with internet connection,
           so the thread for the idle mode will start
        */
        if (NetWork.getConnectionEstablished()) {
            waiter = Waiter.getInstance();
            waiter.stop = false;
            waiter.setActivity(AboutActivity.class);
            waiter.setContext(this);
            waiter.setPeriod(Connection.getMaxInactiveInterval() * 1000); // 1800 milliseconds * 1000 = 30 mins
            if (waiter.getState() == Thread.State.NEW)
                waiter.start();

            // if the message for the session id is visible
            if (waiter.isNotificationShowed()) {
                connection = Connection.getInstance();
                connection.setContext(getApplicationContext());
                // if the session has expired, the session id will be null, so the user will go to MainActivity
                if (connection.getSessionId() == null) {
                    waiter.stop = true;
                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("session_expired", true);
                    startActivity(i);
                } else
                    updateSession();
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("About");

        profile = Profile.getInstance();
        user = User.getInstance();
        findViewsById();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        touch();
        Log.d("activity", "User interaction to " + this.toString());
    }

    public void touch() {
        if (NetWork.getConnectionEstablished()) {
            waiter.touch();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NetWork.getConnectionEstablished()) {
            waiter.setActivityIsVisible(false);
        }
        Log.i("visible", "false");
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (NetWork.getConnectionEstablished()) {
            waiter.setActivityIsVisible(true);
            waiter.touch();
        }
        Log.i("visible", "true");
    }

    public void updateSession() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {
                    try {
                        RefreshSession refreshSession = new RefreshSession(getApplicationContext());
                        refreshSession.putSession(getResources().getString(R.string.url) + "session/" + connection.getSessionId() + ".json");
                        waiter.stop = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void findViewsById() {
        scrollView = (AboutScrollView) findViewById(R.id.about_scrollview);

        // Basic Information
        nicknameLayout = (LinearLayout) findViewById(R.id.nickname_layout);
        birthdayLayout = (LinearLayout) findViewById(R.id.birthday_layout);
        personalSummaryLayout = (LinearLayout) findViewById(R.id.personal_summary_layout);
        birthdayPicker = (DatePicker) findViewById(R.id.birthday_picker);
        datePickerImageView = (ImageView) findViewById(R.id.datepicker_imageview);
        datePickerImageView.setImageDrawable(calendarDrawable);
        datePickerImageView.setOnClickListener(this);

        month = profile.getDateOfBirth().getMonth();
        day = profile.getDateOfBirth().getDate();
        year = Integer.valueOf(profile.getDateOfBirth().getYear() > 20 ? "19" + profile.getDateOfBirth().getYear() : "20" + profile.getDateOfBirth().getYear());
        birthdayPicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
                month = monthOfYear;
                day = dayOfMonth;
                year = Year;
                birthdayTextView.setText(Actions.getMonthfromIndex(month) + " " + day + ", " + year);
            }
        });

        nicknameTextView = (TextView) findViewById(R.id.nickname_value);
        birthdayTextView = (TextView) findViewById(R.id.birthday_value);
        personalSummaryTextView = (TextView) findViewById(R.id.personal_summary_value);

        editBasicInformationImageView = (ImageView) findViewById(R.id.edit_basic_info);
        editBasicInformationImageView.setImageDrawable(editDrawable);

        nicknameEditText = (EditText) findViewById(R.id.nickname_change);
        personalSummaryEditText = (EditText) findViewById(R.id.personal_summary_change);

        findViewById(R.id.edit_basic_button).setOnClickListener(this);

        // Contact Information
        emailLayout = (LinearLayout) findViewById(R.id.email_layout);
        homePageLayout = (LinearLayout) findViewById(R.id.home_page_layout);
        workPhoneLayout = (LinearLayout) findViewById(R.id.work_phone_layout);
        homePhoneLayout = (LinearLayout) findViewById(R.id.home_phone_layout);
        mobilePhoneLayout = (LinearLayout) findViewById(R.id.mobile_phone_layout);
        facsimileLayout = (LinearLayout) findViewById(R.id.facsimile_layout);

        emailTextView = (TextView) findViewById(R.id.email_value);
        homePageTextView = (TextView) findViewById(R.id.home_page_value);
        workPhoneTextView = (TextView) findViewById(R.id.work_phone_value);
        homePhoneTextView = (TextView) findViewById(R.id.home_phone_value);
        mobilePhoneTextView = (TextView) findViewById(R.id.mobile_phone_value);
        facsimileTextView = (TextView) findViewById(R.id.facsimile_value);

        editContactInformationImageView = (ImageView) findViewById(R.id.edit_contact_info);
        editContactInformationImageView.setImageDrawable(editDrawable);


        // Staff Information
        positionLayout = (LinearLayout) findViewById(R.id.position_layout);
        departmentLayout = (LinearLayout) findViewById(R.id.department_layout);
        schoolLayout = (LinearLayout) findViewById(R.id.school_layout);
        roomLayout = (LinearLayout) findViewById(R.id.room_layout);
        staffProfileLayout = (LinearLayout) findViewById(R.id.staff_profile_layout);
        universityUrlLayout = (LinearLayout) findViewById(R.id.university_url_layout);
        academicUrlLayout = (LinearLayout) findViewById(R.id.academic_url_layout);
        publicationsLayout = (LinearLayout) findViewById(R.id.publication_layout);

        positionTextView = (TextView) findViewById(R.id.position_value);
        departmentTextView = (TextView) findViewById(R.id.department_value);
        schoolTextView = (TextView) findViewById(R.id.school_value);
        roomTextView = (TextView) findViewById(R.id.room_value);
        staffProfileTextView = (TextView) findViewById(R.id.staff_profile_value);
        universityUrlTextView = (TextView) findViewById(R.id.university_url_value);
        academicUrlTextView = (TextView) findViewById(R.id.academic_url_value);
        publicationsTextView = (TextView) findViewById(R.id.publication_value);

        editStaffInformationImageView = (ImageView) findViewById(R.id.edit_staff_info);
        editStaffInformationImageView.setImageDrawable(editDrawable);

        // Student Information
        degreeLayout = (LinearLayout) findViewById(R.id.degree_layout);
        subjectsLayout = (LinearLayout) findViewById(R.id.subjects_layout);

        degreeTextView = (TextView) findViewById(R.id.degree_value);
        subjectTextView = (TextView) findViewById(R.id.subjects_value);

        editStudentInformationImageView = (ImageView) findViewById(R.id.edit_student_info);
        editStudentInformationImageView.setImageDrawable(editDrawable);

        // Social Networking
        fbLayout = (LinearLayout) findViewById(R.id.fb_layout);
        linkedInLayout = (LinearLayout) findViewById(R.id.linked_in_layout);
        mySpaceLayout = (LinearLayout) findViewById(R.id.my_space_layout);
        skypeLayout = (LinearLayout) findViewById(R.id.skype_layout);
        twitterLayout = (LinearLayout) findViewById(R.id.twitter_layout);

        fbTextView = (TextView) findViewById(R.id.fb_value);
        linkedInTextView = (TextView) findViewById(R.id.linked_in_value);
        mySpaceTextView = (TextView) findViewById(R.id.my_space_value);
        skypeTextView = (TextView) findViewById(R.id.skype_value);
        twitterTextView = (TextView) findViewById(R.id.twitter_value);

        editSocialInformationImageView = (ImageView) findViewById(R.id.edit_social_info);
        editSocialInformationImageView.setImageDrawable(editDrawable);

        // Personal Information
        booksLayout = (LinearLayout) findViewById(R.id.books_layout);
        tvShowsLayout = (LinearLayout) findViewById(R.id.tv_shows_layout);
        moviesLayout = (LinearLayout) findViewById(R.id.movies_layout);
        quotesLayout = (LinearLayout) findViewById(R.id.quotes_layout);

        booksTextView = (TextView) findViewById(R.id.books_value);
        tvShowsTextView = (TextView) findViewById(R.id.tv_shows_value);
        moviesTextView = (TextView) findViewById(R.id.movies_value);
        quotesTextView = (TextView) findViewById(R.id.quotes_value);

        editPersonalInformationImageView = (ImageView) findViewById(R.id.edit_personal_info);
        editPersonalInformationImageView.setImageDrawable(editDrawable);

        fillValues();
    }


    private void fillValues() {
        // Basic Information
        if (!profile.getNickname().equals("")) {
            nicknameTextView.setText(profile.getNickname());
        } else {
            nicknameLayout.setVisibility(View.GONE);
        }

        if (profile.getDateOfBirth() != null) {// if year is > 20 eg 80 then 1980, else eg 04 then 2004
            birthdayTextView.setText(Actions.getMonthfromIndex(month) + " " + day + ", " + year);
        } else {
            birthdayLayout.setVisibility(View.GONE);
        }

        if (!profile.getPersonalSummary().equals("null")) {
            personalSummaryTextView.setText(Html.fromHtml(profile.getPersonalSummary()));
        } else {
            personalSummaryLayout.setVisibility(View.GONE);
        }

        // Contact Information
        if (!user.getEmail().equals("")) {
            emailTextView.setText(user.getEmail());
        } else {
            emailLayout.setVisibility(View.GONE);
        }

        if (!profile.getHomepage().equals("null")) {
            homePageTextView.setText(profile.getHomepage());
        } else {
            homePageLayout.setVisibility(View.GONE);
        }

        if (!profile.getWorkphone().equals("null")) {
            workPhoneTextView.setText(profile.getWorkphone());
        } else {
            workPhoneLayout.setVisibility(View.GONE);
        }

        if (!profile.getHomephone().equals("null")) {
            homePhoneTextView.setText(profile.getHomephone());
        } else {
            homePhoneLayout.setVisibility(View.GONE);
        }

        if (!profile.getMobilephone().equals("null")) {
            mobilePhoneTextView.setText(profile.getMobilephone());
        } else {
            mobilePhoneLayout.setVisibility(View.GONE);
        }

        if (!profile.getFacsimile().equals("null")) {
            facsimileTextView.setText(profile.getFacsimile());
        } else {
            facsimileLayout.setVisibility(View.GONE);
        }

        // Staff Information
        if (!profile.getPosition().equals("")) {
            positionTextView.setText(profile.getPosition());
        } else {
            positionLayout.setVisibility(View.GONE);
        }

        if (!profile.getDepartment().equals("")) {
            departmentTextView.setText(profile.getDepartment());
        } else {
            departmentLayout.setVisibility(View.GONE);
        }

        if (!profile.getSchool().equals("")) {
            schoolTextView.setText(profile.getSchool());
        } else {
            schoolLayout.setVisibility(View.GONE);
        }

        if (!profile.getRoom().trim().equals("")) {
            roomTextView.setText(profile.getRoom());
        } else {
            roomLayout.setVisibility(View.GONE);
        }

        if (!profile.getStaffProfile().equals("null")) {
            staffProfileTextView.setText(Html.fromHtml(profile.getStaffProfile()));
        } else {
            staffProfileLayout.setVisibility(View.GONE);
        }

        if (!profile.getUniversityProfileUrl().equals("null")) {
            universityUrlTextView.setText(profile.getUniversityProfileUrl());
        } else {
            universityUrlLayout.setVisibility(View.GONE);
        }

        if (!profile.getAcademicProfileUrl().equals("null")) {
            academicUrlTextView.setText(profile.getAcademicProfileUrl());
        } else {
            academicUrlLayout.setVisibility(View.GONE);
        }

        if (!profile.getPublications().equals("null")) {
            publicationsTextView.setText(Html.fromHtml(profile.getPublications()));
        } else {
            publicationsLayout.setVisibility(View.GONE);
        }

        //Student Information
        if (!profile.getCourse().equals("null")) {
            degreeTextView.setText(profile.getCourse());
        } else {
            degreeLayout.setVisibility(View.GONE);
        }

        if (!profile.getSubjects().equals("null")) {
            subjectTextView.setText(profile.getSubjects());
        } else {
            subjectsLayout.setVisibility(View.GONE);
        }

        // Social Networking
        if (!profile.getSocialInfo().getFacebookUrl().equals("")) {
            fbTextView.setText(profile.getSocialInfo().getFacebookUrl());
        } else {
            fbLayout.setVisibility(View.GONE);
        }

        if (!profile.getSocialInfo().getLinkedinUrl().equals("")) {
            linkedInTextView.setText(profile.getSocialInfo().getLinkedinUrl());
        } else {
            linkedInLayout.setVisibility(View.GONE);
        }

        if (!profile.getSocialInfo().getMyspaceUrl().equals("")) {
            mySpaceTextView.setText(profile.getSocialInfo().getMyspaceUrl());
        } else {
            mySpaceLayout.setVisibility(View.GONE);
        }

        if (!profile.getSocialInfo().getSkypeUsername().equals("")) {
            skypeTextView.setText(profile.getSocialInfo().getSkypeUsername());
        } else {
            skypeLayout.setVisibility(View.GONE);
        }

        if (!profile.getSocialInfo().getTwitterUrl().equals("")) {
            twitterTextView.setText(profile.getSocialInfo().getTwitterUrl());
        } else {
            twitterLayout.setVisibility(View.GONE);
        }

        // Personal Information
        if (!profile.getFavouriteBooks().equals("null")) {
            booksTextView.setText(profile.getFavouriteBooks());
        } else {
            booksLayout.setVisibility(View.GONE);
        }

        if (!profile.getFavouriteTvShows().equals("null")) {
            tvShowsTextView.setText(profile.getFavouriteTvShows());
        } else {
            tvShowsLayout.setVisibility(View.GONE);
        }

        if (!profile.getFavouriteMovies().equals("null")) {
            moviesTextView.setText(profile.getFavouriteMovies());
        } else {
            moviesLayout.setVisibility(View.GONE);
        }

        if (!profile.getFavouriteQuotes().equals("null")) {
            quotesTextView.setText(profile.getFavouriteQuotes());
        } else {
            quotesLayout.setVisibility(View.GONE);
        }

        checkVisibilities();
    }

    private void checkVisibilities() {
        // Basic Information
        if (nicknameLayout.getVisibility() == View.GONE && birthdayLayout.getVisibility() == View.GONE && personalSummaryLayout.getVisibility() == View.GONE) {
            findViewById(R.id.no_basic_information).setVisibility(View.VISIBLE);
        }

        // Contact Information
        if (emailLayout.getVisibility() == View.GONE && homePageLayout.getVisibility() == View.GONE && workPhoneLayout.getVisibility() == View.GONE
                && homePhoneLayout.getVisibility() == View.GONE && mobilePhoneLayout.getVisibility() == View.GONE && facsimileLayout.getVisibility() == View.GONE) {
            findViewById(R.id.no_contact_information).setVisibility(View.VISIBLE);
        }

        // Staff Information
        if (positionLayout.getVisibility() == View.GONE && departmentLayout.getVisibility() == View.GONE && schoolLayout.getVisibility() == View.GONE
                && roomLayout.getVisibility() == View.GONE && staffProfileLayout.getVisibility() == View.GONE && universityUrlLayout.getVisibility() == View.GONE
                && academicUrlLayout.getVisibility() == View.GONE && publicationsLayout.getVisibility() == View.GONE) {
            findViewById(R.id.no_staff_information).setVisibility(View.VISIBLE);
        }

        //Student Information
        if (degreeLayout.getVisibility() == View.GONE && subjectsLayout.getVisibility() == View.GONE) {
            findViewById(R.id.no_student_information).setVisibility(View.VISIBLE);
        }

        // Social Networking
        if (fbLayout.getVisibility() == View.GONE && linkedInLayout.getVisibility() == View.GONE && mySpaceLayout.getVisibility() == View.GONE
                && skypeLayout.getVisibility() == View.GONE && twitterLayout.getVisibility() == View.GONE) {
            findViewById(R.id.no_social_information).setVisibility(View.VISIBLE);
        }

        // Personal Information
        if (booksLayout.getVisibility() == View.GONE && tvShowsLayout.getVisibility() == View.GONE && moviesLayout.getVisibility() == View.GONE
                && quotesLayout.getVisibility() == View.GONE) {
            findViewById(R.id.no_personal_information).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (birthdayPicker.getVisibility() == View.VISIBLE)
            scrollView.setScrollable(false);
        else
            scrollView.setScrollable(true);
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.datepicker_imageview:
                if (birthdayPicker.getVisibility() == View.GONE) {
                    birthdayPicker.setVisibility(View.VISIBLE);
                    scrollView.setDatePickerIsVisible(true);
                } else {
                    birthdayPicker.setVisibility(View.GONE);
                    scrollView.setDatePickerIsVisible(false);
                }
                break;
            case R.id.edit_basic_button:
                nicknameTextView.setVisibility(View.GONE);
                nicknameEditText.setVisibility(View.VISIBLE);
                nicknameEditText.setText(profile.getNickname());
                datePickerImageView.setVisibility(View.VISIBLE);
                personalSummaryTextView.setVisibility(View.GONE);
                personalSummaryEditText.setVisibility(View.VISIBLE);
                personalSummaryEditText.setText(profile.getPersonalSummary());
                findViewById(R.id.basic_information_buttons).setVisibility(View.VISIBLE);
                break;
        }
    }
}

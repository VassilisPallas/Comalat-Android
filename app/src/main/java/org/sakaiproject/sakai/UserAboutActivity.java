package org.sakaiproject.sakai;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.general.Connection;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.session.RefreshSession;
import org.sakaiproject.api.session.Waiter;
import org.sakaiproject.customviews.AboutScrollView;
import org.sakaiproject.helpers.user_info_helpers.BasicInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.ContactInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.PersonalInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.SocialNetworkInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.StaffInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.StudentInfoHelper;

public class UserAboutActivity extends AppCompatActivity implements View.OnClickListener {

    private Waiter waiter;  //Thread which controls idle time
    private Connection connection = Connection.getInstance();

    private AboutScrollView scrollView;

    private BasicInfoHelper basicInfo;
    private ContactInfoHelper contactInfoHelper;
    private StaffInfoHelper staffInfo;
    private StudentInfoHelper studentInfo;
    private SocialNetworkInfoHelper socialNetworkInfo;
    private PersonalInfoHelper personalInfo;

    private Drawable editDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_about);

        editDrawable = ActionsHelper.setCustomDrawableColor(this, R.mipmap.ic_create, Color.parseColor("#29A031"));

        scrollView = (AboutScrollView) findViewById(R.id.about_scrollview);

        basicInfo = new BasicInfoHelper(this, editDrawable, this);

        contactInfoHelper = new ContactInfoHelper(this, editDrawable, this);

        staffInfo = new StaffInfoHelper(this, editDrawable, this);

        studentInfo = new StudentInfoHelper(this, editDrawable, this);

        socialNetworkInfo = new SocialNetworkInfoHelper(this, editDrawable, this);

        personalInfo = new PersonalInfoHelper(this, editDrawable, this);


        /* if device is connected on the internet that means that the user made login with internet connection,
           so the thread for the idle mode will start
        */
        if (NetWork.getConnectionEstablished()) {
            waiter = Waiter.getInstance();
            waiter.stop = false;
            waiter.setActivity(UserAboutActivity.class);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (basicInfo.getBirthdayPicker().getVisibility() == View.VISIBLE)
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
                if (basicInfo.getBirthdayPicker().getVisibility() == View.GONE) {
                    basicInfo.getBirthdayPicker().setVisibility(View.VISIBLE);
                    scrollView.setDatePickerIsVisible(true);
                } else {
                    basicInfo.getBirthdayPicker().setVisibility(View.GONE);
                    scrollView.setDatePickerIsVisible(false);
                }
                break;
            case R.id.edit_basic_button:
                basicInfo.enableEdit();
                break;
            case R.id.edit_contact_button:
                contactInfoHelper.enableEdit();
                findViewById(R.id.contact_information_buttons).setVisibility(View.VISIBLE);
                break;
            case R.id.edit_staff_button:
                staffInfo.enableEdit();
                findViewById(R.id.staff_information_buttons).setVisibility(View.VISIBLE);
                break;
            case R.id.edit_student_button:
                studentInfo.enableEdit();
                findViewById(R.id.student_information_buttons).setVisibility(View.VISIBLE);
                break;
            case R.id.edit_social_button:
                socialNetworkInfo.enableEdit();
                findViewById(R.id.social_network_information_buttons).setVisibility(View.VISIBLE);
                break;
            case R.id.edit_personal_button:
                personalInfo.enableEdit();
                findViewById(R.id.personal_information_buttons).setVisibility(View.VISIBLE);
                break;
            case R.id.basic_info_save_changes_button:
                basicInfo.saveEdit();
                break;
            case R.id.basic_info_cancel_button:
                basicInfo.cancelEdit();
                break;
        }
    }
}

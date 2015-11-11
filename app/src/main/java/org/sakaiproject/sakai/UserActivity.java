package org.sakaiproject.sakai;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sakaiproject.api.events.OnlineEvents;
import org.sakaiproject.api.general.SystemNotifications;
import org.sakaiproject.customviews.ImageViewRounded;
import org.sakaiproject.api.general.Actions;
import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.logout.Logout;
import org.sakaiproject.api.session.RefreshSession;
import org.sakaiproject.api.session.Waiter;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.api.user.User;

import java.io.FileNotFoundException;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView displayNameTextView, emailTextView, sessionMessageTextView;
    private RelativeLayout sessionMessageRelative;
    private Button keepSessionButton;
    private ImageViewRounded userImage;
    private Profile profile;
    private User user;
    private Waiter waiter;  //Thread which controls idle time
    private Connection connection = Connection.getInstance();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findViewsById();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.red, R.color.blue, R.color.orange);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        /* if device is connected on the internet that means that the user made login with internet connection,
           so the thread for the idle mode will start
        */
        if (NetWork.getConnectionEstablished()) {
            waiter = Waiter.getInstance();
            waiter.stop = false;
            waiter.setActivity(this);
            waiter.setContext(this);
            waiter.setMessageRelativeLayout(sessionMessageRelative);
            waiter.setMessageTextView(sessionMessageTextView);
            waiter.setPeriod(Connection.getMaxInactiveInterval() * 1000); // 1800 milliseconds * 1000 = 30 mins
            if (waiter.getState() == Thread.State.NEW)
                waiter.start();

            // if the message for the session id is visible
            if (waiter.isMessageVisible()) {
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

        profile = Profile.getInstance();
        user = User.getInstance();
        displayNameTextView.setText(profile.getDisplayName());
        emailTextView.setText(user.getEmail());
        try {
            userImage.setImageBitmap(Actions.getImage(this, "user_thumbnail_image"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        keepSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSession();
            }
        });

    }


    private void refreshContent() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if (NetWork.getConnectionEstablished()) {
                    new Refresh().execute();
                } else {
                    Snackbar.make(root, "No internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Can't sync", null).show();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void updateSession() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sessionMessageRelative.setVisibility(View.GONE);
                            }
                        });
                        RefreshSession refreshSession = new RefreshSession(getApplicationContext());
                        refreshSession.putSession(getResources().getString(R.string.url) + "session/" + connection.getSessionId() + ".json");
                        waiter.setCount(30);
                        waiter.stop = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void findViewsById() {
        displayNameTextView = (TextView) findViewById(R.id.user_display_name);
        emailTextView = (TextView) findViewById(R.id.user_email);
        userImage = (ImageViewRounded) findViewById(R.id.user_image);
        sessionMessageTextView = (TextView) findViewById(R.id.session_message_textview);
        sessionMessageRelative = (RelativeLayout) findViewById(R.id.session_message);
        keepSessionButton = (Button) findViewById(R.id.keep_session_button);

        mSwipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        root = (RelativeLayout)findViewById(R.id.user_root);
    }

    private void logout() {

        AlertDialog.Builder adb = new AlertDialog.Builder(getSupportActionBar().getThemedContext());

        adb.setTitle(getResources().getString(R.string.logout_message));

        adb.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if (NetWork.getConnectionEstablished()) {
                            waiter.stop = true;
                            try {
                                Logout logout = new Logout(getApplicationContext());
                                if (logout.logout("http://141.99.248.86:8089/direct/session/" + Connection.getSessionId()) == 1) {

                                    User.nullInstance();
                                    Profile.nullInstance();
                                    Connection.nullSessionId();

                                    Intent i = new Intent(getApplication(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            User.nullInstance();
                            Profile.nullInstance();
                            Connection.nullSessionId();

                            Intent i = new Intent(getApplication(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });

                thread.start();

            }
        });


        adb.setNegativeButton(getResources().getString(R.string.cancel), null);

        Dialog d = adb.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            logout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectFragment(Fragment f, int id, String title) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(id, f);
        fragmentTransaction.commit();
        setTitle(title);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.dashboard:
                break;
            case R.id.home:
                break;
            case R.id.profile:
                break;
            case R.id.membership:
                break;
            case R.id.schedule:
                ScheduleFragment scheduleFragment = new ScheduleFragment().setSelectedEvent(mSwipeRefreshLayout);
                selectFragment(scheduleFragment, R.id.user_frame, "Schedule");
                break;
            case R.id.resources:
                break;
            case R.id.announcements:
                break;
            case R.id.worksite_setup:
                break;
            case R.id.preferences:
                break;
            case R.id.account:
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.user_help:
                WebViewFragment webViewFragment = new WebViewFragment();
                selectFragment(webViewFragment, R.id.user_frame, "Help");
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            SystemNotifications.cancel(0);

            //mSwipeRefreshLayout.setRefreshing(true);
        }
        Log.i("visible", "true");
    }

    public class Refresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            OnlineEvents onlineEvents = new OnlineEvents(getApplicationContext());
            String url = getResources().getString(R.string.url) + "calendar/my.json";
            onlineEvents.getEvents(url);

            return null;
        }
    }

}

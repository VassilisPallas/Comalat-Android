package org.sakaiproject.sakai;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sakaiproject.api.memberships.OfflineMemberships;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.session.IdleModeCheckService;
import org.sakaiproject.api.sync.Refresh;
import org.sakaiproject.customviews.ImageViewRounded;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.general.Connection;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.session.RefreshSession;
import org.sakaiproject.api.session.Waiter;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.SitesNavigationDrawerHelper;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.UserMainNavigationDrawerHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ISwipeRefresh {

    private TextView displayNameTextView, emailTextView;
    private ImageViewRounded userImage;
    private Waiter waiter;  //Thread which controls idle time
    private Connection connection = Connection.getInstance();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout root;
    private FrameLayout searchFrame;

    private UserMainNavigationDrawerHelper mainNavigationDrawer;
    private static SitesNavigationDrawerHelper sitesNavigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewsById();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.red, R.color.blue, R.color.orange);

        mSwipeRefreshLayout.setRefreshing(false);

        mainNavigationDrawer = new UserMainNavigationDrawerHelper(this, mSwipeRefreshLayout, toolbar, this);

        sitesNavigationDrawer = new SitesNavigationDrawerHelper(this, mSwipeRefreshLayout, toolbar, this);

        /* if device is connected on the internet that means that the user made login with internet connection,
           so the thread for the idle mode will start
        */
        if (NetWork.getConnectionEstablished()) {
            Intent idleService = new Intent(this, IdleModeCheckService.class);

            waiter = Waiter.getInstance();
            waiter.stop = false;
            waiter.setActivity(UserActivity.class);
            waiter.setContext(this);
            waiter.setPeriod(Connection.getMaxInactiveInterval() * 1000); // 1800 milliseconds * 1000 = 30 mins
            if (waiter.getState() == Thread.State.NEW)
                waiter.start();

            // if the message for the session id is visible
            if (waiter.isNotificationShowed()) {
                connection = Connection.getInstance();
                connection.setContext(getApplicationContext());
                // if the session has expired, the session id will be null, so the user will go to MainActivity
                if (Connection.getSessionId() == null) {
                    waiter.stop = true;
                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("session_expired", true);
                    startActivity(i);
                } else
                    updateSession();
            }
        }

        displayNameTextView.setText(Profile.getDisplayName());
        emailTextView.setText(User.getEmail());
        try {
            if (ActionsHelper.createDirIfNotExists(this, User.getUserEid() + File.separator + "user"))
                userImage.setImageBitmap(ActionsHelper.getImage(this, "user_thumbnail_image", User.getUserEid() + File.separator + "user"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        OfflineMemberships offlineSites = new OfflineMemberships(this);
        try {
            offlineSites.getSites();
            sitesNavigationDrawer.fillSitesDrawer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeMainMenu();
    }

    private void initializeMainMenu() {
        mainNavigationDrawer.createDrawer(getResources().getString(R.string.my_workspace), null);
        NavigationDrawerHelper.setSelectedSite(getResources().getString(R.string.my_workspace));
    }

    public static SitesNavigationDrawerHelper getSitesNavigationDrawer() {
        return sitesNavigationDrawer;
    }

    private void refreshContent() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if (NetWork.getConnectionEstablished()) {
                    Refresh refresh = new Refresh(getApplicationContext());
                    refresh.setSwipeRefreshLayout(mSwipeRefreshLayout);
                    refresh.execute();

                } else {
                    Snackbar.make(root, getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getText(R.string.can_not_sync), null).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    public void updateSession() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {
                    try {
                        RefreshSession refreshSession = new RefreshSession(getApplicationContext());
                        refreshSession.putSession(getResources().getString(R.string.url) + "session/" + Connection.getSessionId() + ".json");
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
        mSwipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        root = (RelativeLayout) findViewById(R.id.user_root);
    }

    @Override
    public void onBackPressed() {
        if (NavigationDrawerHelper.getDrawer().isDrawerOpen(GravityCompat.START)) {
            NavigationDrawerHelper.getDrawer().closeDrawer(GravityCompat.START);
        } else if (NavigationDrawerHelper.getDrawer().isDrawerOpen(GravityCompat.END)) {
            NavigationDrawerHelper.getDrawer().closeDrawer(GravityCompat.END);
        } else {
            ActionsHelper.logout(this, waiter);
        }
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

            //mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

//        sitesNavigationDrawer.unCheckFirstItem();
//        mainNavigationDrawer.unCheckFirstItem();

        // left drawer
        switch (item.getItemId()) {
            case R.id.logout:
                ActionsHelper.logout(this, waiter);
                break;
            case R.id.my_workspace:
                initializeMainMenu();
                break;
            case R.id.help:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", getResources().getString(R.string.help_url)));
                break;
        }

        for (Integer ids : NavigationDrawerHelper.getMyWorkspaceIds().keySet()) {
            if (item.getItemId() == ids) {
                NavigationDrawerHelper.doAction(NavigationDrawerHelper.getMyWorkspaceIds().get(ids));
            }
        }

        for (Integer ids : NavigationDrawerHelper.getSitesIds().keySet()) {
            if (item.getItemId() == ids) {
                NavigationDrawerHelper.setSelectedSite(NavigationDrawerHelper.getSitesIds().get(ids).getTitle());
                NavigationDrawerHelper.setSelectedSiteData(NavigationDrawerHelper.getSitesIds().get(ids));
                mainNavigationDrawer.createDrawer(NavigationDrawerHelper.getSitesIds().get(ids).getTitle(), NavigationDrawerHelper.getSitesIds().get(ids).getPages());
            }
        }

        for (Integer ids : NavigationDrawerHelper.getPagesIds().keySet()) {
            if (item.getItemId() == ids) {
                NavigationDrawerHelper.doAction(NavigationDrawerHelper.getPagesIds().get(ids).getTitle());
            }
        }

        if (NavigationDrawerHelper.getDrawer().isDrawerOpen(GravityCompat.START))
            NavigationDrawerHelper.getDrawer().closeDrawer(GravityCompat.START);
        else if (NavigationDrawerHelper.getDrawer().isDrawerOpen(GravityCompat.END))
            NavigationDrawerHelper.getDrawer().closeDrawer(GravityCompat.END);

        return true;
    }


    @Override
    public void Callback(SwipeRefreshLayout.OnRefreshListener listener) {
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        SiteData.getSites().clear();
        SiteData.getProjects().clear();
        UserMainNavigationDrawerHelper.getMyWorkSpaceItems().clear();
    }

}
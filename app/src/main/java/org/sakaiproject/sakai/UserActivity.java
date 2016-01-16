package org.sakaiproject.sakai;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.sakaiproject.api.site.OfflineSite;
import org.sakaiproject.api.sync.Refresh;
import org.sakaiproject.customviews.ImageViewRounded;
import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Connection;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.logout.Logout;
import org.sakaiproject.api.session.RefreshSession;
import org.sakaiproject.api.session.Waiter;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.api.user.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView displayNameTextView, emailTextView;
    private ImageViewRounded userImage;
    private Profile profile;
    private User user;
    private Waiter waiter;  //Thread which controls idle time
    private Connection connection = Connection.getInstance();
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout root;
    private static NavigationView sitesNavigationView;
    private SearchView sitesSearchView;

    public static Map<Integer, String> sitesIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerSlide(drawerView, 0);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        sitesNavigationView = (NavigationView) findViewById(R.id.site_nav_view);

        createDrawer(navigationView.getMenu());

        navigationView.setNavigationItemSelectedListener(this);
        sitesNavigationView.setNavigationItemSelectedListener(this);

        findViewsById();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.red, R.color.blue, R.color.orange);

        mSwipeRefreshLayout.setRefreshing(false);

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

        sitesIds = new Hashtable<>();

        OfflineSite offlineSites = new OfflineSite(this);
        try {
            offlineSites.getSites();
            Actions.fillSitesDrawer(sitesNavigationView.getMenu());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Menu getSiteMenu() {
        return sitesNavigationView.getMenu();
    }

    private static List<String> myWorkSpaceItems = new ArrayList<>();

    static {
        myWorkSpaceItems.add("Dashboard");
        myWorkSpaceItems.add("Home");
        myWorkSpaceItems.add("Profile");
        myWorkSpaceItems.add("Membership");
        myWorkSpaceItems.add("Calendar");
        myWorkSpaceItems.add("Resources");
        myWorkSpaceItems.add("Announcements");
        myWorkSpaceItems.add("Worksite Setup");
        myWorkSpaceItems.add("Preferences");
        myWorkSpaceItems.add("Account");
    }

    public void createDrawer(Menu navigationMenu) {

        MenuItem mainItem = navigationMenu.findItem(R.id.main_item);
        SubMenu subMenu = mainItem.getSubMenu();

        // make visible the items which are on the My Workspace
        for (int i = 0; i < subMenu.size(); i++) {
            for (String item : myWorkSpaceItems) {
                if (subMenu.getItem(i).getTitle().equals(item)) {
                    subMenu.getItem(i).setVisible(true);
                }
            }
        }

        // add new item to menu
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            subMenu.add(R.id.main_group, Menu.NONE, Menu.NONE, "title").setIcon(getResources().getDrawable(R.mipmap.ic_check_circle, getTheme())).setVisible(true);
        } else {
            subMenu.add(R.id.main_group, Menu.NONE, Menu.NONE, "title").setIcon(getResources().getDrawable(R.mipmap.ic_check_circle)).setVisible(true);
        }
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
                    Snackbar.make(root, "No internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Can't sync", null).show();
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

    public void findViewsById() {
        displayNameTextView = (TextView) findViewById(R.id.user_display_name);
        emailTextView = (TextView) findViewById(R.id.user_email);
        userImage = (ImageViewRounded) findViewById(R.id.user_image);
        mSwipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        root = (RelativeLayout) findViewById(R.id.user_root);

        sitesSearchView = (SearchView) findViewById(R.id.site_search_drawer);

        final EditText e = (EditText) sitesSearchView.findViewById(sitesSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        e.setHintTextColor(Color.parseColor("#808080"));

        findViewById(R.id.sites_drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open sites drawer
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
            }
        });

        e.addTextChangedListener(new TextWatcher() {
            MenuItem sitesItem = sitesNavigationView.getMenu().findItem(R.id.sites);
            SubMenu sitesSubMenu = sitesItem.getSubMenu();


            MenuItem projectsItem = sitesNavigationView.getMenu().findItem(R.id.projects);
            SubMenu projectsSubMenu = projectsItem.getSubMenu();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int c) {

                if (e.getText().toString().length() == 0) {
                    for (int i = 0; i < sitesSubMenu.size(); i++) {
                        sitesSubMenu.getItem(i).setVisible(true);
                    }

                    for (int i = 0; i < projectsSubMenu.size(); i++) {
                        projectsSubMenu.getItem(i).setVisible(true);

                    }
                } else {
                    for (int i = 0; i < sitesSubMenu.size(); i++) {
                        if (!sitesSubMenu.getItem(i).getTitle().toString().toLowerCase().contains(s.toString().toLowerCase())) {
                            sitesSubMenu.getItem(i).setVisible(false);
                        } else {
                            sitesSubMenu.getItem(i).setVisible(true);
                        }
                    }

                    for (int i = 0; i < projectsSubMenu.size(); i++) {
                        if (!projectsSubMenu.getItem(i).getTitle().toString().toLowerCase().contains(s.toString().toLowerCase())) {
                            projectsSubMenu.getItem(i).setVisible(false);
                        } else {
                            projectsSubMenu.getItem(i).setVisible(true);
                        }
                    }
                }

                for (int i = 0, count = sitesNavigationView.getChildCount(); i < count; i++) {
                    final View child = sitesNavigationView.getChildAt(i);
                    if (child != null && child instanceof ListView) {
                        final ListView menuView = (ListView) child;
                        final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                        final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                        wrapped.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            logout();
        }
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

        // left drawer
        switch (item.getItemId()) {
            case R.id.dashboard:
                break;
            case R.id.home:
                break;
            case R.id.profile:
                ProfileFragment profileFragment = new ProfileFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
                selectFragment(profileFragment, R.id.user_frame, Profile.getDisplayName());
                break;
            case R.id.membership:
                break;
            case R.id.calendar:
                CalendarFragment calendarFragment = new CalendarFragment().setSelectedEvent(mSwipeRefreshLayout);
                selectFragment(calendarFragment, R.id.user_frame, "Calendar");
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


        for (Integer ids : sitesIds.keySet())
            if (item.getItemId() == ids) {
                Toast.makeText(UserActivity.this, "Pressed " + sitesIds.get(ids), Toast.LENGTH_SHORT).show();
            }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (drawer.isDrawerOpen(GravityCompat.END))
            drawer.closeDrawer(GravityCompat.END);

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

            //mSwipeRefreshLayout.setRefreshing(true);
        }
        Log.i("visible", "true");
    }

}

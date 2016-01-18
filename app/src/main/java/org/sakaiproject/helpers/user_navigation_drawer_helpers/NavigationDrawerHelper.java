package org.sakaiproject.helpers.user_navigation_drawer_helpers;


import android.content.Context;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import org.sakaiproject.api.session.Waiter;
import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.api.site.SitePage;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Actions;
import org.sakaiproject.sakai.CalendarFragment;
import org.sakaiproject.sakai.MembershipFragment;
import org.sakaiproject.sakai.ProfileFragment;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.UserActivity;
import org.sakaiproject.sakai.WebViewFragment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by vasilis on 1/17/16.
 */
public abstract class NavigationDrawerHelper {

    protected static int id = -1;
    protected static Map<Integer, SiteData> sitesIds;
    protected static Map<Integer, SitePage> pagesIds;
    protected static Map<Integer, String> myWorkspaceIds;
    private static Context context;
    private static CustomSwipeRefreshLayout mSwipeRefreshLayout;
    protected static DrawerLayout drawer;

    NavigationDrawerHelper(Context context, CustomSwipeRefreshLayout mSwipeRefreshLayout) {
        this.context = context;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        sitesIds = new Hashtable<>();
        pagesIds = new Hashtable<>();
        myWorkspaceIds = new Hashtable<>();

        Toolbar toolbar = (Toolbar) ((AppCompatActivity) context).findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);

        drawer = (DrawerLayout) ((AppCompatActivity) context).findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                ((AppCompatActivity) context), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

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
    }

    public static Map<Integer, String> getMyWorkspaceIds() {
        return myWorkspaceIds;
    }

    public static Map<Integer, SitePage> getPagesIds() {
        return pagesIds;
    }

    public static Map<Integer, SiteData> getSitesIds() {
        return sitesIds;
    }

    public static DrawerLayout getDrawer() {
        return drawer;
    }

    protected final int findIcon(String pageName) {
        switch (pageName) {
            case "Dashboard":
                return R.mipmap.ic_dashboard;
            case "Home":
                return R.mipmap.ic_home;
            case "Profile":
                return R.mipmap.ic_person;
            case "Membership":
                return R.mipmap.ic_group;
            case "Calendar":
                return R.mipmap.ic_today;
            case "Resources":
                return R.mipmap.ic_folder;
            case "Announcements":
                return R.mipmap.ic_announcement;
            case "Preferences":
                return R.mipmap.ic_settings;
            case "Account":
                return R.mipmap.ic_person;
            case "Assignments":
                return R.mipmap.ic_assignment;
            case "Chat Room":
                return R.mipmap.ic_chat_room;
            case "Contact Us":
                return R.mipmap.ic_email;
            case "Drop Box":
                return R.mipmap.ic_dropbox;
            case "Email":
                return R.mipmap.ic_email;
            case "Email Archive":
                return R.mipmap.ic_email;
            case "External Tool":
                return R.mipmap.ic_public;
            case "Forums":
                return R.mipmap.ic_forum;
            case "Gradebook":
                return R.mipmap.ic_gradebook;
            case "Lessons":
                return R.mipmap.ic_lesson;
            case "Messages":
                return R.mipmap.ic_messages;
            case "News":
                return R.mipmap.ic_news;
            case "Podcasts":
                return R.mipmap.ic_podcasts;
            case "Polls":
                return R.mipmap.ic_polls;
            case "PostEm":
                return R.mipmap.ic_post_em;
            case "Roster":
                return R.mipmap.ic_group;
            case "Search":
                return R.mipmap.ic_search;
            case "Section Info":
                return R.mipmap.ic_person;
            case "Sign-up":
                return R.mipmap.ic_today;
            case "Site Info":
                return R.mipmap.ic_settings;
            case "Statistics":
                return R.mipmap.ic_statistics;
            case "Syllabus":
                return R.mipmap.ic_syllabus;
            case "Tests & Quizzes":
                return R.mipmap.ic_test_quiz;
            case "Wiki":
                return R.mipmap.ic_wiki;
            case "Web Content":
                return R.mipmap.ic_public;
            default:
                return R.mipmap.ic_lesson;
        }
    }

    public static final void doAction(String pageName) {
        switch (pageName) {
            case "Dashboard":
                break;
            case "Home":
                break;
            case "Profile":
                ProfileFragment profileFragment = new ProfileFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
                Actions.selectFragment(profileFragment, R.id.user_frame, Profile.getDisplayName(), context);
                break;
            case "Membership":
                MembershipFragment membershipFragment = new MembershipFragment();
                Actions.selectFragment(membershipFragment, R.id.user_frame, "Membership", context);
                break;
            case "Calendar":
                CalendarFragment calendarFragment = new CalendarFragment().setSelectedEvent(mSwipeRefreshLayout);
                Actions.selectFragment(calendarFragment, R.id.user_frame, "Calendar", context);
                break;
            case "Resources":
                break;
            case "Announcements":
                break;
            case "Preferences":
                break;
            case "Account":
                break;
            case "Assignments":
                break;
            case "Chat Room":
                break;
            case "Contact Us":
                break;
            case "Drop Box":
                break;
            case "Email":
                break;
            case "Email Archive":
                break;
            case "External Tool":
                break;
            case "Forums":
                break;
            case "Gradebook":
                break;
            case "Lessons":
                break;
            case "Messages":
                break;
            case "News":
                break;
            case "Podcasts":
                break;
            case "Polls":
                break;
            case "PostEm":
                break;
            case "Roster":
                break;
            case "Search":
                break;
            case "Section Info":
                break;
            case "Sign-up":
                break;
            case "Site Info":
                break;
            case "Statistics":
                break;
            case "Syllabus":
                break;
            case "Tests & Quizzes":
                break;
            case "Wiki":
                break;
            case "Web Content":
                break;
            default:
                break;
        }
    }
}
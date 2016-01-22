package org.sakaiproject.helpers.user_navigation_drawer_helpers;


import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.api.site.SitePage;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.general.Actions;
import org.sakaiproject.sakai.CalendarFragment;
import org.sakaiproject.sakai.MembershipFragment;
import org.sakaiproject.sakai.ProfileFragment;
import org.sakaiproject.sakai.R;

import java.util.Hashtable;
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

    private static String DASHBOARD;
    private static String HOME;
    private static String PROFILE;
    private static String MEMBERSHIP;
    private static String CALENDAR;
    private static String RESOURCES;
    private static String ANNOUNCEMENTS;
    private static String PREFERENCES;
    private static String ACCOUNT;
    private static String ASSINGMENTS;
    private static String CHAT_ROOM;
    private static String CONTACT_US;
    private static String DROPBOX;
    private static String EMAIL;
    private static String EMAIL_ARCHIVE;
    private static String EXTERNAL_TOOL;
    private static String FORUMS;
    private static String GRADEBOOK;
    private static String LESSONS;
    private static String MESSAGES;
    private static String NEWS;
    private static String PODCASTS;
    private static String POLLS;
    private static String POSTEM;
    private static String ROSTER;
    private static String SEARCH;
    private static String SECTION_INFO;
    private static String SIGN_UP;
    private static String SITE_INFO;
    private static String STATISTICS;
    private static String SYLLABUS;
    private static String TESTS_QUIZZES;
    private static String WIKI;
    private static String WEB_CONTENT;

    NavigationDrawerHelper(Context context, CustomSwipeRefreshLayout mSwipeRefreshLayout) {
        this.context = context;

        DASHBOARD = context.getString(R.string.dashboard);
        HOME = context.getString(R.string.home);
        PROFILE = context.getString(R.string.profile);
        MEMBERSHIP = context.getString(R.string.membership);
        CALENDAR = context.getString(R.string.calendar);
        RESOURCES = context.getString(R.string.resources);
        ANNOUNCEMENTS = context.getString(R.string.announcements);
        PREFERENCES = context.getString(R.string.preferences);
        ACCOUNT = context.getString(R.string.account);
        ASSINGMENTS = context.getString(R.string.assignments);
        CHAT_ROOM = context.getString(R.string.char_room);
        CONTACT_US = context.getString(R.string.contact_us);
        DROPBOX = context.getString(R.string.dropbox);
        EMAIL = context.getString(R.string.email);
        EMAIL_ARCHIVE = context.getString(R.string.email_archive);
        EXTERNAL_TOOL = context.getString(R.string.external_tool);
        FORUMS = context.getString(R.string.forum);
        LESSONS = context.getString(R.string.lessons);
        GRADEBOOK = context.getString(R.string.gradebook);
        MESSAGES = context.getString(R.string.messages);
        NEWS = context.getString(R.string.news);
        PODCASTS = context.getString(R.string.podcasts);
        POLLS = context.getString(R.string.polls);
        POSTEM = context.getString(R.string.post_em);
        ROSTER = context.getString(R.string.roster);
        SEARCH = context.getString(R.string.search);
        SECTION_INFO = context.getString(R.string.section_info);
        SIGN_UP = context.getString(R.string.sign_up);
        SITE_INFO = context.getString(R.string.site_info);
        STATISTICS = context.getString(R.string.statistics);
        SYLLABUS = context.getString(R.string.syllabus);
        TESTS_QUIZZES = context.getString(R.string.test_quizzes);
        WIKI = context.getString(R.string.wiki);
        WEB_CONTENT = context.getString(R.string.web_content);


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

    /**
     * find the icon based on the menu page name
     *
     * @param pageName the page name
     * @return the id of the icon
     */
    protected final int findIcon(String pageName) {
        if (pageName.equals(DASHBOARD)) {
            return R.mipmap.ic_dashboard;
        } else if (pageName.equals(HOME)) {
            return R.mipmap.ic_home;
        } else if (pageName.equals(PROFILE)) {
            return R.mipmap.ic_person;
        } else if (pageName.equals(MEMBERSHIP)) {
            return R.mipmap.ic_group;
        } else if (pageName.equals(CALENDAR)) {
            return R.mipmap.ic_today;
        } else if (pageName.equals(RESOURCES)) {
            return R.mipmap.ic_folder;
        } else if (pageName.equals(ANNOUNCEMENTS)) {
            return R.mipmap.ic_announcement;
        } else if (pageName.equals(PREFERENCES)) {
            return R.mipmap.ic_settings;
        } else if (pageName.equals(ACCOUNT)) {
            return R.mipmap.ic_person;
        } else if (pageName.equals(ASSINGMENTS)) {
            return R.mipmap.ic_assignment;
        } else if (pageName.equals(CHAT_ROOM)) {
            return R.mipmap.ic_chat_room;
        } else if (pageName.equals(CONTACT_US)) {
            return R.mipmap.ic_email;
        } else if (pageName.equals(DROPBOX)) {
            return R.mipmap.ic_dropbox;
        } else if (pageName.equals(EMAIL)) {
            return R.mipmap.ic_email;
        } else if (pageName.equals(EMAIL_ARCHIVE)) {
            return R.mipmap.ic_email;
        } else if (pageName.equals(EXTERNAL_TOOL)) {
            return R.mipmap.ic_public;
        } else if (pageName.equals(FORUMS)) {
            return R.mipmap.ic_forum;
        } else if (pageName.equals(GRADEBOOK)) {
            return R.mipmap.ic_gradebook;
        } else if (pageName.equals(LESSONS)) {
            return R.mipmap.ic_lesson;
        } else if (pageName.equals(MESSAGES)) {
            return R.mipmap.ic_messages;
        } else if (pageName.equals(NEWS)) {
            return R.mipmap.ic_news;
        } else if (pageName.equals(PODCASTS)) {
            return R.mipmap.ic_podcasts;
        } else if (pageName.equals(POLLS)) {
            return R.mipmap.ic_polls;
        } else if (pageName.equals(POSTEM)) {
            return R.mipmap.ic_post_em;
        } else if (pageName.equals(ROSTER)) {
            return R.mipmap.ic_group;
        } else if (pageName.equals(SEARCH)) {
            return R.mipmap.ic_search;
        } else if (pageName.equals(SECTION_INFO)) {
            return R.mipmap.ic_person;
        } else if (pageName.equals(SIGN_UP)) {
            return R.mipmap.ic_today;
        } else if (pageName.equals(SITE_INFO)) {
            return R.mipmap.ic_settings;
        } else if (pageName.equals(STATISTICS)) {
            return R.mipmap.ic_statistics;
        } else if (pageName.equals(SYLLABUS)) {
            return R.mipmap.ic_syllabus;
        } else if (pageName.equals(TESTS_QUIZZES)) {
            return R.mipmap.ic_test_quiz;
        } else if (pageName.equals(WIKI)) {
            return R.mipmap.ic_wiki;
        } else if (pageName.equals(WEB_CONTENT)) {
            return R.mipmap.ic_public;
        } else {
            return R.mipmap.ic_lesson;
        }
    }

    /**
     * the onClick action based on the clicked page
     *
     * @param pageName the page name
     */
    public static void doAction(String pageName) {
        if (pageName.equals(DASHBOARD)) {

        } else if (pageName.equals(HOME)) {

        } else if (pageName.equals(PROFILE)) {
            ProfileFragment profileFragment = new ProfileFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            Actions.selectFragment(profileFragment, R.id.user_frame, Profile.getDisplayName(), context);
        } else if (pageName.equals(MEMBERSHIP)) {
            MembershipFragment membershipFragment = new MembershipFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            Actions.selectFragment(membershipFragment, R.id.user_frame, "Membership", context);
        } else if (pageName.equals(CALENDAR)) {
            CalendarFragment calendarFragment = new CalendarFragment().setSelectedEvent(mSwipeRefreshLayout);
            Actions.selectFragment(calendarFragment, R.id.user_frame, "Calendar", context);
        } else if (pageName.equals(RESOURCES)) {

        } else if (pageName.equals(ANNOUNCEMENTS)) {

        } else if (pageName.equals(PREFERENCES)) {

        } else if (pageName.equals(ACCOUNT)) {

        } else if (pageName.equals(ASSINGMENTS)) {

        } else if (pageName.equals(CHAT_ROOM)) {

        } else if (pageName.equals(CONTACT_US)) {

        } else if (pageName.equals(DROPBOX)) {

        } else if (pageName.equals(EMAIL)) {

        } else if (pageName.equals(EMAIL_ARCHIVE)) {

        } else if (pageName.equals(EXTERNAL_TOOL)) {

        } else if (pageName.equals(FORUMS)) {

        } else if (pageName.equals(GRADEBOOK)) {

        } else if (pageName.equals(LESSONS)) {

        } else if (pageName.equals(MESSAGES)) {

        } else if (pageName.equals(NEWS)) {

        } else if (pageName.equals(PODCASTS)) {

        } else if (pageName.equals(POLLS)) {

        } else if (pageName.equals(POSTEM)) {

        } else if (pageName.equals(ROSTER)) {

        } else if (pageName.equals(SEARCH)) {

        } else if (pageName.equals(SECTION_INFO)) {

        } else if (pageName.equals(SIGN_UP)) {

        } else if (pageName.equals(SITE_INFO)) {

        } else if (pageName.equals(STATISTICS)) {

        } else if (pageName.equals(SYLLABUS)) {

        } else if (pageName.equals(TESTS_QUIZZES)) {

        } else if (pageName.equals(WIKI)) {

        } else if (pageName.equals(WEB_CONTENT)) {

        } else {

        }
    }
}
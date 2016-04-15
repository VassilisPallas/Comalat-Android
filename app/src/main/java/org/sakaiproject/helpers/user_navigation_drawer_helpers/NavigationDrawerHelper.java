package org.sakaiproject.helpers.user_navigation_drawer_helpers;


import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.membership.SitePage;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.AccountFragment;
import org.sakaiproject.sakai.AnnouncementFragment;
import org.sakaiproject.sakai.AssignmentFragment;
import org.sakaiproject.sakai.CalendarFragment;
import org.sakaiproject.sakai.DashboardFragment;
import org.sakaiproject.sakai.DropboxFragment;
import org.sakaiproject.sakai.HomeFragment;
import org.sakaiproject.sakai.MembershipFragment;
import org.sakaiproject.sakai.ProfileFragment;
import org.sakaiproject.sakai.R;
import org.sakaiproject.sakai.RosterFragment;
import org.sakaiproject.sakai.SyllabusFragment;
import org.sakaiproject.sakai.WebContentFragment;
import org.sakaiproject.sakai.WikiFragment;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by vasilis on 1/17/16.
 */
public abstract class NavigationDrawerHelper {

    protected static int id = -1;
    protected static Map<Integer, SiteData> sitesIds;
    protected static Map<Integer, SitePage> pagesIds;
    private static Context context;
    protected static CustomSwipeRefreshLayout mSwipeRefreshLayout;
    public static DrawerLayout drawer;
    private static SiteData selectedSiteData;
    private static String selectedSite;

    private static String DASHBOARD;
    private static String DELEGATED_ACCESS;
    private static String HOME;
    private static String PROFILE;
    private static String MEMBERSHIP;
    private static String CALENDAR;
    private static String RESOURCES;
    private static String ANNOUNCEMENTS;
    private static String PREFERENCES;
    private static String ACCOUNT;
    private static String ASSIGNMENTS;
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

    NavigationDrawerHelper(Context context, CustomSwipeRefreshLayout mSwipeRefreshLayout, Toolbar toolbar) {
        this.context = context;

        DASHBOARD = context.getString(R.string.dashboard);
        DELEGATED_ACCESS = context.getString(R.string.delegated_access);
        HOME = context.getString(R.string.home);
        PROFILE = context.getString(R.string.profile);
        MEMBERSHIP = context.getString(R.string.membership);
        CALENDAR = context.getString(R.string.calendar);
        RESOURCES = context.getString(R.string.resources);
        ANNOUNCEMENTS = context.getString(R.string.announcements);
        PREFERENCES = context.getString(R.string.preferences);
        ACCOUNT = context.getString(R.string.account);
        ASSIGNMENTS = context.getString(R.string.assignments);
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

    public static Map<Integer, SitePage> getPagesIds() {
        return pagesIds;
    }

    public static Map<Integer, SiteData> getSitesIds() {
        return sitesIds;
    }

    public static DrawerLayout getDrawer() {
        return drawer;
    }

    public static String getSelectedSite() {
        return selectedSite;
    }

    public static void setSelectedSite(String selectedSite) {
        NavigationDrawerHelper.selectedSite = selectedSite;
    }

    public static SiteData getSelectedSiteData() {
        return selectedSiteData;
    }

    public static void setSelectedSiteData(SiteData selectedSiteData) {
        NavigationDrawerHelper.selectedSiteData = selectedSiteData;
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
        } else if (pageName.equals(DELEGATED_ACCESS)) {
            return R.mipmap.ic_settings;
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
        } else if (pageName.equals(ASSIGNMENTS)) {
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
    public static void doAction(String pageName, int... params) {

        if (pageName.equals(DASHBOARD)) {
            DashboardFragment dashboardFragment = new DashboardFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(dashboardFragment, R.id.user_frame, context);
        } else if (pageName.equals(DELEGATED_ACCESS)) {

        } else if (pageName.equals(HOME)) {
            HomeFragment homeFragment = new HomeFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(homeFragment, R.id.user_frame, context);
        } else if (pageName.equals(PROFILE)) {
            ProfileFragment profileFragment = new ProfileFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(profileFragment, R.id.user_frame, context);
        } else if (pageName.equals(MEMBERSHIP)) {
            MembershipFragment membershipFragment = new MembershipFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(membershipFragment, R.id.user_frame, context);
        } else if (pageName.equals(CALENDAR)) {
            CalendarFragment calendarFragment = new CalendarFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(calendarFragment, R.id.user_frame, context);
        } else if (pageName.equals(RESOURCES)) {

        } else if (pageName.equals(ANNOUNCEMENTS)) {
            AnnouncementFragment announcementFragment = new AnnouncementFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(announcementFragment, R.id.user_frame, context);
        } else if (pageName.equals(PREFERENCES)) {

        } else if (pageName.equals(ACCOUNT)) {
            AccountFragment accountFragment = new AccountFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(accountFragment, R.id.user_frame, context);
        } else if (pageName.equals(ASSIGNMENTS)) {
            AssignmentFragment assignmentFragment = new AssignmentFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(assignmentFragment, R.id.user_frame, context);
        } else if (pageName.equals(CHAT_ROOM)) {

        } else if (pageName.equals(CONTACT_US)) {

        } else if (pageName.equals(DROPBOX)) {
            DropboxFragment dropboxFragment = new DropboxFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(dropboxFragment, R.id.user_frame, context);
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
            RosterFragment rosterFragment = new RosterFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(rosterFragment, R.id.user_frame, context);
        } else if (pageName.equals(SEARCH)) {

        } else if (pageName.equals(SECTION_INFO)) {

        } else if (pageName.equals(SIGN_UP)) {

        } else if (pageName.equals(SITE_INFO)) {

        } else if (pageName.equals(STATISTICS)) {

        } else if (pageName.equals(SYLLABUS)) {
            SyllabusFragment syllabusFragment = new SyllabusFragment().getData(mSwipeRefreshLayout, selectedSiteData);
            ActionsHelper.selectFragment(syllabusFragment, R.id.user_frame, context);
        } else if (pageName.equals(TESTS_QUIZZES)) {

        } else if (pageName.equals(WIKI)) {
            WikiFragment wikiFragment = new WikiFragment().getSwipeRefreshLayout(mSwipeRefreshLayout);
            ActionsHelper.selectFragment(wikiFragment, R.id.user_frame, context);
        } else if (pageName.equals(WEB_CONTENT)) {
            WebContentFragment webContentFragment;
            if (params.length > 0)
                webContentFragment = new WebContentFragment().getData(mSwipeRefreshLayout, params[0]);
            else
                webContentFragment = new WebContentFragment().getData(mSwipeRefreshLayout, 1);
            ActionsHelper.selectFragment(webContentFragment, R.id.user_frame, context);
        } else {

        }
    }
}
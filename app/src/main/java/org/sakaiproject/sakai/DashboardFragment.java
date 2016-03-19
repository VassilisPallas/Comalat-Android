package org.sakaiproject.sakai;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.sakaiproject.adapters.DashboardTabAdapter;
import org.sakaiproject.api.assignments.UserAssignmentsService;
import org.sakaiproject.api.events.UserEventsService;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.assignments.MembershipAssignmentsService;
import org.sakaiproject.api.memberships.pages.events.SiteEventsService;
import org.sakaiproject.api.sync.CalendarRefreshUI;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

/**
 * Created by vspallas on 23/02/16.
 */
public class DashboardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private SiteData siteData;
    private String siteName;
    private DashboardTabAdapter adapter;

    private FrameLayout root;

    public DashboardFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        swipeRefresh = (ISwipeRefresh) context;
    }

    /**
     * get the swipe refresh layout from activity
     *
     * @param swipeRefreshLayout the layout
     * @return the fragment with the data
     */
    public DashboardFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        DashboardFragment dashboardFragment = new DashboardFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        dashboardFragment.setArguments(b);
        return dashboardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        getActivity().setTitle(getResources().getString(R.string.dashboard));

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");
        root = (FrameLayout) v.findViewById(R.id.root);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.assignments)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.events)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.colorPrimaryDark, getContext().getTheme()));
        } else {
            tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        }

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);

        adapter = new DashboardTabAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), swipeRefreshLayout);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeRefreshLayout.setEnabled(false);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        swipeRefresh.Callback(this);

        return v;
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if (NetWork.getConnectionEstablished()) {
                    String url;
                    if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
                        UserAssignmentsService userAssignmentsService = new UserAssignmentsService(getContext(), null/*delegate*/);
                        userAssignmentsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "assignment/my.json";
                        userAssignmentsService.getAssignments(url);

                        UserEventsService userEventsService = new UserEventsService(getContext(), null /*calendarRefreshUI*/);
                        userEventsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "calendar/my.json";
                        userEventsService.getEvents(url);

                    } else {
                        MembershipAssignmentsService membershipAssignmentsService = new MembershipAssignmentsService(getContext(), siteData.getId(), null/*delegate*/);
                        membershipAssignmentsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "assignment/site/" + siteData.getId() + ".json";
                        membershipAssignmentsService.getAssignments(url);

                        SiteEventsService siteSiteEventsService = new SiteEventsService(getContext(), siteData.getId(), null /*calendarRefreshUI*/);
                        siteSiteEventsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "calendar/site/" + siteData.getId() + ".json";
                        siteSiteEventsService.getEvents(url);
                    }

                } else {
                    Snackbar.make(root, getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getText(R.string.can_not_sync), null).show();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}

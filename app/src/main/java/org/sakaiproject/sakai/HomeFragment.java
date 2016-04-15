package org.sakaiproject.sakai;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.sakaiproject.adapters.HomeTabAdapter;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.pojos.membership.SitePage;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by vassilis on 4/13/16.
 */
public class HomeFragment extends Fragment {

    private SiteData siteData;
    private String siteName;
    private HomeTabAdapter adapter;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private Map<Integer, String> positions = new HashMap<>();

    public HomeFragment() {
    }

    /**
     * get the swipe refresh layout from activity
     *
     * @param swipeRefreshLayout the layout
     * @return the fragment with the data
     */
    public HomeFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        homeFragment.setArguments(b);
        return homeFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle(getContext().getResources().getString(R.string.home));
        initialize(v);
        return v;
    }

    private void initialize(View v) {

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);

        int i = 0;
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.information)));
        addPos(i++, getContext().getResources().getString(R.string.information));

        if (!siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
            for (SitePage page : siteData.getPages()) {
                if (page.getTitle().equals(getContext().getResources().getString(R.string.calendar))) {
                    tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.events)));
                    addPos(i++, getContext().getResources().getString(R.string.events));
                }
                if (page.getTitle().equals(getContext().getResources().getString(R.string.announcements))) {
                    tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.announcements)));
                    addPos(i++, getContext().getResources().getString(R.string.announcements));
                }
                if (page.getTitle().equals(getContext().getResources().getString(R.string.messages))) {
                    tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.messages)));
                    addPos(i, getContext().getResources().getString(R.string.messages));
                }
            }
        } else {
            tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.events)));
            tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.announcements)));
            tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.messages)));

            addPos(i++, getContext().getResources().getString(R.string.events));
            addPos(i++, getContext().getResources().getString(R.string.announcements));
            addPos(i, getContext().getResources().getString(R.string.messages));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.colorPrimaryDark, getContext().getTheme()));
        } else {
            tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        }

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);

        adapter = new HomeTabAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), getContext(), positions);
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
                        swipeRefreshLayout.setEnabled(false);
                        break;
                }
                return false;
            }
        });
    }

    private void addPos(int pos, String title) {
        positions.put(pos, title);
    }

    @Override
    public void onStop() {
        super.onStop();
        swipeRefreshLayout.setEnabled(true);
    }
}

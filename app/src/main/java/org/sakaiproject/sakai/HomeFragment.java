package org.sakaiproject.sakai;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
    private FrameLayout root;

    private static final int WRITE_REQUEST_CODE = 42;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private boolean permissionsAccepted = false;

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

    private void getWritePermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else permissionsAccepted = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                permissionsAccepted = grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }

        if (!permissionsAccepted)
            Snackbar.make(root,
                    R.string.permissions_message,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getWritePermissions();
                        }
                    }).show();
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

        root = (FrameLayout) v.findViewById(R.id.root);
    }

    private void addPos(int pos, String title) {
        positions.put(pos, title);
    }

    @Override
    public void onStop() {
        super.onStop();
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        getWritePermissions();
    }
}

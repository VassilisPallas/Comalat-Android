package org.sakaiproject.sakai;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.sakaiproject.api.announcements.UserAnnouncementHelper;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.announcements.MembershipAnnouncementHelper;
import org.sakaiproject.api.memberships.pages.announcements.OfflineSiteAnnouncements;
import org.sakaiproject.api.memberships.pages.announcements.SiteAnnouncementsService;
import org.sakaiproject.api.sync.AnnouncementRefreshUI;
import org.sakaiproject.api.sync.MembershipRefreshUI;
import org.sakaiproject.customviews.adapters.AnnouncementAdapter;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

/**
 * Created by vspallas on 17/02/16.
 */
public class AnnouncementFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AnnouncementRefreshUI {

    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private String siteName;
    private SiteData siteData;
    private FrameLayout root;
    private AnnouncementRefreshUI delegate = this;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AnnouncementAdapter mAdapter;

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
    public AnnouncementFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        AnnouncementFragment announcement = new AnnouncementFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        announcement.setArguments(b);
        return announcement;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_announcement, container, false);
        getActivity().setTitle(getResources().getString(R.string.announcements));

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.announcement_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // if the memberships recycle view is not on the top then the swipe refresh can not be done
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0 || recyclerView.getChildCount() == 1) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
            mAdapter = new AnnouncementAdapter(UserAnnouncementHelper.userAnnouncement);
        } else {
            OfflineSiteAnnouncements announcements = new OfflineSiteAnnouncements(getContext(), siteData.getId());
            announcements.getAnnouncements();
            mAdapter = new AnnouncementAdapter(MembershipAnnouncementHelper.membershipAnnouncement);
        }

        mRecyclerView.setAdapter(mAdapter);

        swipeRefresh.Callback(this);

        root = (FrameLayout) v.findViewById(R.id.root);

        return v;
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {

                    String url = null;
                    if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
//                        UserEventsService userEventsService = new UserEventsService(getContext(), calendarRefreshUI);
//                        userEventsService.setSwipeRefreshLayout(swipeRefreshLayout);
//                        url = getContext().getResources().getString(R.string.url) + "calendar/my.json";
//                        userEventsService.getEvents(url);
                    } else {
                        SiteAnnouncementsService siteAnnouncementsService = new SiteAnnouncementsService(getContext(), siteData.getId(), delegate);
                        siteAnnouncementsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "announcement/site/" + siteData.getId() + ".json";
                        siteAnnouncementsService.getAnnouncements(url);
                    }

                } else {
                    Snackbar.make(root, getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getText(R.string.can_not_sync), null).show();
                }
            }
        });
    }

    @Override
    public void updateUI() {
        if (mAdapter != null && mRecyclerView != null) {
            if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
                mAdapter.setAnnouncement(UserAnnouncementHelper.userAnnouncement);
            } else {
                mAdapter.setAnnouncement(MembershipAnnouncementHelper.membershipAnnouncement);
            }
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}

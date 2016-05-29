package org.sakaiproject.sakai;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.adapters.EmptyAdapter;
import org.sakaiproject.api.announcements.OfflineUserAnnouncements;
import org.sakaiproject.api.announcements.UserAnnouncementsService;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.announcements.OfflineMembershipAnnouncements;
import org.sakaiproject.api.memberships.pages.announcements.MembershipAnnouncementsService;
import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.adapters.AnnouncementAdapter;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

/**
 * Created by vspallas on 17/02/16.
 */
public class AnnouncementFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback {

    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private String siteName;
    private SiteData siteData;
    private FrameLayout root;
    private Callback callback = this;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AnnouncementAdapter mAdapter;
    private EmptyAdapter emptyAdapter;
    private TextView noAnnouncements;
    private Announcement announcement;

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

        noAnnouncements = (TextView) v.findViewById(R.id.no_announcements);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
            OfflineUserAnnouncements offlineUserAnnouncements = new OfflineUserAnnouncements(getContext(), callback);
            offlineUserAnnouncements.getAnnouncements("announcements");
        } else {
            OfflineMembershipAnnouncements announcements = new OfflineMembershipAnnouncements(getContext(), siteData.getId(), callback);
            announcements.getAnnouncements();
        }

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


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                FragmentManager fm = getFragmentManager();
                AnnouncementDescriptionFragment dialogFragment;

                dialogFragment = new AnnouncementDescriptionFragment().setData(announcement.getAnnouncementCollection().get(position));
                dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.InfoDialogTheme);
                dialogFragment.show(fm, getContext().getResources().getString(R.string.event_info));

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        swipeRefresh.Callback(this);

        root = (FrameLayout) v.findViewById(R.id.root);

        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            setEmptyAdapter();
            noAnnouncements.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setAdapter(mAdapter);
            noAnnouncements.setVisibility(View.GONE);
        }

        return v;
    }

    private void setEmptyAdapter(){
        emptyAdapter = new EmptyAdapter();
        mRecyclerView.setAdapter(emptyAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {

                    String url;
                    if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
                        UserAnnouncementsService userAnnouncementsService = new UserAnnouncementsService(getContext(), callback);
                        userAnnouncementsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "announcement/user.json";
                        userAnnouncementsService.getAnnouncements(url, "announcements");
                    } else {
                        MembershipAnnouncementsService membershipAnnouncementsService = new MembershipAnnouncementsService(getContext(), siteData.getId(), callback);
                        membershipAnnouncementsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "announcement/site/" + siteData.getId() + ".json";
                        membershipAnnouncementsService.getAnnouncements(url);
                    }

                } else {
                    Snackbar.make(root, getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getText(R.string.can_not_sync), null).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onSuccess(Object obj) {
        if (obj instanceof Announcement) {
            this.announcement = (Announcement) obj;
            if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
                mAdapter = new AnnouncementAdapter(announcement, null);
            } else {
                mAdapter = new AnnouncementAdapter(announcement, siteData.getId());
            }

            if (mRecyclerView != null) {
                if (mAdapter.getItemCount() == 0) {
                    noAnnouncements.setVisibility(View.VISIBLE);
                    setEmptyAdapter();
                } else {
                    noAnnouncements.setVisibility(View.GONE);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError)
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

        swipeRefreshLayout.setRefreshing(false);
    }
}

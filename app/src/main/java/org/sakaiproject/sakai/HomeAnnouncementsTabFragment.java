package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.adapters.AnnouncementAdapter;
import org.sakaiproject.api.announcements.OfflineUserAnnouncements;
import org.sakaiproject.api.announcements.UserAnnouncementsService;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.announcements.MembershipAnnouncementsService;
import org.sakaiproject.api.memberships.pages.announcements.OfflineMembershipAnnouncements;
import org.sakaiproject.api.pojos.announcements.Announcement;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

/**
 * Created by vassilis on 4/14/16.
 */
public class HomeAnnouncementsTabFragment extends Fragment implements Callback {

    private String siteName;
    private SiteData siteData;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AnnouncementAdapter mAdapter;
    private TextView noAnnouncements;
    private Announcement announcement;

    public HomeAnnouncementsTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_home_announcements, container, false);
        initialize(v);
        fillList();
        return v;
    }

    private void initialize(View v) {
        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.announcement_recycler_view);

        noAnnouncements = (TextView) v.findViewById(R.id.no_announcements);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

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
    }

    private void fillList() {
        if (NetWork.getConnectionEstablished()) {
            String url;
            if (siteName.equals(getString(R.string.my_workspace))) {
                UserAnnouncementsService userAnnouncementsService = new UserAnnouncementsService(getContext(), this);
                url = getContext().getResources().getString(R.string.url) + "announcement/user.json";
                userAnnouncementsService.getAnnouncements(url, "announcements");
            } else {
                MembershipAnnouncementsService membershipAnnouncementsService = new MembershipAnnouncementsService(getContext(), siteData.getId(), this);
                url = getContext().getResources().getString(R.string.url) + "announcement/site/" + siteData.getId() + ".json";
                membershipAnnouncementsService.getAnnouncements(url);
            }
        } else {
            if (siteName.equals(getString(R.string.my_workspace))) {
                OfflineUserAnnouncements offlineUserAnnouncements = new OfflineUserAnnouncements(getContext(), this);
                offlineUserAnnouncements.getAnnouncements("announcements");
            } else {
                OfflineMembershipAnnouncements announcements = new OfflineMembershipAnnouncements(getContext(), siteData.getId(), this);
                announcements.getAnnouncements();
            }
        }
        onSuccess(null);
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
                } else {
                    noAnnouncements.setVisibility(View.GONE);
                }

                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError)
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
    }
}

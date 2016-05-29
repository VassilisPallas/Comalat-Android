package org.sakaiproject.sakai;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.adapters.EmptyAdapter;
import org.sakaiproject.adapters.RosterAdapter;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.roster.DownloadExcelService;
import org.sakaiproject.api.memberships.pages.roster.OfflineRoster;
import org.sakaiproject.api.memberships.pages.roster.RosterService;
import org.sakaiproject.api.pojos.roster.Roster;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

/**
 * Created by vspallas on 02/03/16.
 */
public class RosterFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback, View.OnClickListener, View.OnLongClickListener {
    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private SiteData siteData;
    private FrameLayout root;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RosterAdapter mAdapter;
    private EmptyAdapter emptyAdapter;
    private Callback callback = this;
    private FloatingActionButton downloadButton;
    private Roster roster;

    public RosterFragment() {
    }

    private void setEmptyAdapter() {
        emptyAdapter = new EmptyAdapter();
        mRecyclerView.setAdapter(emptyAdapter);
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
    public RosterFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        RosterFragment rosterFragment = new RosterFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        rosterFragment.setArguments(b);
        return rosterFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_roster, container, false);
        getActivity().setTitle(getContext().getResources().getString(R.string.roster));

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");
        root = (FrameLayout) v.findViewById(R.id.root);
        siteData = NavigationDrawerHelper.getSelectedSiteData();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.roster_recycler_view);
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

        OfflineRoster offlineRoster = new OfflineRoster(getContext(), siteData, callback);
        offlineRoster.getRoster();

        downloadButton = (FloatingActionButton) v.findViewById(R.id.download_excel);
        downloadButton.setOnClickListener(this);
        downloadButton.setOnLongClickListener(this);

        swipeRefresh.Callback(this);

        if (roster != null) {
            mAdapter = new RosterAdapter(getContext(), roster.getMembers(), siteData);
            if (mAdapter.getItemCount() == 0)
                setEmptyAdapter();
            else mRecyclerView.setAdapter(mAdapter);
        } else setEmptyAdapter();

        return v;
    }


    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {
                    RosterService rosterService = new RosterService(getContext(), siteData, swipeRefreshLayout, callback);
                    rosterService.getRoster(getContext().getResources().getString(R.string.url) + "roster-membership/" + siteData.getId() + "/get-membership.json");
                } else {
                    Snackbar.make(root, getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getText(R.string.can_not_sync), null).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        DownloadExcelService download = new DownloadExcelService(getContext(), siteData);
        download.download(getContext().getResources().getString(R.string.url) + "roster-export/" + siteData.getId() + "/export-to-excel");
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(getContext(), getContext().getResources().getString(R.string.download_roster_to_excel), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onSuccess(Object obj) {
        if (obj != null && obj instanceof Roster) {
            roster = (Roster) obj;
            mAdapter = new RosterAdapter(getContext(), roster.getMembers(), siteData);
            if (mAdapter.getItemCount() == 0)
                setEmptyAdapter();
            else mRecyclerView.setAdapter(mAdapter);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError)
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setEnabled(false);
    }
}

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

import org.sakaiproject.api.assignments.OfflineUserAssignments;
import org.sakaiproject.api.assignments.UserAssignmentsHelper;
import org.sakaiproject.api.assignments.UserAssignmentsService;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.assignments.MembershipAssignmentsHelper;
import org.sakaiproject.api.memberships.pages.assignments.MembershipAssignmentsService;
import org.sakaiproject.api.memberships.pages.assignments.OfflineMembershipAssignments;
import org.sakaiproject.api.sync.AssignmentsRefreshUI;
import org.sakaiproject.customviews.adapters.AssignmentAdapter;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

/**
 * Created by vspallas on 23/02/16.
 */
public class AssignmentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AssignmentsRefreshUI {

    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private String siteName;
    private SiteData siteData;
    private FrameLayout root;
    private AssignmentsRefreshUI delegate = this;
    private RecyclerView mRecyclerView;
    private AssignmentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView noAssignments;

    public AssignmentFragment() {
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
    public AssignmentFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        AssignmentFragment assignment = new AssignmentFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        assignment.setArguments(b);
        return assignment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_assignment, container, false);

        getActivity().setTitle(getResources().getString(R.string.assignments));

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.assignment_recycler_view);

        noAssignments = (TextView) v.findViewById(R.id.no_assignments);

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
            OfflineUserAssignments offlineUserAnnouncements = new OfflineUserAssignments(getContext());
            offlineUserAnnouncements.getAssignments();
            mAdapter = new AssignmentAdapter(getActivity(), UserAssignmentsHelper.userAssignment, null);
        } else {
            OfflineMembershipAssignments offlineMembershipAssignments = new OfflineMembershipAssignments(getContext(), siteData.getId());
            offlineMembershipAssignments.getAssignments();
            mAdapter = new AssignmentAdapter(getActivity(), MembershipAssignmentsHelper.membershipAssignment, siteData.getId());
        }


        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                FragmentManager fm = getFragmentManager();
                AssignmentsDescriptionFragment dialogFragment;

                if (siteName.equals(getContext().getResources().getString(R.string.my_workspace)))
                    dialogFragment = new AssignmentsDescriptionFragment().setData(UserAssignmentsHelper.userAssignment.getAssignmentsCollectionList().get(position), null);
                else
                    dialogFragment = new AssignmentsDescriptionFragment().setData(MembershipAssignmentsHelper.membershipAssignment.getAssignmentsCollectionList().get(position), siteData);
                dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.InfoDialogTheme);
                dialogFragment.show(fm, getContext().getResources().getString(R.string.event_info));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        if (mAdapter.getItemCount() == 0) {
            noAssignments.setVisibility(View.VISIBLE);
        } else {
            noAssignments.setVisibility(View.GONE);
        }

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
                        UserAssignmentsService userAssignmentsService = new UserAssignmentsService(getContext(), delegate);
                        userAssignmentsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "assignment/my.json";
                        userAssignmentsService.getAssignments(url);
                    } else {
                        MembershipAssignmentsService membershipAssignmentsService = new MembershipAssignmentsService(getContext(), siteData.getId(), delegate);
                        membershipAssignmentsService.setSwipeRefreshLayout(swipeRefreshLayout);
                        url = getContext().getResources().getString(R.string.url) + "assignment/site/" + siteData.getId() + ".json";
                        membershipAssignmentsService.getAssignments(url);
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
                mAdapter.setAssignment(UserAssignmentsHelper.userAssignment);
            } else {
                mAdapter.setAssignment(MembershipAssignmentsHelper.membershipAssignment);
            }

            if (mAdapter.getItemCount() == 0) {
                noAssignments.setVisibility(View.VISIBLE);
            } else {
                noAssignments.setVisibility(View.GONE);
            }

            mRecyclerView.setAdapter(mAdapter);
        }
    }
}

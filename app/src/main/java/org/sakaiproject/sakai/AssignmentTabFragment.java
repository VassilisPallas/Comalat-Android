package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sakaiproject.api.assignments.OfflineUserAssignments;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.assignments.OfflineMembershipAssignments;
import org.sakaiproject.adapters.AssignmentAdapter;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.api.sync.AssignmentsRefreshUI;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.util.Iterator;

/**
 * Created by vspallas on 23/02/16.
 */
public class AssignmentTabFragment extends Fragment implements AssignmentsRefreshUI {

    private String siteName;
    private SiteData siteData;
    private RecyclerView mRecyclerView;
    private AssignmentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView noAssignments;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    public AssignmentTabFragment() {
    }

    Assignment assignment;

    /**
     * get the swipe refresh layout from activity
     *
     * @param swipeRefreshLayout the layout
     * @return the fragment with the data
     */
    public AssignmentTabFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        AssignmentTabFragment assignmentTabFragment = new AssignmentTabFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        assignmentTabFragment.setArguments(b);
        return assignmentTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_assignment, container, false);

        swipeRefreshLayout = (CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.assignment_recycler_view);

        noAssignments = (TextView) v.findViewById(R.id.no_assignments);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        fillList();
        mAdapter = new AssignmentAdapter(getActivity(), assignment, null);

        mRecyclerView.setAdapter(mAdapter);

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

                String status;

                status = assignment.getAssignmentsCollectionList().get(position).getStatus();

                if (!status.equals(getContext().getResources().getString(R.string.closed))) {

                    FragmentManager fm = getFragmentManager();
                    AssignmentsDescriptionFragment dialogFragment;

                    dialogFragment = new AssignmentsDescriptionFragment().setData(assignment.getAssignmentsCollectionList().get(position), null);
                    dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.InfoDialogTheme);
                    dialogFragment.show(fm, getContext().getResources().getString(R.string.event_info));
                }
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

        return v;
    }

    private void fillList() {

        if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
            OfflineUserAssignments offlineUserAnnouncements = new OfflineUserAssignments(getContext(), this);
            offlineUserAnnouncements.getAssignments();
        } else {
            OfflineMembershipAssignments offlineMembershipAssignments = new OfflineMembershipAssignments(getContext(), siteData.getId(), this);
            offlineMembershipAssignments.getAssignments();
        }

        if (assignment != null && assignment.getAssignmentsCollectionList() != null) {
            Iterator<Assignment.AssignmentsCollection> it = assignment.getAssignmentsCollectionList().iterator();
            while (it.hasNext()) {
                Assignment.AssignmentsCollection collection = it.next();
                if (collection.getStatus().equals(getContext().getResources().getString(R.string.closed)))
                    it.remove();
            }
        }
    }

    @Override
    public void updateUI(Assignment assignment) {
        this.assignment = assignment;
        if (mAdapter != null && mRecyclerView != null) {
            fillList();
            mAdapter.setAssignment(assignment);

            if (mAdapter.getItemCount() == 0) {
                noAssignments.setVisibility(View.VISIBLE);
            } else {
                noAssignments.setVisibility(View.GONE);
            }

            mRecyclerView.setAdapter(mAdapter);
        }
    }
}

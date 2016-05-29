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
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.adapters.EmptyAdapter;
import org.sakaiproject.api.assignments.OfflineUserAssignments;
import org.sakaiproject.api.assignments.UserAssignmentsService;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.assignments.MembershipAssignmentsService;
import org.sakaiproject.api.memberships.pages.assignments.OfflineMembershipAssignments;
import org.sakaiproject.adapters.AssignmentAdapter;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.util.Iterator;

/**
 * Created by vspallas on 23/02/16.
 */
public class AssignmentTabFragment extends Fragment implements Callback {

    private String siteName;
    private SiteData siteData;
    private RecyclerView mRecyclerView;
    private AssignmentAdapter mAdapter;
    private EmptyAdapter emptyAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView noAssignments;

    public AssignmentTabFragment() {
    }

    Assignment assignment;

    private void setEmptyAdapter(){
        emptyAdapter = new EmptyAdapter();
        mRecyclerView.setAdapter(emptyAdapter);
    }

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

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.assignment_recycler_view);

        noAssignments = (TextView) v.findViewById(R.id.no_assignments);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        fillList();

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

        mAdapter = new AssignmentAdapter(getActivity(), assignment, null);

        if (mAdapter.getItemCount() == 0) {
            noAssignments.setVisibility(View.VISIBLE);
            setEmptyAdapter();
        } else {
            mRecyclerView.setAdapter(mAdapter);
            noAssignments.setVisibility(View.GONE);
        }

        return v;
    }

    private void fillList() {
        if (NetWork.getConnectionEstablished()) {
            String url;
            if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
                UserAssignmentsService userAssignmentsService = new UserAssignmentsService(getContext(), this);
                url = getContext().getResources().getString(R.string.url) + "assignment/my.json";
                userAssignmentsService.getAssignments(url);
            } else {
                MembershipAssignmentsService membershipAssignmentsService = new MembershipAssignmentsService(getContext(), siteData.getId(), this);
                url = getContext().getResources().getString(R.string.url) + "assignment/site/" + siteData.getId() + ".json";
                membershipAssignmentsService.getAssignments(url);
            }
        } else {
            if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
                OfflineUserAssignments offlineUserAnnouncements = new OfflineUserAssignments(getContext(), this);
                offlineUserAnnouncements.getAssignments();
            } else {
                OfflineMembershipAssignments offlineMembershipAssignments = new OfflineMembershipAssignments(getContext(), siteData.getId(), this);
                offlineMembershipAssignments.getAssignments();
            }
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
    public void onSuccess(Object obj) {
        if (obj instanceof Assignment) {
            this.assignment = (Assignment) obj;
            if (mAdapter != null && mRecyclerView != null) {
                fillList();
                mAdapter.setAssignment(assignment);

                if (mAdapter.getItemCount() == 0) {
                    setEmptyAdapter();
                    noAssignments.setVisibility(View.VISIBLE);
                } else {
                    noAssignments.setVisibility(View.GONE);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        }
    }
}

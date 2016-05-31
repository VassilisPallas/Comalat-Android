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
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.assignments.MembershipAssignmentsService;
import org.sakaiproject.api.memberships.pages.assignments.OfflineMembershipAssignments;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.adapters.AssignmentAdapter;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

/**
 * Created by vspallas on 23/02/16.
 */
public class AssignmentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback {

    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private String siteName;
    private SiteData siteData;
    private FrameLayout root;
    private Callback callback = this;
    private RecyclerView mRecyclerView;
    private AssignmentAdapter mAdapter;
    private EmptyAdapter emptyAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView noAssignments;
    private Assignment assignment;

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

        OfflineMembershipAssignments offlineMembershipAssignments = new OfflineMembershipAssignments(getContext(), siteData.getId(), callback);
        offlineMembershipAssignments.getAssignments();

        mAdapter = new AssignmentAdapter(getActivity(), assignment, siteData.getId());

        if (mAdapter.getItemCount() == 0) {
            setEmptyAdapter();
            noAssignments.setVisibility(View.VISIBLE);
        } else {
            noAssignments.setVisibility(View.GONE);
            mRecyclerView.setAdapter(mAdapter);
        }

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

        swipeRefresh.Callback(this);

        root = (FrameLayout) v.findViewById(R.id.root);

        return v;
    }

    private void setEmptyAdapter() {
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
                    MembershipAssignmentsService membershipAssignmentsService = new MembershipAssignmentsService(getContext(), siteData.getId(), callback);
                    membershipAssignmentsService.setSwipeRefreshLayout(swipeRefreshLayout);
                    url = getContext().getResources().getString(R.string.url) + "assignment/site/" + siteData.getId() + ".json";
                    membershipAssignmentsService.getAssignments(url);

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
        if (obj instanceof Assignment) {
            this.assignment = (Assignment) obj;
            mAdapter = new AssignmentAdapter(getActivity(), assignment, siteData.getId());
            if (mRecyclerView != null) {
                if (mAdapter.getItemCount() == 0) {
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
        swipeRefreshLayout.setRefreshing(false);
    }
}

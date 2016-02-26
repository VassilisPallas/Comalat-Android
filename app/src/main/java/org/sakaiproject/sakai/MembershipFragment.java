package org.sakaiproject.sakai;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.MembershipService;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.actions.IUnJoin;
import org.sakaiproject.api.memberships.actions.SiteUnJoin;
import org.sakaiproject.api.sync.MembershipRefreshUI;
import org.sakaiproject.adapters.MembershipAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MembershipFragment extends Fragment implements IUnJoin, SwipeRefreshLayout.OnRefreshListener, IMembershipDialog, MembershipRefreshUI {

    private RecyclerView mRecyclerView;
    private MembershipAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton createSite;
    private EditText membershipSearch;
    private FrameLayout root;

    private ISwipeRefresh swipeRefresh;

    private MembershipRefreshUI delegate = this;

    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    public MembershipFragment() {
        // Required empty public constructor
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
    public MembershipFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        MembershipFragment membershipFragment = new MembershipFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        membershipFragment.setArguments(b);
        return membershipFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_membership, container, false);

        getActivity().setTitle(getContext().getResources().getString(R.string.membership));

        root = (FrameLayout) v.findViewById(R.id.root);

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.membership_recycler_view);

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

        List<SiteData> membership = new ArrayList<>(SiteData.getSites());
        membership.addAll(SiteData.getProjects());

        mAdapter = new MembershipAdapter(membership, v.getContext(), this, this);
        mRecyclerView.setAdapter(mAdapter);

        createSite = (FloatingActionButton) v.findViewById(R.id.create_site);
        createSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.ok), Toast.LENGTH_SHORT).show();
            }
        });

        membershipSearch = (EditText) v.findViewById(R.id.membership_search);
        membershipSearch.addTextChangedListener(searchWatcher);

        swipeRefresh.Callback(this);

        return v;
    }

    private TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void siteUnJoin(List<SiteData> membership, int position) {
        new SiteUnJoin(membership.get(position).getId(), getContext(), mAdapter, position, swipeRefreshLayout, delegate).unJoin();
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {
                    MembershipService membershipService = new MembershipService(getContext(), delegate);
                    membershipService.setSwipeRefreshLayout(swipeRefreshLayout);
                    try {
                        membershipService.getSites(getContext().getString(R.string.url) + "membership.json");
                    } catch (IOException e) {
                        e.printStackTrace();
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
    public void openDialog(String userShortName, String email, String shortDescription, String description, SiteData data) {
        FragmentManager fm = getFragmentManager();
        MembershipDescriptionFragment dialogFragment = new MembershipDescriptionFragment().getData(userShortName, email, shortDescription, description, data);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.InfoDialogTheme);
        dialogFragment.show(fm, getContext().getResources().getString(R.string.site_descr));
    }

    @Override
    public void updateUI() {
        UserActivity.getSitesNavigationDrawer().fillSitesDrawer();
        if (mAdapter != null && mRecyclerView != null) {
            List<SiteData> temp = new ArrayList<>(SiteData.getSites());
            temp.addAll(SiteData.getProjects());
            mAdapter.setMemberships(temp);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
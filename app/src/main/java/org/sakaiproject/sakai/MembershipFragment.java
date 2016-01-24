package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.api.site.actions.IUnJoin;
import org.sakaiproject.api.site.actions.UnJoinAsync;
import org.sakaiproject.customviews.adapters.MembershipAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MembershipFragment extends Fragment implements IUnJoin {

    private RecyclerView mRecyclerView;
    private MembershipAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBar refreshProgressBar;
    private FloatingActionButton createSite;
    private EditText membershipSearch;

    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;

    public MembershipFragment() {
        // Required empty public constructor
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

        refreshProgressBar = (ProgressBar) v.findViewById(R.id.membership_refresh);

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.membership_recycler_view);

//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // if the events recycle view is not on the top then the swipe refresh can not be done
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

        mAdapter = new MembershipAdapter(membership, v.getContext(), this);
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
        new UnJoinAsync(membership.get(position).getId(), getContext(), refreshProgressBar, mAdapter, position).execute();
    }
}
package org.sakaiproject.sakai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.adapters.EmptyAdapter;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.pages.syllabus.OfflineSyllabus;
import org.sakaiproject.api.memberships.pages.syllabus.SyllabusService;
import org.sakaiproject.api.memberships.pages.syllabus.UpdateSyllabus;
import org.sakaiproject.api.pojos.syllabus.Syllabus;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.adapters.SyllabusAdapter;
import org.sakaiproject.customviews.rich_textview.RichTextView;

import java.io.IOException;

/**
 * Created by vasilis on 1/28/16.
 */
public class SyllabusFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, UpdateSyllabus, Callback {

    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private SiteData siteData;
    private Syllabus syllabus;
    private RecyclerView mRecyclerView;
    private SyllabusAdapter mAdapter;
    private EmptyAdapter emptyAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FrameLayout root;
    private UpdateSyllabus callback;
    private Callback delegate = this;
    private TextView noSyllabusItems;
    private RichTextView reLaunchUrl;

    public SyllabusFragment() {
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
    public SyllabusFragment getData(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout, SiteData siteData) {
        SyllabusFragment syllabusFragment = new SyllabusFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        b.putSerializable("siteData", siteData);
        syllabusFragment.setArguments(b);
        return syllabusFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_syllabus, container, false);

        getActivity().setTitle(getResources().getString(R.string.syllabus));

        siteData = (SiteData) getArguments().getSerializable("siteData");

        reLaunchUrl = (RichTextView) v.findViewById(R.id.relaunch_url);
        reLaunchUrl.setContext(getContext());
        reLaunchUrl.setSiteData(siteData.getId());

        noSyllabusItems = (TextView) v.findViewById(R.id.no_syllabus);

        root = (FrameLayout) v.findViewById(R.id.root);

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.syllabus_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        OfflineSyllabus offlineSyllabus = new OfflineSyllabus(getContext(), siteData.getId());

        try {
            syllabus = offlineSyllabus.getSyllabus();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (syllabus != null && syllabus.getRedirectUrl() != null) {
            noSyllabusItems.setVisibility(View.GONE);
            String message = "<a href=\"" + syllabus.getRedirectUrl() + "\">" + getContext().getResources().getString(R.string.re_launch_url) + "</a>";
            reLaunchUrl.setText(message);
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("url", syllabus.getRedirectUrl()));
            reLaunchUrl.setVisibility(View.VISIBLE);
        }

        if (syllabus != null) {
            update(mRecyclerView, mAdapter, syllabus, siteData.getId());
            noSyllabusItems.setVisibility(View.GONE);
        } else {
            setEmptyAdapter();
            noSyllabusItems.setVisibility(View.VISIBLE);
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

        swipeRefresh.Callback(this);
        callback = this;

        return v;
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {
                    SyllabusService syllabusService = new SyllabusService(getContext(), siteData.getId(), delegate);
                    syllabusService.setSwipeRefreshLayout(swipeRefreshLayout);
                    try {
                        syllabusService.getSyllabus(getContext().getString(R.string.url) + "syllabus/site/" + siteData.getId() + ".json");
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
    public void update(RecyclerView recyclerView, SyllabusAdapter adapter, Syllabus syllabusData, String id) {
        adapter = new SyllabusAdapter(syllabusData.getItems(), getContext(), getActivity(), id);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSuccess(Object obj) {
        OfflineSyllabus offlineSyllabus = new OfflineSyllabus(getContext(), siteData.getId());
        Syllabus syllabus = null;
        try {
            syllabus = offlineSyllabus.getSyllabus();
            if (syllabus.getItems() != null && syllabus.getItems().size() > 0) {
                callback.update(mRecyclerView, mAdapter, syllabus, siteData.getId());
                noSyllabusItems.setVisibility(View.GONE);
                reLaunchUrl.setVisibility(View.GONE);
            } else if (syllabus.getRedirectUrl() != null) {
                setEmptyAdapter();
                noSyllabusItems.setVisibility(View.GONE);
                reLaunchUrl.setVisibility(View.VISIBLE);
            } else {
                setEmptyAdapter();
                noSyllabusItems.setVisibility(View.VISIBLE);
                reLaunchUrl.setVisibility(View.GONE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setEmptyAdapter() {
        emptyAdapter = new EmptyAdapter();
        mRecyclerView.setAdapter(emptyAdapter);
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setRefreshing(false);
    }
}

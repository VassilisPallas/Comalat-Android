package org.sakaiproject.sakai;

import android.content.Context;
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
import android.widget.ProgressBar;

import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.pages.syllabus.OfflineSyllabus;
import org.sakaiproject.api.memberships.pages.syllabus.SyllabusService;
import org.sakaiproject.api.memberships.pages.syllabus.UpdateSyllabus;
import org.sakaiproject.api.pojos.syllabus.Syllabus;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.sync.SyllabusRefreshUI;
import org.sakaiproject.customviews.adapters.SyllabusAdapter;
import org.sakaiproject.general.Actions;

import java.io.IOException;

/**
 * Created by vasilis on 1/28/16.
 */
public class SyllabusFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, UpdateSyllabus, SyllabusRefreshUI {

    private FloatingActionButton showCreations;
    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private SiteData siteData;
    private Syllabus syllabus;
    private RecyclerView mRecyclerView;
    private SyllabusAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar refreshProgressBar;
    private FrameLayout root;
    private UpdateSyllabus callback;
    private SyllabusRefreshUI delegate = this;

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

        OfflineSyllabus offlineSyllabus = new OfflineSyllabus(getContext(), siteData.getId());

        try {
            syllabus = offlineSyllabus.getSyllabus();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (syllabus != null && syllabus.getRedirectUrl() != null) {
//            WebViewFragment webViewFragment = new WebViewFragment().getUrl(syllabus.getRedirectUrl());
//            Actions.selectFragment(webViewFragment, R.id.user_frame, getContext());
        }

        root = (FrameLayout) v.findViewById(R.id.root);

        refreshProgressBar = (ProgressBar) v.findViewById(R.id.syllabus_refresh);

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.syllabus_recycler_view);

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

        showCreations = (FloatingActionButton) v.findViewById(R.id.show_creations);
        showCreations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AnimatorSet s = new AnimatorSet();
//                s.play(R.anim.button_rotation_move).before(anim3);
//                s.play(anim4).after(anim3);
//
//                Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.button_rotation_move);
//                Animation rightRotation = AnimationUtils.loadAnimation(getContext(), R.anim.button_rotation_back);
//                showCreations.startAnimation(rotation);
//                showCreations.startAnimation(rightRotation);
            }
        });

        if (syllabus != null) {
            update(mRecyclerView, mAdapter, syllabus, siteData.getId());
        } else {

        }

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
//                    SyllabusRefresh refresh = new SyllabusRefresh(getContext());
//                    refresh.setSwipeRefreshLayout(swipeRefreshLayout);
//                    refresh.setmAdapter(mAdapter);
//                    refresh.setmRecyclerView(mRecyclerView);
//                    refresh.setSiteId(siteData.getId());
//                    refresh.setCallback(callback);
//                    refresh.execute();


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
    public void updateUI() {
        OfflineSyllabus offlineSyllabus = new OfflineSyllabus(getContext(), siteData.getId());
        Syllabus syllabus = null;
        try {
            syllabus = offlineSyllabus.getSyllabus();
            if (syllabus.getItems().size() > 0) {
                callback.update(mRecyclerView, mAdapter, syllabus, siteData.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

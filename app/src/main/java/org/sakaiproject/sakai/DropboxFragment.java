package org.sakaiproject.sakai;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.adapters.dropbox.FillGridView;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.dropbox.DropboxService;
import org.sakaiproject.api.memberships.pages.dropbox.OfflineDropbox;
import org.sakaiproject.api.memberships.pages.roster.OfflineRoster;
import org.sakaiproject.api.pojos.roster.Roster;
import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.io.File;
import java.util.Map;

/**
 * Created by vspallas on 03/03/16.
 */
public class DropboxFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback {

    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private SiteData siteData;
    private FrameLayout root;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Callback callback = this;
    private Roster roster;

    public DropboxFragment() {
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
    public DropboxFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        DropboxFragment dropboxFragment = new DropboxFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        dropboxFragment.setArguments(b);
        return dropboxFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dropbox, container, false);
        getActivity().setTitle(getContext().getResources().getString(R.string.dropbox));
        root = (FrameLayout) v.findViewById(R.id.root);
        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");
        siteData = NavigationDrawerHelper.getSelectedSiteData();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.dropbox_recycle_view);
        layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

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

        File externalStoragePath = getContext().getFilesDir();
        File file = new File(externalStoragePath, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "roster" + File.separator + siteData.getId() + "_roster.json");

        if (file.exists()) {
            OfflineRoster offlineRoster = new OfflineRoster(getContext(), siteData, callback);
            offlineRoster.getRoster();
            new OfflineDropbox(getContext(), siteData, callback, roster).getDropboxItems();
        } else {
            // download roster and make call
        }

        swipeRefresh.Callback(this);
        return v;
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {
                    File externalStoragePath = getContext().getFilesDir();
                    File file = new File(externalStoragePath, User.getUserEid() + File.separator + "memberships" + File.separator + siteData.getId() + File.separator + "roster" + File.separator + siteData.getId() + "_roster.json");

                    if (file.exists()) {
                        OfflineRoster offlineRoster = new OfflineRoster(getContext(), siteData, callback);
                        offlineRoster.getRoster();
                        DropboxService dropboxService = new DropboxService(getContext(), siteData, callback, roster, swipeRefreshLayout);
                        dropboxService.getDropboxItems();
                    } else {
                        // download roster and make call
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    Snackbar.make(root, getContext().getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT)
                            .setAction(getResources().getText(R.string.can_not_sync), null).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onSuccess(Object obj) {
        if (obj instanceof Roster) {
            this.roster = (Roster) obj;
        } else if (obj instanceof Map<?, ?>) {
            new FillGridView(getContext(), siteData, (Map<String, Integer>) obj, mRecyclerView).loadFiles();
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError)
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setEnabled(false);
    }
}

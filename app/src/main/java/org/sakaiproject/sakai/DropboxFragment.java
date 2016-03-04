package org.sakaiproject.sakai;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sakaiproject.adapters.DropboxAdapter;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.dropbox.DropboxService;
import org.sakaiproject.api.memberships.pages.roster.OfflineRoster;
import org.sakaiproject.api.pojos.dropbox.Dropbox;
import org.sakaiproject.api.pojos.roster.Member;
import org.sakaiproject.api.pojos.roster.Roster;
import org.sakaiproject.api.sync.DropboxRefreshUI;
import org.sakaiproject.api.sync.RosterRefreshUI;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vspallas on 03/03/16.
 */
public class DropboxFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DropboxRefreshUI, RosterRefreshUI {

    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private SiteData siteData;
    private FrameLayout root;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DropboxAdapter dropboxAdapter;
    private DropboxRefreshUI callback = this;
    private Roster roster;
    private RosterRefreshUI rosterCallback = this;
    private LinkedHashMap<Member, Dropbox> dropboxList;

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

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String previous = ((TextView) view.findViewById(R.id.file_name)).getText().toString();
                Log.i("previous", previous);
                Member member = (new ArrayList<>(dropboxList.keySet())).get(position);
                System.out.println("you have clicked " + member.getEid() + " with files ");
                Dropbox dropbox = dropboxList.get(member);
                for (Dropbox.Collection collection : dropbox.getCollection()) {
                    System.out.println(collection.getTitle());
                }

                dropboxAdapter.setClickedMemberDir(member);
                dropboxAdapter.setPreviousPlace(previous);

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

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
                        OfflineRoster offlineRoster = new OfflineRoster(getContext(), siteData, rosterCallback);
                        offlineRoster.getRoster();
                        DropboxService dropboxService = new DropboxService(getContext(), siteData, callback, roster, swipeRefreshLayout);
                        dropboxService.getDropboxItems();
                    } else {
                        // download roster and make call
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
    public void updateUI(LinkedHashMap<Member, Dropbox> dropboxList) {
        this.dropboxList = dropboxList;
        dropboxAdapter = new DropboxAdapter(dropboxList, getContext(), siteData.getId(), true);
        mRecyclerView.setAdapter(dropboxAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void updateUI(Roster roster) {
        this.roster = roster;
    }
}

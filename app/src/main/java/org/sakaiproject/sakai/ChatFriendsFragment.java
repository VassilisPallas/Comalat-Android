package org.sakaiproject.sakai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.adapters.ChatFriendsAdapter;
import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.friends.FriendsService;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.pojos.friends.Friends;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.listeners.RecyclerItemClickListener;

import java.util.List;


public class ChatFriendsFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, Callback, OnRefreshListener {

    private ISwipeRefresh swipeRefresh;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ChatFriendsAdapter adapter;
    List<Friends> friends;
    private RelativeLayout root;
    private Callback callback = this;

    public ChatFriendsFragment() {
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
    public ChatFriendsFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        ChatFriendsFragment chatFriendsFragment = new ChatFriendsFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        chatFriendsFragment.setArguments(b);
        return chatFriendsFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat_friends, container, false);
        initialize(v);
        return v;
    }

    private void initialize(View v) {
        root = (RelativeLayout) v.findViewById(R.id.root);
        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycle_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(null);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, this));
        swipeRefresh.Callback(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(getActivity(), ChatActivity.class));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onSuccess(Object obj) {
        if (obj instanceof List<?>) {
            friends = (List<Friends>) obj;
            adapter = new ChatFriendsAdapter(friends);
            mRecyclerView.setAdapter(adapter);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(VolleyError error) {
        if (error instanceof ServerError)
            Toast.makeText(getContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {
                    FriendsService friendsService = new FriendsService(callback, getContext());
                    friendsService.getFriends(getString(R.string.url) + "profile/" + User.getUserEid() + "/connections.json");
                } else {
                    Snackbar.make(root, getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getText(R.string.can_not_sync), null).show();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}

package org.sakaiproject.sakai;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.web_content.OfflineWebContent;
import org.sakaiproject.api.memberships.pages.web_content.WebContentService;
import org.sakaiproject.api.pojos.web_content.WebContent;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.rich_textview.RichTextView;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

/**
 * Created by vspallas on 01/03/16.
 */
public class WebContentFragment extends Fragment implements Callback, SwipeRefreshLayout.OnRefreshListener {
    private RichTextView urlTextView;
    private int index;
    private String siteName;
    private SiteData siteData;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private ISwipeRefresh swipeRefresh;
    private FrameLayout root;
    private Callback callback = this;

    public WebContentFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        swipeRefresh = (ISwipeRefresh) context;
    }

    /**
     * get the swipe refresh layout from activity
     *
     * @param index the web content index
     * @return the fragment with the data
     */
    public WebContentFragment getData(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout, int index) {
        WebContentFragment webContentFragment = new WebContentFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        b.putInt("index", index);
        webContentFragment.setArguments(b);
        return webContentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_web_content, container, false);
        getActivity().setTitle(getContext().getResources().getString(R.string.web_content));

        root = (FrameLayout) v.findViewById(R.id.root);

        index = getArguments().getInt("index");

        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");

        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();

        urlTextView = (RichTextView) v.findViewById(R.id.web_content_url);
        urlTextView.setContext(getContext());
        urlTextView.setSiteData(siteData.getId());

        swipeRefresh.Callback(this);

        if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
            OfflineWebContent offlineWebContent = new OfflineWebContent(getContext(), null, callback);
            offlineWebContent.getWebContent();
        } else {
            OfflineWebContent offlineWebContent = new OfflineWebContent(getContext(), siteData.getId(), callback);
            offlineWebContent.getWebContent();
        }
        return v;
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (NetWork.getConnectionEstablished()) {
                    if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
                        WebContentService webContentService = new WebContentService(getContext(), swipeRefreshLayout, null, callback);
                        webContentService.getWebContent(getContext().getResources().getString(R.string.url) + "webcontent/site/~" + User.getUserId() + ".json");
                    } else {
                        WebContentService webContentService = new WebContentService(getContext(), swipeRefreshLayout, siteData.getId(), callback);
                        webContentService.getWebContent(getContext().getResources().getString(R.string.url) + "webcontent/site/" + siteData.getId() + ".json");
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
    public void onSuccess(Object obj) {
        if (obj instanceof WebContent) {
            WebContent webContent = (WebContent) obj;
            urlTextView.setText(String.format("<a href=\"%s\">%s</a>", webContent.getCollection().get(index - 1).getUrl(), webContent.getCollection().get(index - 1).getUrl()));
            swipeRefreshLayout.setRefreshing(false);
            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("url", webContent.getCollection().get(index - 1).getUrl()));
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

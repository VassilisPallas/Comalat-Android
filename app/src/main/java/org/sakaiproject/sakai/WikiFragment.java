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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;

import org.sakaiproject.api.callback.Callback;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.memberships.pages.wiki.MembershipOfflineWiki;
import org.sakaiproject.api.memberships.pages.wiki.MembershipWikiService;
import org.sakaiproject.api.pojos.wiki.Wiki;
import org.sakaiproject.api.user.User;
import org.sakaiproject.customviews.rich_textview.RichTextView;
import org.sakaiproject.customviews.scrollview.CustomScrollView;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.helpers.user_navigation_drawer_helpers.NavigationDrawerHelper;

import java.io.Serializable;

/**
 * Created by vspallas on 28/02/16.
 */
public class WikiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback {

    RichTextView wikiText;
    TextView openComments;
    private org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout;
    private ISwipeRefresh swipeRefresh;
    private String siteName;
    private SiteData siteData;
    private FrameLayout root;
    private Callback callback = this;
    private CustomScrollView scrollView;

    public WikiFragment() {
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
    public WikiFragment getSwipeRefreshLayout(org.sakaiproject.customviews.CustomSwipeRefreshLayout swipeRefreshLayout) {
        WikiFragment wikiFragment = new WikiFragment();
        Bundle b = new Bundle();
        b.putSerializable("swipeRefresh", swipeRefreshLayout);
        wikiFragment.setArguments(b);
        return wikiFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wiki, container, false);

        getActivity().setTitle(getContext().getResources().getString(R.string.wiki));
        root = (FrameLayout) v.findViewById(R.id.root);
        swipeRefreshLayout = (org.sakaiproject.customviews.CustomSwipeRefreshLayout) getArguments().getSerializable("swipeRefresh");
        siteData = NavigationDrawerHelper.getSelectedSiteData();
        siteName = NavigationDrawerHelper.getSelectedSite();
        wikiText = (RichTextView) v.findViewById(R.id.wiki_text);
        wikiText.setContext(getContext());
        wikiText.setSiteData(siteData.getId());
        openComments = (TextView) v.findViewById(R.id.open_comments);
        swipeRefresh.Callback(this);
        scrollView = (CustomScrollView) v.findViewById(R.id.scrollView);
        scrollView.setSwipeRefreshLayout(swipeRefreshLayout);

        if (siteName.equals(getContext().getResources().getString(R.string.my_workspace))) {
            MembershipOfflineWiki membershipOfflineWiki = new MembershipOfflineWiki(callback, getContext(), null);
            membershipOfflineWiki.getWiki();
        } else {
            MembershipOfflineWiki membershipOfflineWiki = new MembershipOfflineWiki(callback, getContext(), siteData.getId());
            membershipOfflineWiki.getWiki();
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
                        MembershipWikiService membershipWikiService = new MembershipWikiService(getContext(), swipeRefreshLayout, callback, null);
                        membershipWikiService.getWiki(getContext().getResources().getString(R.string.url) + "wiki/site/~" + User.getUserId() + ".json");
                    } else {
                        MembershipWikiService membershipWikiService = new MembershipWikiService(getContext(), swipeRefreshLayout, callback, siteData.getId());
                        membershipWikiService.getWiki(getContext().getResources().getString(R.string.url) + "wiki/site/" + siteData.getId() + ".json");
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
        if (obj instanceof Wiki) {
            final Wiki wiki = (Wiki) obj;

            String message = wiki.getHtml().trim();
            if (!wiki.getHtml().trim().equals(""))
                message = ActionsHelper.deleteHtmlTags(message);

            if (!message.equals(""))
                wikiText.setText(message);
            else
                wikiText.setText(getContext().getResources().getString(R.string.no_wiki));

            if (wiki.getNumberOfComments() > 0) {
                openComments.setText(String.format("%d %s", wiki.getNumberOfComments(), getContext().getResources().getString(R.string.comments)));
                openComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), CommentsActivity.class).putExtra("comments", (Serializable) wiki.getComments()).putExtra("title", getContext().getResources().getString(R.string.Comments)));
                    }
                });
            } else {
                openComments.setText(getContext().getResources().getString(R.string.no_comments));
            }
            swipeRefreshLayout.setRefreshing(false);
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

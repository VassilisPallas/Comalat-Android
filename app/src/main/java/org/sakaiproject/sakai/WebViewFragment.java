package org.sakaiproject.sakai;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    private WebView webView;

    public WebViewFragment() {
        // Required empty public constructor
    }

    /**
     * get the swipe refresh layout from activity
     *
     * @param url the url for the webview
     * @return the fragment with the data
     */
    public WebViewFragment getUrl(String url) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle b = new Bundle();
        b.putSerializable("url", url);
        webViewFragment.setArguments(b);
        return webViewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_web_view, container, false);

        webView = (WebView) v.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getArguments().getString("url"));

        // Inflate the layout for this fragment
        return v;
    }

}

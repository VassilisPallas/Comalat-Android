package org.sakaiproject.sakai;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.motd.OnlineMessageOfTheDay;
import org.sakaiproject.customviews.rich_textview.RichTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {

    private ProgressBar progressBar;
    private OnlineMessageOfTheDay onlineMessageOfTheDay;

    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);

        getActivity().setTitle(getContext().getResources().getString(R.string.welcome));

        findViewsById(v);
        onlineMessageOfTheDay = new OnlineMessageOfTheDay(getContext());
        new MotdAsync(getResources().getString(R.string.url) + "announcement/motd.json").execute();


        return v;
    }

    private void findViewsById(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.motd_progressbar);

        RichTextView welcomeMessage = (RichTextView) v.findViewById(R.id.welcome_message);
        welcomeMessage.setContext(getContext());
    }

    public class MotdAsync extends AsyncTask<Void, Void, Integer> {

        private String url;

        MotdAsync(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            if (NetWork.getConnectionEstablished()) {
                onlineMessageOfTheDay.getMessageOfTheDay(url);
                onlineMessageOfTheDay = onlineMessageOfTheDay.getMessageOfTheDayObj();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer number) {
            super.onPostExecute(number);
            progressBar.setVisibility(View.GONE);
        }
    }


}

package org.sakaiproject.sakai;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.online.motd.MessageOfTheDay;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {

    private ProgressBar progressBar;
    private MessageOfTheDay messageOfTheDay;

    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        findViewsById(v);
        messageOfTheDay = new MessageOfTheDay();
        new MotdAsync(getResources().getString(R.string.url) + "announcement/motd.json").execute();
        return v;
    }

    private void findViewsById(View v) {
        progressBar = (ProgressBar) v.findViewById(R.id.motd_progressbar);
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
                messageOfTheDay.getMessageOfTheDay(url);
                messageOfTheDay = messageOfTheDay.getMessageOfTheDayObj();
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

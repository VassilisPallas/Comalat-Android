package org.sakaiproject.api.customviews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sakaiproject.api.user.data.UserEvents;
import org.sakaiproject.sakai.R;

import java.util.List;

/**
 * Created by vasilis on 10/20/15.
 */
public class SelectedDayEventsAdapter extends RecyclerView.Adapter<SelectedDayEventsAdapter.ViewHolder> {

    List<UserEvents> userEventsList;

    public SelectedDayEventsAdapter(List<UserEvents> userEventsList) {
        this.userEventsList = userEventsList;
    }


    @Override
    public SelectedDayEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_day_events, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(SelectedDayEventsAdapter.ViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.eventTitle.setText(userEventsList.get(position).getTitle());
        holder.eventTime.setText(userEventsList.get(position).getEventTime());

    }

    @Override
    public int getItemCount() {
        if (userEventsList != null)
            return userEventsList.size();
        return 0;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView eventTitle, eventTime;
        ImageView activity_type;

        public ViewHolder(View v) {
            super(v);
            eventTitle = (TextView) v.findViewById(R.id.tv_event);
            eventTime = (TextView) v.findViewById(R.id.tv_time);
            activity_type = (ImageView) v.findViewById(R.id.activity_type_event);
        }
    }

}

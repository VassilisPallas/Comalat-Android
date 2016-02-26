package org.sakaiproject.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sakaiproject.api.events.UserEvents;
import org.sakaiproject.sakai.R;

import java.util.List;

/**
 * Created by vasilis on 10/20/15.
 */
public class SelectedDayEventsAdapter extends RecyclerView.Adapter<SelectedDayEventsAdapter.ViewHolder> {

    List<UserEvents> userEventsList;
    private Context context;

    /**
     * SelectedDayEventsAdapter constructor
     *
     * @param context        the context
     * @param userEventsList the list with the events
     */
    public SelectedDayEventsAdapter(Context context, List<UserEvents> userEventsList) {
        this.context = context;
        this.userEventsList = userEventsList;
    }


    public void setUserEventsList(List<UserEvents> userEventsList) {
        this.userEventsList = userEventsList;
    }

    @Override
    public SelectedDayEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_day_events, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(SelectedDayEventsAdapter.ViewHolder holder, int position) {

        holder.eventTitle.setText(userEventsList.get(position).getTitle());
        holder.eventTime.setText(userEventsList.get(position).getEventWholeDate());
        holder.activity_type.setImageBitmap(imageByType(userEventsList.get(position).getType()));
    }

    @Override
    public int getItemCount() {
        return userEventsList != null ? userEventsList.size() : 0;
    }

    /**
     * get the image for the event type
     *
     * @param eventType the type of the event
     * @return the image from the resources as bitmap
     */
    public Bitmap imageByType(String eventType) {
        Bitmap bitmap = null;
        switch (eventType) {
            case "Academic Calendar":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.academic_calendar);
                break;
            case "Class section - Lecture":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.class_section_lecture);
                break;
            case "Exam":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.exam);
                break;
            case "Web Assignment":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.web_assignment);
                break;
            case "Activity":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.activity);
                break;
            case "Class section - Small Group":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.class_section_small_group);
                break;
            case "Meeting":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.meeting);
                break;
            case "Cancellation":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.cancellation);
                break;
            case "Class session":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.class_session);
                break;
            case "Multidisciplinary Conference":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.multidisciplinary_conference);
                break;
            case "Class section - Discussion":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.class_section_discussion);
                break;
            case "Computer Session":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.computer_session);
                break;
            case "Quiz":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.quiz);
                break;
            case "Class section - Lab":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.class_section_lab);
                break;
            case "Deadline":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.deadline);
                break;
            case "Special event":
                bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.special_event);
                break;
            default:
                break;
        }
        return bitmap;
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

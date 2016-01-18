package org.sakaiproject.customviews.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.general.Actions;
import org.sakaiproject.sakai.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vasilis on 1/18/16.
 */
public class MemebershipAdapter extends RecyclerView.Adapter<MemebershipAdapter.ViewHolder> {

    private List<SiteData> membership;
    private Context context;

    public MemebershipAdapter(List<SiteData> membership, Context context) {
        this.membership = membership;
        this.context = context;
    }

    @Override
    public MemebershipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.site_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        vh.name = (TextView) v.findViewById(R.id.membership_name);
        vh.roster = (TextView) v.findViewById(R.id.membership_roster);
        vh.description = (TextView) v.findViewById(R.id.membership_description);
        vh.unjoin = (ImageView) v.findViewById(R.id.unjoin_imageview);

        return vh;
    }

    @Override
    public void onBindViewHolder(MemebershipAdapter.ViewHolder holder, int position) {
        holder.name.setText(membership.get(position).getTitle());
        //holder.roster.setText("");

        List<String> images = Actions.findImages(membership.get(position).getDescription());

        String description = Actions.deleteHtmlTags(membership.get(position).getDescription());

        if (description.length() > 60) {
            description = description.substring(0, 60);
            description += "...";
        }
        holder.description.setText(description);

        holder.unjoin.setImageDrawable(Actions.setCustomDrawableColor(context, R.mipmap.ic_unjoin, Color.RED));
    }

    @Override
    public int getItemCount() {
        return membership.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView unjoin;
        TextView name, roster, description;

        public ViewHolder(View v) {
            super(v);
        }
    }
}

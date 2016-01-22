package org.sakaiproject.customviews.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.api.site.actions.SiteUnJoin;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.general.Actions;
import org.sakaiproject.sakai.R;

import java.util.List;

/**
 * Created by vasilis on 1/18/16.
 */
public class MemebershipAdapter extends RecyclerView.Adapter<MemebershipAdapter.ViewHolder> {

    private List<SiteData> membership;
    private Context context;

    /**
     * MemebershipAdapter constructor
     *
     * @param membership the list with the sites and projects
     * @param context    the context
     */
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
        vh.description = (org.sakaiproject.customviews.TextViewWithImages) v.findViewById(R.id.membership_description);
        vh.unjoin = (ImageView) v.findViewById(R.id.unjoin_imageview);
        vh.unjoinLayout = (FrameLayout) v.findViewById(R.id.unjoin);
        return vh;
    }

    @Override
    public void onBindViewHolder(MemebershipAdapter.ViewHolder holder, final int position) {

        holder.name.setText(membership.get(position).getTitle());
        if (holder.roster.getText().toString().length() == 0) {
            holder.roster.setVisibility(View.GONE);
        } else {
            //holder.roster.setText("");
        }

        List<String> sounds = Actions.findAudios(membership.get(position).getDescription());

        String description = Actions.deleteHtmlTags(membership.get(position).getDescription());

        if (description.equals("")) {
            holder.description.setText(context.getString(R.string.no_description));
        } else {

            holder.description.setSiteData(membership.get(position));

            if (description.length() > 60) {
                description = description.substring(0, 60);
                description += "...";
            }
            holder.description.setText(description);
        }

        if (membership.get(position).getOwner().equals(User.getUserId())) {
            holder.unjoin.setVisibility(View.GONE);
        }

        holder.unjoin.setImageDrawable(Actions.setCustomDrawableColor(context, R.mipmap.ic_unjoin, Color.parseColor("#FE6363")));

        holder.unjoinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(((AppCompatActivity) context).getSupportActionBar().getThemedContext());

                adb.setTitle(context.getResources().getString(R.string.unjoin_message) + " " + membership.get(position).getTitle() + " ?");

                adb.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new UnJoinAsync(membership.get(position).getId()).execute();
                    }
                });

                adb.setNegativeButton(context.getResources().getString(R.string.cancel), null);

                Dialog d = adb.show();
            }
        });
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
        TextView name, roster;
        org.sakaiproject.customviews.TextViewWithImages description;
        FrameLayout unjoinLayout;

        public ViewHolder(View v) {
            super(v);
        }
    }

    private class UnJoinAsync extends AsyncTask<Void, Void, Boolean> {

        String siteId;

        public UnJoinAsync(String siteId) {
            this.siteId = siteId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return SiteUnJoin.unJoin(context.getString(R.string.url) + "/membership/unjoin/site/" + siteId);
        }

        @Override
        protected void onPostExecute(Boolean unjoin) {
            super.onPostExecute(unjoin);
            if (unjoin) {
                Toast.makeText(context, context.getResources().getString(R.string.successful_unjoined), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package org.sakaiproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.pojos.wiki.Comment;
import org.sakaiproject.sakai.CommentsActivity;
import org.sakaiproject.sakai.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by vspallas on 29/02/16.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> comments;
    private Context context;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.userName = (TextView) v.findViewById(R.id.current_status_user_name);
        viewHolder.date = (TextView) v.findViewById(R.id.current_status_user_date);
        viewHolder.post = (TextView) v.findViewById(R.id.current_status_post);
        viewHolder.replies = (LinearLayout) v.findViewById(R.id.view_more_replies);
        viewHolder.repliesCount = (TextView) v.findViewById(R.id.replies_count);
        viewHolder.firstReplyName = (TextView) v.findViewById(R.id.first_reply_user_name);
        viewHolder.firstReplyPost = (TextView) v.findViewById(R.id.first_reply_post);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Comment comment = comments.get(position);
        holder.userName.setText(comment.getCreatorDisplayName());

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM, H:mm", Locale.US);
        Date d = new Date(comment.getCreated());
        holder.date.setText(format.format(d));

        holder.post.setText(comment.getContent());

        if (comment.getChildComments() != null && comment.getChildComments().size() > 0) {
            holder.replies.setVisibility(View.VISIBLE);
            final List<Comment> childComments = comment.getChildComments();

            if (childComments.size() > 1) {
                holder.repliesCount.setVisibility(View.VISIBLE);
                holder.repliesCount.setText(String.format("%s %d %s", context.getResources().getString(R.string.view), childComments.size(), context.getResources().getString(R.string.more_replies)));
            } else {
                holder.repliesCount.setVisibility(View.GONE);
            }
            holder.firstReplyName.setText(childComments.get(0).getCreatorDisplayName());
            holder.firstReplyPost.setText(childComments.get(0).getContent());


            holder.replies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CommentsActivity.class).putExtra("comments", (Serializable) childComments).putExtra("title", context.getResources().getString(R.string.replies)));
                }
            });
        } else {
            holder.replies.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, date, post, repliesCount, firstReplyName, firstReplyPost;
        LinearLayout replies;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

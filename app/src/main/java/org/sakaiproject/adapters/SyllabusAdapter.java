package org.sakaiproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sakaiproject.api.pojos.syllabus.Item;
import org.sakaiproject.customviews.rich_textview.RichTextView;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.general.AttachmentType;
import org.sakaiproject.sakai.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by vasilis on 1/28/16.
 */
public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusAdapter.ViewHolder> {
    private List<Item> syllabusItems;
    private Context context;
    private FragmentActivity activity;
    private String pageid;

    public SyllabusAdapter(List<Item> syllabuseItems, Context context, FragmentActivity activity, String id) {
        this.syllabusItems = syllabuseItems;
        this.context = context;
        this.activity = activity;
        pageid = id;
    }

    public void setSyllabusItems(List<Item> syllabuseItems) {
        this.syllabusItems = syllabuseItems;
    }

    // SimpleDateFormat is not thread-safe, so give one to each thread
    private static final ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.US);
        }
    };

    public String formatIt(Date date) {
        return formatter.get().format(date);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.syllabus_item, parent, false);
        // set the view's size, margin, padding and layout parameters

        ViewHolder vh = new ViewHolder(v);

        vh.title = (TextView) v.findViewById(R.id.syllabus_title);
        vh.data = (RichTextView) v.findViewById(R.id.syllabus_data);
        vh.data.setSiteData(pageid);
        vh.delete = (ImageView) v.findViewById(R.id.delete_imageview);
        vh.deleteLayout = (FrameLayout) v.findViewById(R.id.delete_item);
        vh.attachements = (LinearLayout) v.findViewById(R.id.attachments_linear);
        vh.date = (TextView) v.findViewById(R.id.syllabus_date);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.title.setText(syllabusItems.get(position).getTitle());

        String data = null;
        if (syllabusItems.get(position).getData() != null) {
            data = ActionsHelper.deleteHtmlTags(syllabusItems.get(position).getData());
            holder.data.setVisibility(View.VISIBLE);
            holder.data.setText(data);
        } else {
            holder.data.setVisibility(View.GONE);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.delete.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_delete, context.getResources().getColor(R.color.delete_red, context.getTheme())));
        } else {
            holder.delete.setImageDrawable(ActionsHelper.setCustomDrawableColor(context, R.mipmap.ic_delete, context.getResources().getColor(R.color.delete_red)));
        }


        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        if (syllabusItems.get(position).getStartDate() <= 0 && syllabusItems.get(position).getEndDate() <= 0) {
            holder.date.setVisibility(View.GONE);
        } else {
            holder.date.setVisibility(View.VISIBLE);

            if (syllabusItems.get(position).getStartDate() > 0)
                holder.date.setText(formatIt(new Date(syllabusItems.get(position).getStartDate())));

            if (syllabusItems.get(position).getEndDate() > 0) {
                if (syllabusItems.get(position).getStartDate() > 0)
                    holder.date.append(" - ");
                holder.date.append(formatIt(new Date(syllabusItems.get(position).getEndDate())));
            }
        }

        if (syllabusItems.get(position).getAttachments().size() > 0) {
            holder.attachements.setVisibility(View.VISIBLE);
            // add attachments to the attachment linear layout on the fly
            for (int i = 0; i < syllabusItems.get(position).getAttachments().size(); i++) {
                View currentAttachment = holder.attachements.inflate(activity, R.layout.attachment_row, null);

                TextView name = (TextView) currentAttachment.findViewById(R.id.attachment_name);

                String url = syllabusItems.get(position).getAttachments().get(i).getUrl();

                try {

                    url = url.substring(url.lastIndexOf('/') + 1);

                    if (ActionsHelper.getAttachmentType(url) != AttachmentType.URL) {
                        name.setText(URLDecoder.decode(url, "UTF-8").substring(url.lastIndexOf('/') + 1));
                    } else {
                        name.setText(URLDecoder.decode(url, "UTF-8").replaceAll("_", "/"));
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                ImageView image = (ImageView) currentAttachment.findViewById(R.id.attachment_type_image);

                image.setImageBitmap(ActionsHelper.getAttachmentTypeImage(context, url));

                holder.attachements.addView(currentAttachment);
            }
        } else {
            holder.attachements.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return syllabusItems != null ? syllabusItems.size() : 0;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView delete;
        TextView title;
        RichTextView data;
        LinearLayout attachements;
        FrameLayout deleteLayout;
        TextView date;

        public ViewHolder(View v) {
            super(v);
        }
    }
}

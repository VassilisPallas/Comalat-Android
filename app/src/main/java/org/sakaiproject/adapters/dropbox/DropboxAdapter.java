package org.sakaiproject.adapters.dropbox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sakaiproject.sakai.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by vspallas on 03/03/16.
 */
public class DropboxAdapter extends RecyclerView.Adapter<DropboxAdapter.ViewHolder> {
    private List<ListItem> dropboxList;
    String siteId;

    public DropboxAdapter(List<ListItem> dropboxList, String siteId) {
        this.dropboxList = dropboxList;
        this.siteId = siteId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropbox_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.fileTypeImage = (ImageView) v.findViewById(R.id.image_type);
        vh.fileTypeImageSmall = (ImageView) v.findViewById(R.id.image_type_small);
        vh.fileName = (TextView) v.findViewById(R.id.file_name);
        vh.fileSize = (TextView) v.findViewById(R.id.file_size);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem item = dropboxList.get(position);

        try {
            String name = URLDecoder.decode(item.getTitle(), "UTF-8");
            if (name.length() > 17) {
                String ex = name.substring(name.lastIndexOf('.'));
                name = name.substring(0, 10);
                holder.fileName.setText(String.format("%s..%s", name, ex));
            } else
                holder.fileName.setText(name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        holder.fileTypeImage.setImageBitmap(item.getImage());
        holder.fileTypeImageSmall.setImageBitmap(item.getSmallImage());
        holder.fileSize.setText(convertToStringRepresentation(item.getSize()));
    }

    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = M * K;
    private static final long T = G * K;

    public static String convertToStringRepresentation(final long value) {
        final long[] dividers = new long[]{T, G, M, K, 1};
        final String[] units = new String[]{"TB", "GB", "MB", "KB", "B"};
        if (value < 0)
            throw new IllegalArgumentException("Invalid file size: " + value);
        String result = null;
        for (int i = 0; i < dividers.length; i++) {
            final long divider = dividers[i];
            if (value >= divider) {
                result = format(value, divider, units[i]);
                break;
            }
        }
        return result;
    }

    private static String format(final long value,
                                 final long divider,
                                 final String unit) {
        final double result =
                divider > 1 ? (double) value / (double) divider : (double) value;
        return new DecimalFormat("#,##0.#").format(result) + " " + unit;
    }


    @Override
    public int getItemCount() {
        return dropboxList != null ? dropboxList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fileTypeImage, fileTypeImageSmall;
        TextView fileName, fileSize;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

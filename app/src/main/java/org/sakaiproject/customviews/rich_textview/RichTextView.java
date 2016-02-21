package org.sakaiproject.customviews.rich_textview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Image;
import org.sakaiproject.sakai.AppController;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vasilis on 1/18/16.
 */
interface ImageCallback {
    void success(Spannable spannable, Context context, Image im);

    Drawable error();

    Drawable placeholder();
}

public class RichTextView extends TextView implements ImageCallback, Html.ImageGetter {

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    private String id;
    private Bitmap bitmap;
    private File image;
    private File path;
    private int width;
    private int height;
    private boolean set;
    private Matcher refImgMatcher;
    private List<Image> images;
    private Context context;
    private Spannable spannable;
    private String imageName;
    private boolean addMore = false;
    private CharSequence tempText;
    private boolean downloadError = false;

    public RichTextView(Context context) {
        super(context);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAddMore(boolean addMore) {
        this.addMore = addMore;
    }

    public void setSiteData(String id) {
        this.id = id;
    }

    public void setContext(Context context) {
        this.context = context;
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private Spannable getRichText(Context context, CharSequence text) {
        images = new ArrayList<>();
        spannable = spannableFactory.newSpannable(text);
        //addImage(context, spannable);
        return spannable;
    }

    private Editable addMoreTag(Spannable s) {
        Editable editable = new SpannableStringBuilder((Html.fromHtml(s.toString(), this, new HtmlHandler(context))).subSequence(0, 60));

        editable = editable.insert(editable.toString().length(), "...");
        int from = editable.length();
        editable = editable.insert(editable.toString().length(), "\n[Tap for More]");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent, context.getTheme())), from, editable.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        } else
            editable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent)), from, editable.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return editable;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        Spannable s = getRichText(getContext(), text);
        tempText = s;

        if (addMore && text.length() > 60) {
            super.setText(addMoreTag(s), BufferType.EDITABLE);
        } else {
            super.setText(Html.fromHtml(s.toString(), this, new HtmlHandler(context)), BufferType.SPANNABLE);
        }
    }

    @Override
    public void success(Spannable spannable, Context context, Image im) {

    }

    @Override
    public Drawable error() {
        Resources resources = context.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resources.getDrawable(R.mipmap.ic_broken_image, context.getTheme());
        }
        return resources.getDrawable(R.mipmap.ic_broken_image);
    }

    @Override
    public Drawable placeholder() {
        Resources resources = context.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resources.getDrawable(R.mipmap.ic_image, context.getTheme());
        }
        return resources.getDrawable(R.mipmap.ic_image);
    }

    @Override
    public Drawable getDrawable(final String source) {
        Resources resources = context.getResources();
        Drawable res;

        // the move cursor for font awesome starts with data:
        if (!source.startsWith("data:")) {
            imageName = source.substring(source.lastIndexOf("/") + 1, source.lastIndexOf("."));
            res = findDrawableImage(imageName);
            if (res != null) {
                res.setBounds(0, 0, res.getIntrinsicWidth(), res.getIntrinsicHeight());
                return res;
            } else {
                if (id != null) {
                    if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + id)) {
                        path = new File(context.getFilesDir(), User.getUserEid() + File.separator + "memberships" + File.separator + id);
                    }
                    imageName = id + "_" + source.substring(source.lastIndexOf("/") + 1, source.length());
                    res = findImageFromPath(path.getAbsolutePath(), imageName);

                    if (res == null) {
                        // download
                        res = placeholder();

                        ImageRequest imageRequest = new ImageRequest(source, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {

                                if (id != null) {
                                    if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + id))
                                        try {
                                            Actions.saveImage(context, response, imageName, User.getUserEid() + File.separator + "memberships" + File.separator + id);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                }

                                Spannable s = getRichText(getContext(), tempText);
                                setText(s, BufferType.SPANNABLE);
                            }
                        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                downloadError = true;
                                Spannable s = getRichText(getContext(), tempText);
                                setText(s, BufferType.SPANNABLE);
                            }
                        });

                        AppController.getInstance().addToRequestQueue(imageRequest, imageName + " " + (id != null ? id : ""));
                    }

                }
            }
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                res = resources.getDrawable(R.drawable.empty, context.getTheme());
            } else {
                res = resources.getDrawable(R.drawable.empty);
            }
            return res;
        }

        if (res != null) {
            if (downloadError)
                res = error();
            res.setBounds(0, 0, res.getIntrinsicWidth(), res.getIntrinsicHeight());
            return res;
        }

        return null;
    }

    private Drawable findDrawableImage(String name) {
        Resources resources = context.getResources();
        int identifier;
        Drawable res = null;
        identifier = resources.getIdentifier(name, "drawable", context.getPackageName());

        if (identifier != 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                res = resources.getDrawable(identifier, context.getTheme());
            } else {
                res = resources.getDrawable(identifier);
            }
        return res;
    }

    private Drawable findImageFromPath(String path, String name) {
        Drawable res;
        res = Drawable.createFromPath(path + File.separator + name);

        if (res != null) {
            if (res.getIntrinsicWidth() < 20)
                res.setBounds(0, 0, res.getIntrinsicWidth() * 10, res.getIntrinsicHeight() * 10);
            else if (res.getIntrinsicWidth() < 150)
                res.setBounds(0, 0, res.getIntrinsicWidth() * 4, res.getIntrinsicHeight() * 4);
            else
                res.setBounds(0, 0, res.getIntrinsicWidth(), res.getIntrinsicHeight());
            return res;
        }
        return null;
    }

    private int getDrawableId(String name) {
        Resources resources = context.getResources();
        int identifier;
        identifier = resources.getIdentifier(name, "drawable", context.getPackageName());

        return identifier;
    }


}
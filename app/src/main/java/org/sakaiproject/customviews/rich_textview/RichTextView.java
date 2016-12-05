package org.sakaiproject.customviews.rich_textview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;

import org.sakaiproject.api.user.User;
import org.sakaiproject.helpers.ActionsHelper;
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
public class RichTextView extends TextView implements Html.ImageGetter {

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    private String id;
    private File path;
    private File image;
    private Context context;
    private String imageName;
    private boolean addMore = false;
    private CharSequence tempText;
    private int width;
    private int height;
    private Bitmap bitmap;
    private boolean set;
    private Matcher refImgMatcher;
    private List<Image> images;
    private URLDrawable urlDrawable = new URLDrawable();

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
        setMovementMethod(CustomLinkMovementMethod.getInstance(context));
    }

    private void addImage(final Context context, final Spannable spannable) {
        if (id != null)
            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + id)) {
                path = new File(context.getFilesDir(), User.getUserEid() + File.separator + "memberships" + File.separator + id);
            }

        Pattern refImgPattern = Pattern.compile("<img .+?\\/>");

        refImgMatcher = refImgPattern.matcher(spannable);
        while (refImgMatcher.find()) {


            String imageUrl = spannable.subSequence(refImgMatcher.start(0), refImgMatcher.end(0)).toString().trim();

            width = 0;
            Pattern widthPattern = Pattern.compile("width=\"[0-9]+?\"");
            Matcher widthMatcher = widthPattern.matcher(imageUrl);

            if (widthMatcher.find()) {
                String w = widthMatcher.group(0);
                w = w.replaceAll("width=", "");
                w = w.replaceAll("\"", "");
                width = Integer.valueOf(w);
            }

            height = 0;
            Pattern heightPattern = Pattern.compile("height=\"[0-9]+?\"");
            Matcher heightMatcher = heightPattern.matcher(imageUrl);

            if (heightMatcher.find()) {
                String h = heightMatcher.group(0);
                h = h.replaceAll("height=", "");
                h = h.replaceAll("\"", "");
                height = Integer.valueOf(h);
            }

            Pattern urlPattern = Pattern.compile("(http|ftp|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_ -]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])?");
            Matcher urlMatcher = urlPattern.matcher(imageUrl);

            if (urlMatcher.find()) {
                imageUrl = urlMatcher.group(0);
                imageName = id + "_" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
                images.add(new Image(imageUrl, imageName, width, height, refImgMatcher.start(0), refImgMatcher.end(0)));
            }

        }

        if (images.size() > 0) {

            for (final Image img : images) {
                image = new File(path, img.getName());

                // image does not exists on hte internal storage
                if (!image.exists()) {
                    // search on the drawables
                    String drawableName = img.getName().substring(0, img.getName().lastIndexOf('.')).replace(id + "_", "");
                    int identifier = getDrawableId(drawableName);

                    if (identifier == 0) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), identifier);
                        downloadImage(context, img);
                    }
                }
            }
        }

    }


    private void downloadImage(final Context context, final Image img) {

        ImageRequest imageRequest = new ImageRequest(img.getPath(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                if (id != null) {
                    if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + id))
                        try {
                            ActionsHelper.saveImage(context, response, img.getName(), User.getUserEid() + File.separator + "memberships" + File.separator + id);
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
                VolleyLog.d(img.getName() + " " + (id != null ? id : ""), error);
            }
        });

        AppController.getInstance().addToRequestQueue(imageRequest, img.getName() + " " + (id != null ? id : ""));
    }

    private int getDrawableId(String name) {
        Resources resources = context.getResources();
        int identifier;
        identifier = resources.getIdentifier(name, "drawable", context.getPackageName());

        return identifier;
    }

    private Spannable getRichText(Context context, CharSequence text) {
        images = new ArrayList<>();
        Spannable spannable = spannableFactory.newSpannable(text);
        addImage(context, spannable);
        return spannable;
    }

    private Editable addMoreTag(Spannable s) {
        Editable editable = new SpannableStringBuilder((Html.fromHtml(s.toString(), this, new HtmlHandler(context))).subSequence(0, 56));

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


    public Drawable placeholder(int identifier) {
        Drawable urlDrawable;
        Resources resources = context.getResources();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            urlDrawable = resources.getDrawable(identifier, context.getTheme());
        } else {
            urlDrawable = resources.getDrawable(identifier);
        }
        if (urlDrawable != null) {
            urlDrawable.setBounds(0, 0, urlDrawable.getIntrinsicWidth(), urlDrawable.getIntrinsicHeight());
            return urlDrawable;

        }
        return null;
    }

    private Drawable findImageFromPath(File file) {

        Drawable urlDrawable = Drawable.createFromPath(file.getAbsolutePath());

        if (urlDrawable != null) {

            if (urlDrawable.getIntrinsicWidth() < 20)
                urlDrawable.setBounds(0, 0, urlDrawable.getIntrinsicWidth() * 10, urlDrawable.getIntrinsicHeight() * 10);
            else if (urlDrawable.getIntrinsicWidth() < 150)
                urlDrawable.setBounds(0, 0, urlDrawable.getIntrinsicWidth() * 4, urlDrawable.getIntrinsicHeight() * 4);
            else
                urlDrawable.setBounds(0, 0, urlDrawable.getIntrinsicWidth() * 10, urlDrawable.getIntrinsicHeight() * 10);

            return urlDrawable;
        }

        return null;
    }

    private Drawable findDrawableImage(String name) {
        //int size = (int) (-getPaint().ascent());

        Drawable urlDrawable = new URLDrawable();
        Resources resources = context.getResources();
        int identifier;
        identifier = resources.getIdentifier(name, "drawable", context.getPackageName());
        if (identifier != 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                urlDrawable = resources.getDrawable(identifier, context.getTheme());
            } else {
                urlDrawable = resources.getDrawable(identifier);
            }
        if (urlDrawable != null) {
            urlDrawable.setBounds(0, 0, urlDrawable.getIntrinsicWidth(), urlDrawable.getIntrinsicHeight());
            return urlDrawable;
        }
        return null;
    }


    @Override
    public Drawable getDrawable(String source) {
        if (id != null)
            if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + id)) {
                path = new File(context.getFilesDir(), User.getUserEid() + File.separator + "memberships" + File.separator + id);
            }

        Pattern urlPattern = Pattern.compile("(http|ftp|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_ -]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])?");
        Matcher urlMatcher = urlPattern.matcher(source);

        if (urlMatcher.find()) {
            imageName = (id != null ? id + "_" : "") + source.substring(source.lastIndexOf("/") + 1, source.length());
            image = new File(path, imageName);

            if (!image.exists()) {
                String drawableName = imageName.substring(0, imageName.lastIndexOf('.')).replace(id + "_", "");

                int identifier = getDrawableId(drawableName);
                if (identifier != 0) {
                    urlDrawable.setDrawable(findDrawableImage(drawableName));
                } else {
                    urlDrawable.setDrawable(placeholder(R.mipmap.ic_image));
                }
            } else {
                urlDrawable.setDrawable(findImageFromPath(image));
            }
        } else {
            urlDrawable.setDrawable(placeholder(R.drawable.empty));
        }

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable.getDrawable();
    }
}
package org.sakaiproject.customviews.rich_textview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

    void error(Spannable spannable, Context context, Image im);

    void placeholder(Spannable spannable, Context context, Image im);
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
    Drawable res = null;

    public RichTextView(Context context) {
        super(context);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSiteData(String id) {
        this.id = id;
    }

    public void setContext(Context context) {
        this.context = context;
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addImage(final Context context, final Spannable spannable) {
        if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + id)) {
            path = new File(context.getFilesDir(), User.getUserEid() + File.separator + "memberships" + File.separator + id);
        }

        Pattern refImgPattern = Pattern.compile("<img .+?\\/>");

        refImgMatcher = refImgPattern.matcher(spannable);
        while (refImgMatcher.find()) {

            set = true;
            for (ImageSpan span : spannable.getSpans(refImgMatcher.start(), refImgMatcher.end(), ImageSpan.class)) {
                if (spannable.getSpanStart(span) >= refImgMatcher.start()
                        && spannable.getSpanEnd(span) <= refImgMatcher.end()
                        ) {
                    spannable.removeSpan(span);
                } else {
                    set = false;
                    break;
                }
            }

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

                if (id != null)
                    imageName = id + "_" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
                else
                    imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
                images.add(new Image(imageUrl, imageName, width, height, refImgMatcher.start(0), refImgMatcher.end(0)));
            }

        }

        if (images.size() > 0) {
            for (final Image img : images) {
                image = new File(path, img.getName());

                if (!image.exists()) {

                    int identifier = getDrawableId(img.getName().substring(0, img.getName().lastIndexOf('.')));
//                    if (identifier != 0) {
////                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), identifier);
////                        loadBitmapFromResources(spannable, context, img, bitmap);
//                        continue;
//                    }
                    if (identifier == 0) {
                        placeholder(spannable, context, img);

                        ImageRequest imageRequest = new ImageRequest(img.getPath(), new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {

                                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + id))
                                    try {
                                        Actions.saveImage(context, response, img.getName(), User.getUserEid() + File.separator + "memberships" + File.separator + id);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                success(spannable, context, img);
                                Spannable s = getRichText(getContext(), tempText);
                                setText(s, BufferType.SPANNABLE);
                            }
                        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error(spannable, context, img);
                                Spannable s = getRichText(getContext(), tempText);
                                setText(s, BufferType.SPANNABLE);
                            }
                        });

                        AppController.getInstance().addToRequestQueue(imageRequest, img.getName() + " " + id);
                    }
                } else
                    success(spannable, context, img);
            }
        }

    }


    private Spannable getRichText(Context context, CharSequence text) {
        images = new ArrayList<>();
        spannable = spannableFactory.newSpannable(text);
        addImage(context, spannable);
        return spannable;
    }

    private CharSequence tempText;

    @Override
    public void setText(CharSequence text, BufferType type) {
        Spannable s = getRichText(getContext(), text);
        tempText = s;
        super.setText(Html.fromHtml(text.toString(), this, new HtmlHandler(context)), BufferType.SPANNABLE);
    }

    private void loadBitmapFromResources(Spannable spannable, Context context, Image im, Bitmap bitmap) {
        if (im.getWidth() > 0 && im.getHeight() > 0)
            bitmap = Bitmap.createScaledBitmap(bitmap, im.getWidth() * 3, im.getHeight() * 3, true);

        if (set) {
            spannable.setSpan(new ImageSpan(context, bitmap),
                    im.getStartIndex(),
                    im.getEndIndex(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    @Override
    public void success(Spannable spannable, Context context, Image im) {
        if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + id)) {
            path = new File(context.getFilesDir(), User.getUserEid() + File.separator + "memberships" + File.separator + id);
            image = new File(path, im.getName());
        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        if (im.getWidth() > 0 && im.getHeight() > 0)
            bitmap = Bitmap.createScaledBitmap(bitmap, im.getWidth() * 3, im.getHeight() * 3, true);

        if (set) {
            spannable.setSpan(new ImageSpan(context, bitmap),
                    im.getStartIndex(),
                    im.getEndIndex(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    @Override
    public void error(Spannable spannable, Context context, Image im) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_broken_image, bmOptions);

        if (im.getWidth() > 0 && im.getHeight() > 0)
            bitmap = Bitmap.createScaledBitmap(bitmap, im.getWidth() * 3, im.getHeight() * 3, true);

        if (set) {
            spannable.setSpan(new ImageSpan(context, bitmap),
                    im.getStartIndex(),
                    im.getEndIndex(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    @Override
    public void placeholder(Spannable spannable, Context context, Image im) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_image, bmOptions);

        if (im.getWidth() > 0 && im.getHeight() > 0)
            bitmap = Bitmap.createScaledBitmap(bitmap, im.getWidth() * 3, im.getHeight() * 3, true);

        if (set) {
            spannable.setSpan(new ImageSpan(context, bitmap),
                    im.getStartIndex(),
                    im.getEndIndex(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    private Drawable findDrawableImage(String name) {
        Resources resources = context.getResources();
        int identifier;
        identifier = resources.getIdentifier(name, "drawable", context.getPackageName());
        if (identifier != 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                res = resources.getDrawable(identifier, context.getTheme());
            } else {
                res = resources.getDrawable(identifier);
            }
        if (res != null) {
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

    private Drawable findImageFromPath(String path, String name) {

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


    @Override
    public Drawable getDrawable(final String source) {
        Resources resources = context.getResources();
        int identifier;

        if (!source.startsWith("data:")) {

            imageName = source.substring(source.lastIndexOf("/") + 1, source.lastIndexOf("."));
            identifier = resources.getIdentifier(imageName, "drawable", context.getPackageName());
            if (identifier != 0)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    res = resources.getDrawable(identifier, context.getTheme());
                } else {
                    res = resources.getDrawable(identifier);
                }
            //res = findDrawableImage(imageName);

            if (res != null) {
                res.setBounds(0, 0, res.getIntrinsicWidth(), res.getIntrinsicHeight());
                return res;
            } else {

                if (Actions.createDirIfNotExists(context, User.getUserEid() + File.separator + "memberships" + File.separator + id)) {
                    path = new File(context.getFilesDir(), User.getUserEid() + File.separator + "memberships" + File.separator + id);
                }

                imageName = id + "_" + source.substring(source.lastIndexOf("/") + 1, source.length());

                //res = findImageFromPath(path.getAbsolutePath(), imageName);

                res = Drawable.createFromPath(path + File.separator + imageName);

                if (res != null) {

                    if (res.getIntrinsicWidth() < 20)
                        res.setBounds(0, 0, res.getIntrinsicWidth() * 10, res.getIntrinsicHeight() * 10);
                    else if (res.getIntrinsicWidth() < 150)
                        res.setBounds(0, 0, res.getIntrinsicWidth() * 4, res.getIntrinsicHeight() * 4);
                    else
                        res.setBounds(0, 0, res.getIntrinsicWidth(), res.getIntrinsicHeight());
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
        return res;
    }
}
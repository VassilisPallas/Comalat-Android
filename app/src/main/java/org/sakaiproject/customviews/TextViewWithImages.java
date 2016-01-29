package org.sakaiproject.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.api.user.User;
import org.sakaiproject.general.Actions;
import org.sakaiproject.general.Connection;
import org.sakaiproject.general.ConnectionType;
import org.sakaiproject.general.Image;
import org.sakaiproject.sakai.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vasilis on 1/18/16.
 */
public class TextViewWithImages extends TextView {

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    private String id;
    private Bitmap bitmap;
    private File image;
    private File path;
    private String imageName;
    private int width;
    private int height;
    private boolean set;
    private Matcher refImgMatcher;
    private List<Image> images;

    public TextViewWithImages(Context context) {
        super(context);
    }

    public TextViewWithImages(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewWithImages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSiteData(String id) {
        this.id = id;
    }


    private void add(final Context context, final Spannable spannable) {

        if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + id)) {
            path = new File(context.getFilesDir(), User.getUserId() + File.separator + "memberships" + File.separator + id);
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

            if (urlMatcher.find())
                imageUrl = urlMatcher.group(0);

            imageName = id + "_" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());

            images.add(new Image(imageUrl, imageName, width, height, refImgMatcher.start(0), refImgMatcher.end(0)));


        }

        if (images.size() > 0) {


            for (final Image img : images) {
                image = new File(path, img.getName());
                if (!image.exists()) {
                    new DownLoadImage(context, spannable, img).execute();
                    loadingImage(spannable, context, img);
                } else
                    addImages(spannable, context, img);
            }
        }

    }

    private Spannable getTextWithImages(Context context, CharSequence text) {
        images = new ArrayList<>();

        Spannable spannable = spannableFactory.newSpannable(text);
        add(context, spannable);
        return spannable;
    }


    private void loadingImage(Spannable spannable, Context context, Image im) {
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

    private void addImages(Spannable spannable, Context context, Image im) {
        if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + id)) {
            path = new File(context.getFilesDir(), User.getUserId() + File.separator + "memberships" + File.separator + id);
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

    private CharSequence tempText;

    @Override
    public void setText(CharSequence text, BufferType type) {
        Spannable s = getTextWithImages(getContext(), text);
        tempText = s;
        super.setText(s, BufferType.SPANNABLE);
    }


    private class DownLoadImage extends AsyncTask<Void, Void, Boolean> {

        private Connection connection = Connection.getInstance();
        private Context context;
        private Spannable spannable;
        private Image img;

        public DownLoadImage(Context context, Spannable spannable, Image image) {
            this.spannable = spannable;
            this.context = context;
            this.img = image;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                connection.openConnection(img.getPath(), ConnectionType.GET, false, false, null);

                Integer status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (Actions.createDirIfNotExists(context, User.getUserId() + File.separator + "memberships" + File.separator + id))
                        Actions.saveImage(context, bitmap, img.getName(), User.getUserId() + File.separator + "memberships" + File.separator + id);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.closeConnection();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean downloaded) {
            super.onPostExecute(downloaded);
            if (downloaded) {
                addImages(spannable, context, img);
                Spannable s = getTextWithImages(getContext(), tempText);
                setText(s, BufferType.SPANNABLE);
            }
        }
    }
}

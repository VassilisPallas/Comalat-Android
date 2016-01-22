package org.sakaiproject.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.general.Actions;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vasilis on 1/18/16.
 */
public class TextViewWithImages extends TextView {

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();
    private static SiteData siteData;
    private static Bitmap bitmap;

    public TextViewWithImages(Context context) {
        super(context);
    }

    public TextViewWithImages(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewWithImages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSiteData(SiteData siteData) {
        this.siteData = siteData;
    }

    private static boolean addImages(Context context, Spannable spannable) {
        Pattern refImgPattern = Pattern.compile("<img .+?\\/>");
        boolean hasChanges = false;

        Matcher refImgMatcher = refImgPattern.matcher(spannable);
        while (refImgMatcher.find()) {
            boolean set = true;
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

            int width = 0;
            Pattern widthPattern = Pattern.compile("width=\"[0-9]+?\"");
            Matcher widthMatcher = widthPattern.matcher(imageUrl);

            if (widthMatcher.find()) {
                String w = widthMatcher.group(0);
                w = w.replaceAll("width=", "");
                w = w.replaceAll("\"", "");
                width = Integer.valueOf(w);
            }

            int height = 0;
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

            Log.i("url", imageUrl);
            Log.i("width", String.valueOf(width));
            Log.i("height", String.valueOf(height));

            String imageName = siteData.getId() + "_" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());

            File path = context.getFilesDir();

            File image = new File(path, imageName);

            if (!image.exists()) {
                Actions.downloadAndSaveImage(imageUrl, context, imageName);
                image = new File(path, imageName);
            }

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            if (width > 0 && height > 0)
                bitmap = Bitmap.createScaledBitmap(bitmap, width * 3, height * 3, true);

            if (set) {
                hasChanges = true;
                spannable.setSpan(new ImageSpan(context, bitmap),
                        refImgMatcher.start(),
                        refImgMatcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }


//            int id = context.getResources().getIdentifier(imageUrl, "drawable", context.getPackageName());
//
//
//            if (set) {
//                hasChanges = true;
//                spannable.setSpan(new ImageSpan(context, id),
//                        matcher.start(),
//                        matcher.end(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                );
//            }

        }

        return hasChanges;
    }

    private static Spannable getTextWithImages(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addImages(context, spannable);
        return spannable;
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        Spannable s = getTextWithImages(getContext(), text);
        super.setText(s, BufferType.SPANNABLE);
    }
}

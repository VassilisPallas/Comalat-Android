package org.sakaiproject.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by vasilis on 10/16/15.
 * This is the rounded imageview for the user's profile image
 */
public class ImageViewRounded extends ImageView {

    public ImageViewRounded(Context context) {
        super(context);
    }

    public ImageViewRounded(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewRounded(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap fullSizeBitmap = drawable.getBitmap();

        int scaledWidth = getMeasuredWidth();
        int scaledHeight = getMeasuredHeight();

        Bitmap mScaledBitmap;
        if (scaledWidth == fullSizeBitmap.getWidth()
                && scaledHeight == fullSizeBitmap.getHeight()) {
            mScaledBitmap = fullSizeBitmap;
        } else {
            mScaledBitmap = Bitmap.createScaledBitmap(fullSizeBitmap,
                    scaledWidth, scaledHeight, false /* filter */);

        }

        Bitmap circleBitmap = getCircledBitmap(mScaledBitmap);

        canvas.drawBitmap(circleBitmap, 0, 0, null);

    }

    Bitmap getCircledBitmap(Bitmap bitmap) {

        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(result);

        int color = Color.BLUE;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getHeight() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);


        return result;
    }

}

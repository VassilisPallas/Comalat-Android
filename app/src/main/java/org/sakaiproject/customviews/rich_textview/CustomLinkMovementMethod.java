package org.sakaiproject.customviews.rich_textview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;

import org.sakaiproject.sakai.WebViewActivity;

/**
 * Created by vspallas on 21/02/16.
 */
public class CustomLinkMovementMethod extends LinkMovementMethod {

    private static Context movementContext;

    private static CustomLinkMovementMethod linkMovementMethod = new CustomLinkMovementMethod();

    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0) {
                String url = link[0].getURL();
                if (url.startsWith("https") || url.startsWith("http")) {
                    movementContext.startActivity(new Intent(movementContext, WebViewActivity.class).putExtra("url", url));
                } else if (url.startsWith("tel")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(url));
                    movementContext.startActivity(intent);
                } else if (url.startsWith("mailto")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    //movementContext.startActivity(Intent.createChooser(emailIntent, "Choose email client"));
                    movementContext.startActivity(emailIntent);
                }
                return true;
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    public static android.text.method.MovementMethod getInstance(Context c) {
        movementContext = c;
        return linkMovementMethod;
    }
}
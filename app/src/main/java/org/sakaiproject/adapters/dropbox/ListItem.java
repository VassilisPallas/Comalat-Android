package org.sakaiproject.adapters.dropbox;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by vspallas on 08/03/16.
 */
public class ListItem implements Comparable<ListItem> {

    private Bitmap image, smallImage;
    private String title;
    private int size;

    public ListItem(Bitmap image, Bitmap smallImage, String title, int size) {
        super();
        this.image = image;
        this.smallImage = smallImage;
        this.title = title;
        this.size = size;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Bitmap getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(Bitmap smallImage) {
        this.smallImage = smallImage;
    }

    @Override
    public int compareTo(@NonNull ListItem another) {
        return this.title.toLowerCase().charAt(0) > another.title.toLowerCase().charAt(0) ? 1 : (this.title.toLowerCase().charAt(0) < another.title.toLowerCase().charAt(0) ? -1 : 0);
    }
}

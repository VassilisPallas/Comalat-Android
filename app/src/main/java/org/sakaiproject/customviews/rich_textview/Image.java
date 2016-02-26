package org.sakaiproject.customviews.rich_textview;

/**
 * Created by vasilis on 1/28/16.
 */
public class Image {
    private String path, name;
    private int width = 0, height = 0, startIndex, endIndex;

    public Image(String path, String name, int width, int height, int startIndex, int endIndex) {
        this.path = path;
        this.name = name;
        this.width = width;
        this.height = height;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
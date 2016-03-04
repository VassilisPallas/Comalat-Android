package org.sakaiproject.api.pojos.dropbox;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vspallas on 03/03/16.
 */
public class Dropbox {
    @SerializedName("dropbox_collection")
    private List<Collection> collection;

    public List<Collection> getCollection() {
        return collection;
    }

    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }

    public class Collection {
        private String description;
        private int size;
        private String title;
        private String type;
        private String url;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

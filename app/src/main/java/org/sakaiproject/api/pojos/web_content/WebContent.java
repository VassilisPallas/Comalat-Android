package org.sakaiproject.api.pojos.web_content;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vspallas on 01/03/16.
 */
public class WebContent {
    @SerializedName("webcontent_collection")
    List<Collection> collection = new ArrayList<>();

    public List<Collection> getCollection() {
        return collection;
    }

    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }

    public class Collection {
        private String title;
        private String url;
        private String entityReference;
        private String entityURL;
        private String entityTitle;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getEntityReference() {
            return entityReference;
        }

        public void setEntityReference(String entityReference) {
            this.entityReference = entityReference;
        }

        public String getEntityURL() {
            return entityURL;
        }

        public void setEntityURL(String entityURL) {
            this.entityURL = entityURL;
        }

        public String getEntityTitle() {
            return entityTitle;
        }

        public void setEntityTitle(String entityTitle) {
            this.entityTitle = entityTitle;
        }
    }
}

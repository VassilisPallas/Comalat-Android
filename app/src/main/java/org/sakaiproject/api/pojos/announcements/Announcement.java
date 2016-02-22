package org.sakaiproject.api.pojos.announcements;

import com.google.gson.annotations.SerializedName;

import org.sakaiproject.api.pojos.Attachment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vspallas on 17/02/16.
 */
public class Announcement implements Serializable {

    @SerializedName("announcement_collection")
    List<AnnouncementItems> announcementCollection;

    public List<AnnouncementItems> getAnnouncementCollection() {
        return announcementCollection;
    }

    public void setAnnouncementCollection(List<AnnouncementItems> announcementCollection) {
        this.announcementCollection = announcementCollection;
    }

    public static class AnnouncementItems implements Serializable {
        private String announcementId;
        private List<Attachment> attachments;
        private String body;
        private String channel;
        private String createdByDisplayName;
        private long createdOn;
        private String siteId;
        private String siteTitle;
        private String title;

        public String getAnnouncementId() {
            return announcementId;
        }

        public void setAnnouncementId(String announcementId) {
            this.announcementId = announcementId;
        }

        public List<Attachment> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<Attachment> attachments) {
            this.attachments = attachments;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCreatedByDisplayName() {
            return createdByDisplayName;
        }

        public void setCreatedByDisplayName(String createdByDisplayName) {
            this.createdByDisplayName = createdByDisplayName;
        }

        public long getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(long createdOn) {
            this.createdOn = createdOn;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public String getSiteTitle() {
            return siteTitle;
        }

        public void setSiteTitle(String siteTitle) {
            this.siteTitle = siteTitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

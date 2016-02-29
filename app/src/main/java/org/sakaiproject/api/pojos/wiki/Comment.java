package org.sakaiproject.api.pojos.wiki;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by vspallas on 28/02/16.
 */
public class Comment implements Serializable {
    private List<Comment> childComments;
    private String content;
    private long created;
    private String creatorDisplayName;

    public List<Comment> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<Comment> childComments) {
        this.childComments = childComments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getCreatorDisplayName() {
        return creatorDisplayName;
    }

    public void setCreatorDisplayName(String creatorDisplayName) {
        this.creatorDisplayName = creatorDisplayName;
    }
}

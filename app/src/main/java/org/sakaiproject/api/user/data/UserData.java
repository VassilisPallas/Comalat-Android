package org.sakaiproject.api.user.data;

import java.io.Serializable;
import java.util.Date;

import org.sakaiproject.api.time.Time;

/**
 * Created by vasilis on 10/13/15.
 * This class stores the data from user json
 * http://url/direct/user/userEid.json
 */
@SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
public class UserData implements Serializable {

    public UserData() {
    }

    private Date createdDate;
    private Time createdTime;
    private String displayId;
    private String displayName;
    private String eid;
    private String email;
    private String firstName;
    private String id;
    private long lastModified;
    private String lastName;
    private Date modifiedDate;
    private Time modifiedTime;
    private String owner;
    private String password;
    //private Map props;
    private String reference;
    private String sortName;
    private String type;
    private String url;
    private String entityTitle;
    private String entityReference;
    private String EntityURL;
    private String EntityId;

    public Date getCreatedDate() {
        return createdDate;
    }

    public Time getCreatedTime() {
        return createdTime;
    }

    public String getDisplayId() {
        return displayId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEid() {
        return eid;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public Time getModifiedTime() {
        return modifiedTime;
    }

    public String getOwner() {
        return owner;
    }

    public String getPassword() {
        return password;
    }

    public String getReference() {
        return reference;
    }

    public String getSortName() {
        return sortName;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getEntityTitle() {
        return entityTitle;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public String getSetEntityURL() {
        return EntityURL;
    }

    public String getSetEntityId() {
        return EntityId;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setCreatedTime(Time createdTime) {
        this.createdTime = createdTime;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setModifiedTime(Time modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEntityTitle(String entityTitle) {
        this.entityTitle = entityTitle;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public void setEntityURL(String setEntityURL) {
        this.EntityURL = setEntityURL;
    }

    public void setEntityId(String setEntityId) {
        this.EntityId = setEntityId;
    }

}

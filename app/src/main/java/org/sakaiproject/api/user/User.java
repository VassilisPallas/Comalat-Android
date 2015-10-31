package org.sakaiproject.api.user;

import org.sakaiproject.api.time.Time;

import java.util.Date;

/**
 * Created by vasilis on 10/26/15.
 */
public class User {
    private static User instance = null;

    private static Date createdDate;
    private static Time createdTime;
    private static String userEid;
    private static String userId;
    private static String email;
    private static String firstName;
    private static String lastName;
    private static String type;
    private static String sortName;
    private static Date modifiedDate;
    private static Time modifiedTime;

    private User() {
    }

    public static synchronized User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }

    public static synchronized void nullInstance() {
        if (instance != null)
            instance = null;
    }

    public static synchronized Date getCreatedDate() {
        return createdDate;
    }

    public static synchronized Time getCreatedTime() {
        return createdTime;
    }

    public static synchronized String getUserEid() {
        return userEid;
    }

    public static synchronized String getUserId() {
        return userId;
    }

    public static synchronized String getEmail() {
        return email;
    }

    public static synchronized String getFirstName() {
        return firstName;
    }

    public static synchronized String getLastName() {
        return lastName;
    }

    public static synchronized String getType() {
        return type;
    }

    public static synchronized String getSortName() {
        return sortName;
    }

    public static synchronized Date getModifiedDate() {
        return modifiedDate;
    }

    public static synchronized Time getModifiedTime() {
        return modifiedTime;
    }

    public static synchronized void setModifiedDate(Date modifiedDate) {
        User.modifiedDate = modifiedDate;
    }

    public static synchronized void setModifiedTime(Time modifiedTime) {
        User.modifiedTime = modifiedTime;
    }

    public static synchronized void setCreatedDate(Date createdDate) {
        User.createdDate = createdDate;
    }

    public static synchronized void setCreatedTime(Time createdTime) {
        User.createdTime = createdTime;
    }

    public static synchronized void setUserEid(String userEid) {
        User.userEid = userEid;
    }

    public static synchronized void setUserId(String userId) {
        User.userId = userId;
    }

    public static synchronized void setEmail(String email) {
        User.email = email;
    }

    public static synchronized void setFirstName(String firstName) {
        User.firstName = firstName;
    }

    public static synchronized void setLastName(String lastName) {
        User.lastName = lastName;
    }

    public static synchronized void setType(String type) {
        User.type = type;
    }

    public static synchronized void setSortName() {
        User.sortName = User.lastName + ", " + User.firstName;
    }
}

package org.sakaiproject.api.user;

import org.sakaiproject.api.pojos.Time;

import java.util.Date;
import java.util.Map;

/**
 * Created by vasilis on 10/26/15.
 * * a JavaBeans convention class for the User data
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
    private static Map<String, String> attribute;

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

    public static Date getCreatedDate() {
        return createdDate;
    }

    public static Time getCreatedTime() {
        return createdTime;
    }

    public static String getUserEid() {
        return userEid;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getEmail() {
        return email;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static String getType() {
        return type;
    }

    public static String getSortName() {
        return sortName;
    }

    public static Date getModifiedDate() {
        return modifiedDate;
    }

    public static Time getModifiedTime() {
        return modifiedTime;
    }

    public static void setModifiedDate(Date modifiedDate) {
        User.modifiedDate = modifiedDate;
    }

    public static void setModifiedTime(Time modifiedTime) {
        User.modifiedTime = modifiedTime;
    }

    public static void setCreatedDate(Date createdDate) {
        User.createdDate = createdDate;
    }

    public static void setCreatedTime(Time createdTime) {
        User.createdTime = createdTime;
    }

    public static void setUserEid(String userEid) {
        User.userEid = userEid;
    }

    public static void setUserId(String userId) {
        User.userId = userId;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static void setFirstName(String firstName) {
        User.firstName = firstName;
    }

    public static void setLastName(String lastName) {
        User.lastName = lastName;
    }

    public static void setType(String type) {
        User.type = type;
    }

    public static void setSortName() {
        User.sortName = User.lastName + ", " + User.firstName;
    }

    public static Map<String, String> getAttribute() {
        return attribute;
    }

    public static void setAttribute(Map<String, String> attribute) {
        User.attribute = attribute;
    }
}

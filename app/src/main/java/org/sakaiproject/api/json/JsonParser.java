package org.sakaiproject.api.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sakaiproject.api.online.motd.MessageOfTheDay;
import org.sakaiproject.api.time.Time;
import org.sakaiproject.api.online.user.data.UserData;
import org.sakaiproject.api.online.user.data.UserProfileData;
import org.sakaiproject.api.online.user.data.UserSessionData;

/**
 * Created by vasilis on 10/13/15.
 * This is the json parser for every response from the server
 */
public class JsonParser {

    public JsonParser() {
    }

    public UserSessionData parseLoginResult(String result) {

        UserSessionData userSessionData = new UserSessionData();

        try {
            JSONObject obj = new JSONObject(result);

            userSessionData.setAttributes(obj.optString("attributes"));
            userSessionData.setCreationTime(obj.optInt("creationTime"));
            userSessionData.setCurrentTime(obj.getInt("currentTime"));
            userSessionData.setSessionID(obj.optString("id"));
            userSessionData.setLastAccessedTime(obj.optInt("lastAccessedTime"));
            userSessionData.setMaxInactiveInterval(obj.optInt("maxInactiveInterval"));
            userSessionData.setUserEid(obj.optString("userEid"));
            userSessionData.setUserId(obj.optString("userId"));
            userSessionData.setActive(obj.optBoolean("active"));
            userSessionData.setEntityReference(obj.optString("entityReference"));
            userSessionData.setEntityURL(obj.optString("entityURL"));
            userSessionData.setEntityId(obj.optString("entityId"));

            // attributeNames -> Enum

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userSessionData;
    }

    public UserData parseUserDataJson(String result) {

        UserData userData = new UserData();

        try {
            JSONObject obj = new JSONObject(result);
            JSONObject createdTimeJson = obj.getJSONObject("createdTime");
            JSONObject modifiedTimeJson = obj.getJSONObject("modifiedTime");

            if (!obj.optString("createdDate").equals("null"))
                userData.setCreatedDate(new Date(Long.parseLong(obj.optString("createdDate"))));
            else
                userData.setCreatedDate(new Date(0));
            userData.setCreatedTime(new Time(createdTimeJson.optString("display"), new Date(Long.parseLong(createdTimeJson.optString("time")))));
            userData.setDisplayId(obj.optString("displayId"));
            userData.setDisplayName(obj.optString("displayName"));
            userData.setEid(obj.optString("eid"));
            userData.setEmail(obj.optString("email"));
            userData.setFirstName(obj.getString("firstName"));
            userData.setId(obj.optString("id"));
            userData.setLastModified(obj.optInt("lastModified"));
            if (!obj.optString("modifiedDate").equals("null"))
                userData.setModifiedDate(new Date(Long.parseLong(obj.optString("modifiedDate"))));
            else
                userData.setModifiedDate(new Date(0));
            userData.setModifiedTime(new Time(modifiedTimeJson.optString("display"), new Date(Long.parseLong(modifiedTimeJson.optString("time")))));
            userData.setLastName(obj.optString("lastName"));
            userData.setOwner(obj.optString("owner"));
            userData.setPassword(obj.optString("password"));
            userData.setReference(obj.optString("reference"));
            userData.setSortName(obj.optString("sortName"));
            userData.setType(obj.optString("type"));
            userData.setUrl(obj.optString("url"));
            userData.setEntityReference(obj.optString("entityReference"));
            userData.setEntityURL(obj.optString("entityURL"));
            userData.setEntityId(obj.optString("entityId"));
            userData.setEntityTitle(obj.optString("entityTitle"));

            // props - > Map
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userData;
    }

    public UserProfileData parseUserProfileDataJson(String result) {

        UserProfileData userProfileData = new UserProfileData();

        try {

            JSONObject obj = new JSONObject(result);
            userProfileData.setAcademicProfileUrl(obj.optString("academicProfileUrl"));
            userProfileData.setBirthday(obj.optString("birthday"));
            userProfileData.setBirthdayDisplay(obj.optString("birthdayDisplay"));
            userProfileData.setBusinessBiography(obj.optString("businessBiography"));
            userProfileData.setCourse(obj.optString("course"));
            if (!obj.optString("dateOfBirth").equals("null"))
                userProfileData.setDateOfBirth(new Date(Long.parseLong(obj.optString("dateOfBirth"))));
            else
                userProfileData.setDateOfBirth(new Date(0));

            userProfileData.setDepartment(obj.optString("department"));
            userProfileData.setDisplayName(obj.optString("displayName"));
            userProfileData.setEmail(obj.optString("email"));
            userProfileData.setFacsimile(obj.optString("facsimile"));
            userProfileData.setFavouriteBooks(obj.optString("favouriteBooks"));
            userProfileData.setFavouriteMovies(obj.optString("favouriteMovies"));
            userProfileData.setFavouriteQuotes(obj.optString("favouriteQuotes"));
            userProfileData.setFavouriteTvShows(obj.optString("favouriteTvShows"));
            userProfileData.setHomepage(obj.optString("homepage"));
            userProfileData.setHomephone(obj.optString("homephone"));
            userProfileData.setImageThumbUrl(obj.optString("imageThumbUrl"));
            userProfileData.setImageUrl(obj.optString("imageUrl"));
            userProfileData.setMobilephone(obj.optString("mobilephone"));
            userProfileData.setNickname(obj.optString("nickname"));
            userProfileData.setPersonalSummary(obj.optString("personalSummary"));
            userProfileData.setPosition(obj.optString("position"));
            userProfileData.setPublications(obj.optString("publications"));
            userProfileData.setRoom(obj.optString("room"));
            userProfileData.setSchool(obj.optString("school"));
            userProfileData.setStaffProfile(obj.optString("staffProfile"));
            userProfileData.setSubjects(obj.optString("subjects"));
            userProfileData.setUniversityProfileUrl(obj.optString("universityProfileUrl"));
            userProfileData.setUserUuid(obj.optString("userUuid"));
            userProfileData.setWorkphone(obj.optString("workphone"));
            userProfileData.setLocked(obj.optBoolean("locked"));
            userProfileData.setEntityReference(obj.optString("entityReference"));
            userProfileData.setEntityURL(obj.optString("entityURL"));
            userProfileData.setEntityId(obj.optString("entityId"));

            // companyProfiles -> List
            // props -> Map
            // socialInfo -> SocialNetworkingInfo
            // status -> ProfileStatus
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userProfileData;
    }

    public MessageOfTheDay parseMotdJson(String result) {
        MessageOfTheDay messageOfTheDay = new MessageOfTheDay();
        List<String> messagesList = new ArrayList<>();
        List<String> siteUrlsList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray messages = jsonObject.getJSONArray("announcement_collection");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject obj = messages.optJSONObject(i);
                messagesList.add(obj.optString("motdBody"));
                siteUrlsList.add(obj.optString("motdUrl"));
            }

            messageOfTheDay.setMessage(messagesList);
            messageOfTheDay.setSiteUrl(siteUrlsList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageOfTheDay;
    }

}

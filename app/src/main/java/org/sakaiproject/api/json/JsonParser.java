package org.sakaiproject.api.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.motd.OnlineMessageOfTheDay;
import org.sakaiproject.api.time.Time;
import org.sakaiproject.api.user.data.Profile;
import org.sakaiproject.api.user.data.User;
import org.sakaiproject.api.user.data.UserEvents;

/**
 * Created by vasilis on 10/13/15.
 * This is the json parser for every response from the server
 */
public class JsonParser {

    Connection con;
    User user;
    Profile profile;

    public JsonParser() {
        con = Connection.getInstance();
        user = User.getInstance();
        profile = Profile.getInstance();
    }

    public void parseLoginResult(String result) {

        try {
            JSONObject obj = new JSONObject(result);

            con.setCreationTime(obj.optInt("creationTime"));
            con.setLastAccessedTime(obj.optInt("lastAccessedTime"));
            con.setMaxInactiveInterval(obj.optInt("maxInactiveInterval"));
            user.setUserEid(obj.optString("userEid"));
            user.setUserId(obj.optString("userId"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseUserDataJson(String result) {

        try {
            JSONObject obj = new JSONObject(result);
            JSONObject createdTimeJson = obj.getJSONObject("createdTime");
            JSONObject modifiedTimeJson = obj.getJSONObject("modifiedTime");

            if (!obj.optString("createdDate").equals("null"))
                user.setCreatedDate(new Date(Long.parseLong(obj.optString("createdDate"))));
            else
                user.setCreatedDate(new Date(0));
            user.setCreatedTime(new Time(createdTimeJson.optString("display"), new Date(Long.parseLong(createdTimeJson.optString("time")))));
            user.setEmail(obj.optString("email"));
            user.setFirstName(obj.getString("firstName"));
            if (!obj.optString("modifiedDate").equals("null"))
                user.setModifiedDate(new Date(Long.parseLong(obj.optString("modifiedDate"))));
            else
                user.setModifiedDate(new Date(0));
            user.setModifiedTime(new Time(modifiedTimeJson.optString("display"), new Date(Long.parseLong(modifiedTimeJson.optString("time")))));
            user.setLastName(obj.optString("lastName"));
            user.setType(obj.optString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseUserProfileDataJson(String result) {

        try {

            JSONObject obj = new JSONObject(result);
            profile.setAcademicProfileUrl(obj.optString("academicProfileUrl"));
            profile.setBirthday(obj.optString("birthday"));
            profile.setBirthdayDisplay(obj.optString("birthdayDisplay"));
            profile.setBusinessBiography(obj.optString("businessBiography"));
            profile.setCourse(obj.optString("course"));
//            if (!obj.optString("dateOfBirth").equals("null"))
//                profile.setDateOfBirth(new Date(Long.parseLong(obj.optString("dateOfBirth"))));
//            else
//                profile.setDateOfBirth(new Date(0));

            profile.setDepartment(obj.optString("department"));
            profile.setDisplayName(obj.optString("displayName"));
            profile.setFacsimile(obj.optString("facsimile"));
            profile.setFavouriteBooks(obj.optString("favouriteBooks"));
            profile.setFavouriteMovies(obj.optString("favouriteMovies"));
            profile.setFavouriteQuotes(obj.optString("favouriteQuotes"));
            profile.setFavouriteTvShows(obj.optString("favouriteTvShows"));
            profile.setHomepage(obj.optString("homepage"));
            profile.setHomephone(obj.optString("homephone"));
            profile.setImageThumbUrl(obj.optString("imageThumbUrl"));
            profile.setImageUrl(obj.optString("imageUrl"));
            profile.setMobilephone(obj.optString("mobilephone"));
            profile.setNickname(obj.optString("nickname"));
            profile.setPersonalSummary(obj.optString("personalSummary"));
            profile.setPosition(obj.optString("position"));
            profile.setPublications(obj.optString("publications"));
            profile.setRoom(obj.optString("room"));
            profile.setSchool(obj.optString("school"));
            profile.setStaffProfile(obj.optString("staffProfile"));
            profile.setSubjects(obj.optString("subjects"));
            profile.setUniversityProfileUrl(obj.optString("universityProfileUrl"));
            profile.setWorkphone(obj.optString("workphone"));
            profile.setLocked(obj.optBoolean("locked"));

            // companyProfiles -> List
            // props -> Map
            // socialInfo -> SocialNetworkingInfo
            // status -> ProfileStatus
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public OnlineMessageOfTheDay parseMotdJson(String result) {
        OnlineMessageOfTheDay onlineMessageOfTheDay = new OnlineMessageOfTheDay();
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

            onlineMessageOfTheDay.setMessage(messagesList);
            onlineMessageOfTheDay.setSiteUrl(siteUrlsList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return onlineMessageOfTheDay;
    }

    public List<UserEvents> parseUserEventJson(String result) {
        List<UserEvents> userEventsList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray collections = jsonObject.getJSONArray("calendar_collection");

            for (int i = 0; i < collections.length(); i++) {
                JSONObject obj = collections.optJSONObject(i);

                UserEvents userEvents = new UserEvents();

                JSONObject firstTimeJson = obj.getJSONObject("firstTime");

                userEvents.setCreator(obj.optString("creator"));
                userEvents.setDuration(obj.optInt("duration"));
                userEvents.setEventId(obj.optString("eventId"));
                userEvents.setFirstTime(new Time(firstTimeJson.optString("display"), new Date(Long.parseLong(firstTimeJson.optString("time")))));
                userEvents.setReference(obj.optString("reference"));
                userEvents.setSiteName(obj.optString("siteName"));
                userEvents.setTitle(obj.optString("title"));
                userEvents.setType(obj.optString("type"));
                userEvents.setEntityReference(obj.optString("entityReference"));
                userEvents.setEntityURL(obj.optString("entityURL"));
                userEvents.setEntityTitle(obj.optString("entityTitle"));

                userEvents.setEventTime();

                userEventsList.add(userEvents);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return userEventsList;
    }
}

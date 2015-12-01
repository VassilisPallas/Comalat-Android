package org.sakaiproject.api.json;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.events.OnlineEvents;
import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.motd.OnlineMessageOfTheDay;
import org.sakaiproject.api.site.OnlineSite;
import org.sakaiproject.api.site.SiteData;
import org.sakaiproject.api.time.Time;
import org.sakaiproject.api.user.profile.DateOfBirth;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.UserEvents;
import org.sakaiproject.api.events.RecurrenceRule;
import org.sakaiproject.api.user.profile.ProfileStatus;
import org.sakaiproject.api.user.profile.SocialNetworkingInfo;

/**
 * Created by vasilis on 10/13/15.
 * This is the json parser for every json response from the server
 */
public class JsonParser {

    private Connection con;
    private User user;
    private Profile profile;
    private Context context;

    public JsonParser(Context context) {
        this.context = context;
        con = Connection.getInstance();
        con.setContext(context);
        user = User.getInstance();
        profile = Profile.getInstance();
    }

    /**
     * parse the session json
     * http://141.99.248.86:8089/direct/session/sessionId.json
     *
     * @param result the response json
     */
    public void parseLoginResult(String result) {

        try {
            JSONObject obj = new JSONObject(result);

            con.setCreationTime(obj.getInt("creationTime"));
            con.setLastAccessedTime(obj.getInt("lastAccessedTime"));
            con.setMaxInactiveInterval(obj.getInt("maxInactiveInterval"));
            user.setUserEid(obj.getString("userEid"));
            user.setUserId(obj.getString("userId"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // attributeName
        // attribute
    }

    /**
     * parse the user data json
     * http://141.99.248.86:8089/direct/user/userEid.json
     *
     * @param result the response json
     */
    public void parseUserDataJson(String result) {

        try {
            JSONObject obj = new JSONObject(result);
            JSONObject createdTimeJson = obj.getJSONObject("createdTime");
            JSONObject modifiedTimeJson = obj.getJSONObject("modifiedTime");

            if (!obj.getString("createdDate").equals("null"))
                user.setCreatedDate(new Date(Long.parseLong(obj.getString("createdDate"))));
            else
                user.setCreatedDate(new Date(0));
            user.setCreatedTime(new Time(createdTimeJson.getString("display"), new Date(Long.parseLong(createdTimeJson.getString("time")))));
            user.setEmail(obj.getString("email"));
            user.setFirstName(obj.getString("firstName"));
            if (!obj.getString("modifiedDate").equals("null"))
                user.setModifiedDate(new Date(Long.parseLong(obj.getString("modifiedDate"))));
            else
                user.setModifiedDate(new Date(0));
            user.setModifiedTime(new Time(modifiedTimeJson.getString("display"), new Date(Long.parseLong(modifiedTimeJson.getString("time")))));
            user.setLastName(obj.getString("lastName"));
            user.setType(obj.getString("type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * parse the user profile data json
     * http://141.99.248.86:8089/direct/profile/userEid.json
     *
     * @param result the response json
     */
    public void parseUserProfileDataJson(String result) {

        try {

            JSONObject obj = new JSONObject(result);
            profile.setAcademicProfileUrl(obj.getString("academicProfileUrl"));
            profile.setBirthday(obj.getString("birthday"));
            profile.setBirthdayDisplay(obj.getString("birthdayDisplay"));
            profile.setBusinessBiography(obj.getString("businessBiography"));
            profile.setCourse(obj.getString("course"));


            if (!obj.isNull("dateOfBirth")) {
                JSONObject dateOfBirthObj = obj.getJSONObject("dateOfBirth");
                int date;
                int day;
                int month;
                long time;
                int timezoneOffset;
                int year;

                date = dateOfBirthObj.getInt("date");
                day = dateOfBirthObj.getInt("day");
                month = dateOfBirthObj.getInt("month");
                time = dateOfBirthObj.getInt("time");
                timezoneOffset = dateOfBirthObj.getInt("timezoneOffset");
                year = dateOfBirthObj.getInt("year");

                profile.setDateOfBirth(new DateOfBirth(date, day, month, time, timezoneOffset, year));
            }

            profile.setDepartment(obj.getString("department"));
            profile.setDisplayName(obj.getString("displayName"));
            profile.setFacsimile(obj.getString("facsimile"));
            profile.setFavouriteBooks(obj.getString("favouriteBooks"));
            profile.setFavouriteMovies(obj.getString("favouriteMovies"));
            profile.setFavouriteQuotes(obj.getString("favouriteQuotes"));
            profile.setFavouriteTvShows(obj.getString("favouriteTvShows"));
            profile.setHomepage(obj.getString("homepage"));
            profile.setHomephone(obj.getString("homephone"));
            profile.setImageThumbUrl(obj.getString("imageThumbUrl"));
            profile.setImageUrl(obj.getString("imageUrl"));
            profile.setMobilephone(obj.getString("mobilephone"));
            profile.setNickname(obj.getString("nickname"));
            profile.setPersonalSummary(obj.getString("personalSummary"));
            profile.setPosition(obj.getString("position"));
            profile.setPublications(obj.getString("publications"));
            profile.setRoom(obj.getString("room"));
            profile.setSchool(obj.getString("school"));
            profile.setStaffProfile(obj.getString("staffProfile"));
            profile.setSubjects(obj.getString("subjects"));
            profile.setUniversityProfileUrl(obj.getString("universityProfileUrl"));
            profile.setWorkphone(obj.getString("workphone"));
            profile.setLocked(obj.getBoolean("locked"));


            if (!obj.isNull("socialInfo")) {
                JSONObject socialInfoObj = obj.getJSONObject("socialInfo");
                String fb = "", linkedIn = "", mySpace = "", skype = "", twitter = "";
                if (!socialInfoObj.isNull("facebookUrl")) {
                    fb = socialInfoObj.getString("facebookUrl");
                }

                if (!socialInfoObj.isNull("linkedinUrl")) {
                    linkedIn = socialInfoObj.getString("linkedinUrl");
                }

                if (!socialInfoObj.isNull("myspaceUrl")) {
                    mySpace = socialInfoObj.getString("myspaceUrl");
                }

                if (!socialInfoObj.isNull("skypeUsername")) {
                    skype = socialInfoObj.getString("skypeUsername");
                }

                if (!socialInfoObj.isNull("twitterUrl")) {
                    twitter = socialInfoObj.getString("twitterUrl");
                }
                profile.setSocialInfo(new SocialNetworkingInfo(fb, linkedIn, mySpace, skype, twitter));
            }

            if (!obj.isNull("status")) {
                JSONObject statusObj = obj.getJSONObject("status");
                Date dateAdded;
                String dateFormatted;
                String message;

                dateAdded = new Date(statusObj.getInt("dateAdded"));
                dateFormatted = statusObj.getString("dateFormatted");
                message = statusObj.getString("message");

                profile.setStatus(new ProfileStatus(message, dateAdded, dateFormatted));
            }


            // companyProfiles -> List
            // props -> Map
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * parse the message of the day json
     * http://141.99.248.86:8089/direct/announcement/motd.json
     *
     * @param result the response json
     * @return
     */
    public OnlineMessageOfTheDay parseMotdJson(String result) {
        OnlineMessageOfTheDay onlineMessageOfTheDay = new OnlineMessageOfTheDay(context);
        List<String> messagesList = new ArrayList<>();
        List<String> siteUrlsList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray messages = jsonObject.getJSONArray("announcement_collection");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject obj = messages.getJSONObject(i);
                messagesList.add(obj.getString("motdBody"));
                siteUrlsList.add(obj.getString("motdUrl"));
            }

            onlineMessageOfTheDay.setMessage(messagesList);
            onlineMessageOfTheDay.setSiteUrl(siteUrlsList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return onlineMessageOfTheDay;
    }

    /**
     * parse user's events json
     * http://141.99.248.86:8089/direct/calendar/my.json
     *
     * @param result the response json
     */
    public void parseUserEventJson(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray collections = jsonObject.getJSONArray("calendar_collection");

            for (int i = 0; i < collections.length(); i++) {
                JSONObject obj = collections.getJSONObject(i);

                UserEvents userEvents = new UserEvents();

                JSONObject firstTimeJson = obj.getJSONObject("firstTime");

                userEvents.setCreator(obj.getString("creator"));
                userEvents.setDuration(obj.getInt("duration"));
                userEvents.setEventId(obj.getString("eventId"));
                userEvents.setFirstTime(new Time(firstTimeJson.getString("display"), new Date(Long.parseLong(firstTimeJson.getString("time")))));
                userEvents.setSiteName(obj.getString("siteName"));
                userEvents.setTitle(obj.getString("title"));
                userEvents.setType(obj.getString("type"));
                userEvents.setReference(obj.getString("reference"));
                userEvents.setEventDate();
                userEvents.setEventWholeDate();


                EventsCollection.getUserEventsList().add(userEvents);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * parse user's event's info json
     * http://141.99.248.86:8089/direct/calendar/event/~owner/eventId.json
     *
     * @param result the response json
     * @param index  the index of the event on the List
     */
    public void parseUserEventInfoJson(String result, int index) {
        try {

            JSONObject obj = new JSONObject(result);
            JSONObject lastTimeJson = obj.getJSONObject("lastTime");

            EventsCollection.getUserEventsList().get(index).setDescription(obj.getString("description"));
            EventsCollection.getUserEventsList().get(index).setLastTime(new Time(lastTimeJson.getString("display"), new Date(Long.parseLong(lastTimeJson.getString("time")))));
            EventsCollection.getUserEventsList().get(index).setLocation(obj.getString("location"));

            if (obj.has("recurrenceRule") && !obj.isNull("recurrenceRule")) {
                JSONObject recurrenceRuleObj = obj.getJSONObject("recurrenceRule");

                int count = recurrenceRuleObj.getInt("count");
                String frequency = recurrenceRuleObj.getString("frequency");
                String frequencyDescription = recurrenceRuleObj.getString("frequencyDescription");
                int interval = recurrenceRuleObj.getInt("interval");
                Time until = null;
                EventsCollection.getUserEventsList().get(index).setRecurrenceRule(new RecurrenceRule(count, frequency, frequencyDescription, interval, until));
                if (!recurrenceRuleObj.isNull("until")) {
                    JSONObject untilRuleObj = recurrenceRuleObj.getJSONObject("until");
                    until = new Time(untilRuleObj.getString("display"), new Date(Long.parseLong(untilRuleObj.getString("time"))));
                    EventsCollection.getUserEventsList().get(index).getRecurrenceRule().setUntil(until);
                    EventsCollection.getUserEventsList().get(index).getRecurrenceRule().setEndDate();
                }

            }

            if (obj.has("attachments")) {
                JSONArray attachments = obj.getJSONArray("attachments");

                for (int i = 0; i < attachments.length(); i++) {
                    JSONObject attachment = attachments.getJSONObject(i);
                    EventsCollection.getUserEventsList().get(index).getAttachments().add(attachment.getString("url"));
                    Log.i("url", attachment.getString("url"));
                }
            }

            EventsCollection.getUserEventsList().get(index).setTimeDuration();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getEventCreatorDisplayName(String result, int index) {
        try {
            JSONObject obj = new JSONObject(result);
            EventsCollection.getUserEventsList().get(index).setCreatorUserId(obj.getString("displayName"));
            SharedPreferences preferences = context.getSharedPreferences("event_owners", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(EventsCollection.getUserEventsList().get(index).getEventId(), obj.getString("displayName"));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseSiteDataJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray collections = jsonObject.getJSONArray("membership_collection");
            for (int i = 0; i < collections.length(); i++) {
                JSONObject obj = collections.getJSONObject(i);
                SiteData data = new SiteData();
                data.setId(obj.getString("id").replace(User.getUserId() + "::site:", ""));
                data.setType(obj.getString("siteType"));

                if (obj.getString("siteType").equals("null")) {
                    SiteData.getSites().add(data);
                } else {
                    SiteData.getProjects().add(data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseSiteWholeDataJson(String result, int index) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            SiteData.getSites().get(index).setTitle(jsonObject.getString("entityTitle"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseProjectWholeDataJson(String result, int index) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            SiteData.getProjects().get(index).setTitle(jsonObject.getString("entityTitle"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

package org.sakaiproject.api.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sakaiproject.api.events.OnlineEvents;
import org.sakaiproject.api.general.Connection;
import org.sakaiproject.api.motd.OnlineMessageOfTheDay;
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

    /**
     * parse the session json
     * http://141.99.248.86:8089/direct/session/sessionId.json"
     *
     * @param result the response json
     */
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

    /**
     * parse the user profile data json
     * http://141.99.248.86:8089/direct/profile/userEid.json
     *
     * @param result the response json
     */
    public void parseUserProfileDataJson(String result) {

        try {

            JSONObject obj = new JSONObject(result);
            profile.setAcademicProfileUrl(obj.optString("academicProfileUrl"));
            profile.setBirthday(obj.optString("birthday"));
            profile.setBirthdayDisplay(obj.optString("birthdayDisplay"));
            profile.setBusinessBiography(obj.optString("businessBiography"));
            profile.setCourse(obj.optString("course"));


            if (!obj.isNull("dateOfBirth")) {
                JSONObject dateOfBirthObj = obj.getJSONObject("dateOfBirth");
                int date;
                int day;
                int month;
                long time;
                int timezoneOffset;
                int year;

                date = dateOfBirthObj.optInt("date");
                day = dateOfBirthObj.optInt("day");
                month = dateOfBirthObj.optInt("month");
                time = dateOfBirthObj.optInt("time");
                timezoneOffset = dateOfBirthObj.optInt("timezoneOffset");
                year = dateOfBirthObj.optInt("year");

                profile.setDateOfBirth(new DateOfBirth(date, day, month, time, timezoneOffset, year));
            }

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

            JSONObject socialInfoObj = obj.getJSONObject("socialInfo");
            String fb = "", linkedIn = "", mySpace = "", skype = "", twitter = "";
            if (!socialInfoObj.isNull("facebookUrl")) {
                fb = socialInfoObj.optString("socialInfoObj");
            }

            if (!socialInfoObj.isNull("linkedinUrl")) {
                linkedIn = socialInfoObj.optString("linkedinUrl");
            }

            if (!socialInfoObj.isNull("myspaceUrl")) {
                mySpace = socialInfoObj.optString("myspaceUrl");
            }

            if (!socialInfoObj.isNull("skypeUsername")) {
                skype = socialInfoObj.optString("skypeUsername");
            }

            if (!socialInfoObj.isNull("twitterUrl")) {
                twitter = socialInfoObj.optString("twitterUrl");
            }
            profile.setSocialInfo(new SocialNetworkingInfo(fb, linkedIn, mySpace, skype, twitter));


            if (!obj.isNull("status")) {
                JSONObject statusObj = obj.getJSONObject("status");
                Date dateAdded;
                String dateFormatted;
                String message;

                dateAdded = new Date(statusObj.optInt("dateAdded"));
                dateFormatted = statusObj.optString("dateFormatted");
                message = statusObj.optString("message");

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
                JSONObject obj = collections.optJSONObject(i);

                UserEvents userEvents = new UserEvents();

                JSONObject firstTimeJson = obj.getJSONObject("firstTime");

                userEvents.setCreator(obj.optString("creator"));
                userEvents.setDuration(obj.optInt("duration"));
                userEvents.setEventId(obj.optString("eventId"));
                userEvents.setFirstTime(new Time(firstTimeJson.optString("display"), new Date(Long.parseLong(firstTimeJson.optString("time")))));
                userEvents.setSiteName(obj.optString("siteName"));
                userEvents.setTitle(obj.optString("title"));
                userEvents.setType(obj.optString("type"));

                userEvents.setEventDate();
                userEvents.setEventWholeDate();


                OnlineEvents.getUserEventsList().add(userEvents);
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

            OnlineEvents.getUserEventsList().get(index).setDescription(obj.optString("description"));
            OnlineEvents.getUserEventsList().get(index).setLastTime(new Time(lastTimeJson.optString("display"), new Date(Long.parseLong(lastTimeJson.optString("time")))));
            OnlineEvents.getUserEventsList().get(index).setLocation(obj.optString("location"));

            if (obj.has("recurrenceRule")) {
                JSONObject recurrenceRuleObj = obj.getJSONObject("recurrenceRule");

                int count = recurrenceRuleObj.optInt("count");
                String frequency = recurrenceRuleObj.optString("frequency");
                String frequencyDescription = recurrenceRuleObj.optString("frequencyDescription");
                int interval = recurrenceRuleObj.optInt("interval");
                Time until = null;
                OnlineEvents.getUserEventsList().get(index).setRecurrenceRule(new RecurrenceRule(count, frequency, frequencyDescription, interval, until));
                if (!recurrenceRuleObj.isNull("until")) {
                    JSONObject untilRuleObj = recurrenceRuleObj.getJSONObject("until");
                    until = new Time(untilRuleObj.optString("display"), new Date(Long.parseLong(untilRuleObj.optString("time"))));
                    OnlineEvents.getUserEventsList().get(index).getRecurrenceRule().setUntil(until);
                    OnlineEvents.getUserEventsList().get(index).getRecurrenceRule().setEndDate();
                }

            }

            if (obj.has("attachments")) {
                JSONArray attachments = obj.getJSONArray("attachments");

                for (int i = 0; i < attachments.length(); i++) {
                    JSONObject attachment = attachments.optJSONObject(i);
                    OnlineEvents.getUserEventsList().get(index).getAttachments().add(attachment.optString("url"));
                    Log.i("url", attachment.optString("url"));
                }
            }


            OnlineEvents.getUserEventsList().get(index).setTimeDuration();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

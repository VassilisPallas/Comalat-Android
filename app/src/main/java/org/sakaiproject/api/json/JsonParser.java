package org.sakaiproject.api.json;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.sakaiproject.api.events.EventsCollection;
import org.sakaiproject.api.site.Owner;
import org.sakaiproject.api.site.SitePage;
import org.sakaiproject.api.site.SiteTools;
import org.sakaiproject.general.Connection;
import org.sakaiproject.api.motd.OnlineMessageOfTheDay;
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

    /**
     * the JsonParser constructor
     * @param context the context
     */
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
            user.setEmail(!obj.getString("email").trim().equals("") && !obj.getString("email").trim().equals("null") ? obj.getString("email") : "");
            user.setFirstName(!obj.getString("firstName").trim().equals("") && !obj.getString("firstName").trim().equals("null") ? obj.getString("firstName") : "");
            if (!obj.getString("modifiedDate").equals("null"))
                user.setModifiedDate(new Date(Long.parseLong(obj.getString("modifiedDate"))));
            else
                user.setModifiedDate(new Date(0));
            user.setModifiedTime(new Time(modifiedTimeJson.getString("display"), new Date(Long.parseLong(modifiedTimeJson.getString("time")))));
            user.setLastName(!obj.getString("lastName").trim().equals("") && !obj.getString("lastName").trim().equals("null") ? obj.getString("lastName") : "");
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
            profile.setAcademicProfileUrl(!obj.getString("academicProfileUrl").trim().equals("") && !obj.getString("academicProfileUrl").trim().equals("null") ? obj.getString("academicProfileUrl") : "");
            profile.setBirthday(obj.getString("birthday"));
            profile.setBirthdayDisplay(obj.getString("birthdayDisplay"));
            profile.setBusinessBiography(obj.getString("businessBiography"));
            profile.setCourse(!obj.getString("course").trim().equals("") && !obj.getString("course").trim().equals("null") ? obj.getString("course") : "");


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

            profile.setDepartment(!obj.getString("department").trim().equals("") && !obj.getString("department").trim().equals("null") ? obj.getString("department") : "");
            profile.setDisplayName(!obj.getString("displayName").trim().equals("") && !obj.getString("displayName").trim().equals("null") ? obj.getString("displayName") : "");
            profile.setFacsimile(!obj.getString("facsimile").trim().equals("") && !obj.getString("facsimile").trim().equals("null") ? obj.getString("facsimile") : "");
            profile.setFavouriteBooks(!obj.getString("favouriteBooks").trim().equals("") && !obj.getString("favouriteBooks").trim().equals("null") ? obj.getString("favouriteBooks") : "");
            profile.setFavouriteMovies(!obj.getString("favouriteMovies").trim().equals("") && !obj.getString("favouriteMovies").trim().equals("null") ? obj.getString("favouriteMovies") : "");
            profile.setFavouriteQuotes(!obj.getString("favouriteQuotes").trim().equals("") && !obj.getString("favouriteQuotes").trim().equals("null") ? obj.getString("favouriteQuotes") : "");
            profile.setFavouriteTvShows(!obj.getString("favouriteTvShows").trim().equals("") && !obj.getString("favouriteTvShows").trim().equals("null") ? obj.getString("favouriteTvShows") : "");
            profile.setHomepage(!obj.getString("homepage").trim().equals("") && !obj.getString("homepage").trim().equals("null") ? obj.getString("homepage") : "");
            profile.setHomephone(!obj.getString("homephone").trim().equals("") && !obj.getString("homephone").trim().equals("null") ? obj.getString("homephone") : "");
            profile.setImageThumbUrl(!obj.getString("imageThumbUrl").trim().equals("") && !obj.getString("imageThumbUrl").trim().equals("null") ? obj.getString("imageThumbUrl") : "");
            profile.setImageUrl(!obj.getString("imageUrl").trim().equals("") && !obj.getString("imageUrl").trim().equals("null") ? obj.getString("imageUrl") : "");
            profile.setMobilephone(!obj.getString("mobilephone").trim().equals("") && !obj.getString("mobilephone").trim().equals("null") ? obj.getString("mobilephone") : "");
            profile.setNickname(!obj.getString("nickname").trim().equals("") && !obj.getString("nickname").trim().equals("null") ? obj.getString("nickname") : "");
            profile.setPersonalSummary(!obj.getString("personalSummary").trim().equals("") && !obj.getString("personalSummary").trim().equals("null") ? obj.getString("personalSummary") : "");
            profile.setPosition(!obj.getString("position").trim().equals("") && !obj.getString("position").trim().equals("null") ? obj.getString("position") : "");
            profile.setPublications(!obj.getString("publications").trim().equals("") && !obj.getString("publications").trim().equals("null") ? obj.getString("publications") : "");
            profile.setRoom(!obj.getString("room").trim().equals("") && !obj.getString("room").trim().equals("null") ? obj.getString("room") : "");
            profile.setSchool(!obj.getString("school").trim().equals("") && !obj.getString("school").trim().equals("null") ? obj.getString("school") : "");
            profile.setStaffProfile(!obj.getString("staffProfile").trim().equals("") && !obj.getString("staffProfile").trim().equals("null") ? obj.getString("staffProfile") : "");
            profile.setSubjects(!obj.getString("subjects").trim().equals("") && !obj.getString("subjects").trim().equals("null") ? obj.getString("subjects") : "");
            profile.setUniversityProfileUrl(!obj.getString("universityProfileUrl").trim().equals("") && !obj.getString("universityProfileUrl").trim().equals("null") ? obj.getString("universityProfileUrl") : "");
            profile.setWorkphone(!obj.getString("workphone").trim().equals("") && !obj.getString("workphone").trim().equals("null") ? obj.getString("workphone") : "");
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

    /**
     * get the full name from the owner of the event
     * http://141.99.248.86:8089/direct/profile/user_id.json
     *
     * @param result the response json
     * @param index the index of the event on the List
     */
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

    /**
     * get the data from the sites and projects
     * if the type is project it saves the data on the projects list, else on the sites list
     * http://141.99.248.86:8089/direct/membership.json
     *
     * @param result the response json
     */
    public void parseSiteDataJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray collections = jsonObject.getJSONArray("membership_collection");
            for (int i = 0; i < collections.length(); i++) {
                JSONObject obj = collections.getJSONObject(i);
                SiteData data = new SiteData();
                data.setId(obj.getString("id").replace(User.getUserId() + "::site:", ""));
                data.setMemberRole(obj.getString("memberRole"));
                data.setActive(obj.getBoolean("active"));
                data.setProvided(obj.getBoolean("provided"));
                data.setType(obj.getString("siteType"));

                if (obj.getString("siteType").equals("project")) {
                    SiteData.getProjects().add(data);
                } else {
                    SiteData.getSites().add(data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the data from the selected site
     * http://141.99.248.86:8089/direct/site/site_id.json
     *
     * @param result the response json
     * @param index the index of the site on the List
     */
    public void getSiteData(String result, int index) {
        try {
            JSONObject jsonObject = new JSONObject(result);

            SiteData.getSites().get(index).setContactEmail(jsonObject.getString("contactEmail"));
            SiteData.getSites().get(index).setContactName(jsonObject.getString("contactName"));

            JSONObject createdTime = jsonObject.getJSONObject("createdTime");
            SiteData.getSites().get(index).setCreatedTime(new Time(createdTime.getString("display"), new Date(createdTime.getLong("time"))));


            SiteData.getSites().get(index).setDescription(!jsonObject.getString("description").trim().equals("") && !jsonObject.getString("description").trim().equals("null") ? jsonObject.getString("description") : "");

            SiteData.getSites().get(index).setShortDescription(!jsonObject.getString("shortDescription").trim().equals("") && !jsonObject.getString("shortDescription").trim().equals("null") ? jsonObject.getString("shortDescription") : "");

            SiteData.getSites().get(index).setIconUrlFull(jsonObject.getString("iconUrlFull"));

            SiteData.getSites().get(index).setInfoUrlFull(jsonObject.getString("infoUrlFull"));

            SiteData.getSites().get(index).setJoinerRole(jsonObject.getString("joinerRole"));

            SiteData.getSites().get(index).setLastModified(jsonObject.getLong("lastModified"));

            SiteData.getSites().get(index).setMaintainRole(jsonObject.getString("maintainRole"));

            JSONObject modifiedTime = jsonObject.getJSONObject("modifiedTime");
            SiteData.getSites().get(index).setModifiedTime(new Time(modifiedTime.getString("display"), new Date(modifiedTime.getLong("time"))));

            SiteData.getSites().get(index).setOwner(jsonObject.getString("owner"));


            JSONObject props = jsonObject.getJSONObject("props");

            if (props != JSONObject.NULL) {
                SiteData.getSites().get(index).setProps(jsonObjectToMap(props));
            }

            SiteData.getSites().get(index).setProviderGroupId(jsonObject.getString("providerGroupId"));

            SiteData.getSites().get(index).setProviderGroupId(jsonObject.getString("providerGroupId"));

            JSONArray siteGroups = jsonObject.optJSONArray("siteGroups");

            if (siteGroups != null) {
                SiteData.getSites().get(index).setSiteGroups(jsonArrayToList(siteGroups));
            }

            JSONObject siteOwner = jsonObject.getJSONObject("siteOwner");

            if (siteOwner != JSONObject.NULL) {
                SiteData.getSites().get(index).setSiteOwner(new Owner(siteOwner.getString("userDisplayName"), siteOwner.getString("userId")));
            }

            SiteData.getSites().get(index).setSkin(jsonObject.getString("skin"));

            // it returns Date but is always null
//            if (jsonObject.getLong("softlyDeletedDate") != JSONObject.NULL)
//                SiteData.getSites().get(index).setSoftlyDeletedDate(new Date(jsonObject.getLong("softlyDeletedDate")));

            SiteData.getSites().get(index).setTitle(jsonObject.getString("title"));

            JSONArray userRoles = jsonObject.getJSONArray("userRoles");
            if (userRoles != null) {
                SiteData.getSites().get(index).setUserRoles(jsonArrayToList(userRoles));
            }

            SiteData.getSites().get(index).setActiveEdit(jsonObject.getBoolean("activeEdit"));

            SiteData.getSites().get(index).setCustomPageOrdered(jsonObject.getBoolean("customPageOrdered"));

            SiteData.getSites().get(index).setEmpty(jsonObject.getBoolean("empty"));

            SiteData.getSites().get(index).setJoinable(jsonObject.getBoolean("joinable"));

            SiteData.getSites().get(index).setPubView(jsonObject.getBoolean("pubView"));

            SiteData.getSites().get(index).setPublished(jsonObject.getBoolean("published"));

            SiteData.getSites().get(index).setSoftlyDeleted(jsonObject.getBoolean("softlyDeleted"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the data from the selected project
     * http://141.99.248.86:8089/direct/site/site_id.json
     *
     * @param result the response json
     * @param index the index of the project on the List
     */
    public void getProjectData(String result, int index) {
        try {
            JSONObject jsonObject = new JSONObject(result);

            SiteData.getProjects().get(index).setContactEmail(jsonObject.getString("contactEmail"));
            SiteData.getProjects().get(index).setContactName(jsonObject.getString("contactName"));

            JSONObject createdTime = jsonObject.getJSONObject("createdTime");
            SiteData.getProjects().get(index).setCreatedTime(new Time(createdTime.getString("display"), new Date(createdTime.getLong("time"))));

            SiteData.getProjects().get(index).setDescription(!jsonObject.getString("description").trim().equals("") && !jsonObject.getString("description").trim().equals("null") ? jsonObject.getString("description") : "");

            SiteData.getProjects().get(index).setShortDescription(!jsonObject.getString("shortDescription").trim().equals("") && !jsonObject.getString("shortDescription").trim().equals("null") ? jsonObject.getString("shortDescription") : "");

            SiteData.getProjects().get(index).setIconUrlFull(jsonObject.getString("iconUrlFull"));

            SiteData.getProjects().get(index).setInfoUrlFull(jsonObject.getString("infoUrlFull"));

            SiteData.getProjects().get(index).setJoinerRole(jsonObject.getString("joinerRole"));

            SiteData.getProjects().get(index).setLastModified(jsonObject.getLong("lastModified"));

            SiteData.getProjects().get(index).setMaintainRole(jsonObject.getString("maintainRole"));

            JSONObject modifiedTime = jsonObject.getJSONObject("modifiedTime");
            SiteData.getProjects().get(index).setModifiedTime(new Time(modifiedTime.getString("display"), new Date(modifiedTime.getLong("time"))));

            SiteData.getProjects().get(index).setOwner(jsonObject.getString("owner"));


            JSONObject props = jsonObject.getJSONObject("props");

            if (props != JSONObject.NULL) {
                SiteData.getProjects().get(index).setProps(jsonObjectToMap(props));
            }

            SiteData.getProjects().get(index).setProviderGroupId(jsonObject.getString("providerGroupId"));

            SiteData.getProjects().get(index).setProviderGroupId(jsonObject.getString("providerGroupId"));

            JSONArray siteGroups = jsonObject.optJSONArray("siteGroups");

            if (siteGroups != null) {
                SiteData.getProjects().get(index).setSiteGroups(jsonArrayToList(siteGroups));
            }

            JSONObject siteOwner = jsonObject.getJSONObject("siteOwner");

            if (siteOwner != JSONObject.NULL) {
                SiteData.getProjects().get(index).setSiteOwner(new Owner(siteOwner.getString("userDisplayName"), siteOwner.getString("userId")));
            }

            SiteData.getProjects().get(index).setSkin(jsonObject.getString("skin"));


            // it returns Date but is always null
//            if (jsonObject.getLong("softlyDeletedDate") != JSONObject.NULL)
//                SiteData.getProjects().get(index).setSoftlyDeletedDate(new Date(jsonObject.getLong("softlyDeletedDate")));

            SiteData.getProjects().get(index).setTitle(jsonObject.getString("title"));

            JSONArray userRoles = jsonObject.getJSONArray("userRoles");
            if (userRoles != null) {
                SiteData.getProjects().get(index).setUserRoles(jsonArrayToList(userRoles));
            }

            SiteData.getProjects().get(index).setActiveEdit(jsonObject.getBoolean("activeEdit"));

            SiteData.getProjects().get(index).setCustomPageOrdered(jsonObject.getBoolean("customPageOrdered"));

            SiteData.getProjects().get(index).setEmpty(jsonObject.getBoolean("empty"));

            SiteData.getProjects().get(index).setJoinable(jsonObject.getBoolean("joinable"));

            SiteData.getProjects().get(index).setPubView(jsonObject.getBoolean("pubView"));

            SiteData.getProjects().get(index).setPublished(jsonObject.getBoolean("published"));

            SiteData.getProjects().get(index).setSoftlyDeleted(jsonObject.getBoolean("softlyDeleted"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the info from the tools for each site or project
     * http://141.99.248.86:8089/direct/site/site_id/pages.json
     *
     * @param result the response json
     * @param index the index of the project on the List
     * @param type "project" for project type, and "site" for site type
     */
    public void getSitePageData(String result, int index, String type) {
        try {

            List<SitePage> pages = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                SitePage page = new SitePage();
                page.setLayout(obj.getInt("layout"));
                page.setToolPopupUrl(obj.getString("toolpopupurl"));
                page.setToolPopup(obj.getBoolean("toolpopup"));
                page.setSkin(obj.getString("skin"));
                page.setId(obj.getString("id"));
                page.setPosition(obj.getInt("position"));
                page.setTitle(obj.getString("title"));

                JSONArray tools = obj.getJSONArray("tools");
                List<SiteTools> toolsList = new ArrayList<>();
                for (int j = 0; j < tools.length(); j++) {

                    JSONObject tool = tools.getJSONObject(j);

                    SiteTools siteTools = new SiteTools();

                    siteTools.setToolId(tool.getString("toolId"));
                    siteTools.setPageOrder(tool.getInt("pageOrder"));
                    siteTools.setPlacementId(tool.getString("placementId"));
                    siteTools.setDescription(tool.getString("description"));
                    siteTools.setTitle(tool.getString("title"));

                    toolsList.add(siteTools);
                    // home
                }

                page.setTools(toolsList);

                pages.add(page);
            }

            if (type.equals("project"))
                SiteData.getProjects().get(index).setPages(pages);
            else
                SiteData.getSites().get(index).setPages(pages);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * get the permissions from the site
     * http://141.99.248.86:8089/direct/site/site_id/perms.json
     *
     * @param result the response json
     * @param index the index of the project on the List
     * @param type "project" for project type, and "site" for site type
     */
    public void getSitePermissions(String result, int index, String type) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONObject data = obj.getJSONObject("data");

            JSONArray access = data.getJSONArray("access");
            if (type.equals("project")) {
                SiteData.getProjects().get(index).setAccess(jsonArrayToList(access));
            } else {
                SiteData.getSites().get(index).setAccess(jsonArrayToList(access));
            }

            JSONArray maintain = data.getJSONArray("maintain");
            if (type.equals("project")) {
                SiteData.getProjects().get(index).setMaintain(jsonArrayToList(maintain));
            } else {
                SiteData.getSites().get(index).setMaintain(jsonArrayToList(maintain));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the permissions for the user from the site (mainten or access)
     * http://141.99.248.86:8089/direct/site/site_id/userPerms.json
     *
     * @param result the response json
     * @param index the index of the project on the List
     * @param type "project" for project type, and "site" for site type
     */
    public void getUserSitePermissions(String result, int index, String type) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray data = obj.getJSONArray("data");

            if (type.equals("project")) {
                SiteData.getProjects().get(index).setUserSitePermissions(jsonArrayToList(data));
            } else {
                SiteData.getSites().get(index).setUserSitePermissions(jsonArrayToList(data));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert jsonObject to Map<String, String>
     *
     * @param props the json object
     * @return the converted jsonObject to Map
     * @throws JSONException
     */
    private Map<String, String> jsonObjectToMap(JSONObject props) throws JSONException {
        Map<String, String> temp = new Hashtable<>();

        Iterator<?> propsKeys = props.keys();

        while (propsKeys.hasNext()) {
            String key = (String) propsKeys.next();
            String value = (String) props.get(key);
            temp.put(key, value);
        }

        return temp;
    }

    /**
     * Convert jsonArray to List<String>
     *
     * @param siteGroups the json array
     * @return the converted jsonArray to List
     * @throws JSONException
     */
    private List<String> jsonArrayToList(JSONArray siteGroups) throws JSONException {
        List<String> temp = new ArrayList<>();

        for (int i = 0; i < siteGroups.length(); i++) {
            temp.add(siteGroups.get(i).toString());
        }

        return temp;
    }

}

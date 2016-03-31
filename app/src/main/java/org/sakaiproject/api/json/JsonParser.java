package org.sakaiproject.api.json;

import android.content.Context;
import android.content.SharedPreferences;
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
import org.sakaiproject.api.pojos.SiteName;
import org.sakaiproject.api.pojos.assignments.Assignment;
import org.sakaiproject.api.pojos.events.EventInfo;
import org.sakaiproject.api.pojos.UserEventOwner;
import org.sakaiproject.api.pojos.events.Event;
import org.sakaiproject.api.pojos.login.Login;
import org.sakaiproject.api.pojos.login.Profile;
import org.sakaiproject.api.pojos.login.UserData;
import org.sakaiproject.api.pojos.membership.MembershipData;
import org.sakaiproject.api.pojos.membership.Membership;
import org.sakaiproject.api.pojos.membership.PagePermissions;
import org.sakaiproject.api.pojos.membership.PageUserPermissions;
import org.sakaiproject.api.pojos.membership.SitePage;
import org.sakaiproject.general.Connection;
import org.sakaiproject.api.memberships.SiteData;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.events.UserEvents;

/**
 * Created by vasilis on 10/13/15.
 * This is the json parser for every json response from the server
 */
public class JsonParser {

    private Connection con;
    private User user;
    private org.sakaiproject.api.user.profile.Profile profile;
    private Context context;

    /**
     * the JsonParser constructor
     *
     * @param context the context
     */
    public JsonParser(Context context) {
        this.context = context;
        con = Connection.getInstance();
        con.setContext(context);
        user = User.getInstance();
        profile = org.sakaiproject.api.user.profile.Profile.getInstance();
    }

    /**
     * parse the session json
     * http://141.99.248.86:8089/direct/session/sessionId.json
     *
     * @param login the object with the json data
     */
    public static void parseLoginResult(Login login) {

        Connection.setCreationTime(login.getCreationTime());
        Connection.setLastAccessedTime(login.getLastAccessedTime());
        Connection.setMaxInactiveInterval(login.getMaxInactiveInterval());
        User.setUserEid(login.getUserEid());
        User.setUserId(login.getUserId());

        /**
         * posible values for enum
         * <xs:enumeration value="true"/>
         * <xs:enumeration value="yes"/>
         * <xs:enumeration value="false"/>
         * <xs:enumeration value="no"/>
         * @see <a href="http://help.pervasive.com/plugins/servlet/mobile#content/view/19137048">Confluence Mobile - XML Schema for Deployment Descriptor</a>
         */
        //Enum attributeName;

    }

    /**
     * parse the user data json
     * http://141.99.248.86:8089/direct/user/userEid.json
     *
     * @param userData the object with the json data
     */
    public static void parseUserDataJson(UserData userData) {
        if (userData.getCreatedDate() != null)
            User.setCreatedDate(new Date(userData.getCreatedDate()));
        else
            User.setCreatedDate(new Date(0));
        User.setCreatedTime(userData.getCreatedTime());
        User.setEmail(userData.getEmail());
        User.setFirstName(userData.getFirstName());
        if (userData.getModifiedDate() != null)
            User.setModifiedDate(new Date(userData.getModifiedDate()));
        else
            User.setModifiedDate(new Date(0));
        User.setModifiedTime(userData.getModifiedTime());
        User.setLastName(userData.getLastName());
        User.setType(userData.getType());
    }

    /**
     * parse the user profile data json
     * http://141.99.248.86:8089/direct/profile/userEid.json
     *
     * @param profile the object with the json data
     */
    public static void parseUserProfileDataJson(Profile profile) {

        org.sakaiproject.api.user.profile.Profile.setAcademicProfileUrl(profile.getAcademicProfileUrl());
        org.sakaiproject.api.user.profile.Profile.setBirthday(profile.getBirthday());
        org.sakaiproject.api.user.profile.Profile.setBirthdayDisplay(profile.getBirthdayDisplay());
        org.sakaiproject.api.user.profile.Profile.setBusinessBiography(profile.getBusinessBiography());
        org.sakaiproject.api.user.profile.Profile.setCourse(profile.getCourse());
        org.sakaiproject.api.user.profile.Profile.setDateOfBirth(profile.getDateOfBirth());
        org.sakaiproject.api.user.profile.Profile.setDepartment(profile.getDepartment());
        org.sakaiproject.api.user.profile.Profile.setDisplayName(profile.getDisplayName());
        org.sakaiproject.api.user.profile.Profile.setFacsimile(profile.getFacsimile());
        org.sakaiproject.api.user.profile.Profile.setFavouriteBooks(profile.getFavouriteBooks());
        org.sakaiproject.api.user.profile.Profile.setFavouriteMovies(profile.getFavouriteMovies());
        org.sakaiproject.api.user.profile.Profile.setFavouriteQuotes(profile.getFavouriteQuotes());
        org.sakaiproject.api.user.profile.Profile.setFavouriteTvShows(profile.getFavouriteTvShows());
        org.sakaiproject.api.user.profile.Profile.setHomepage(profile.getHomepage());
        org.sakaiproject.api.user.profile.Profile.setHomephone(profile.getHomephone());
        org.sakaiproject.api.user.profile.Profile.setImageThumbUrl(profile.getImageThumbUrl());
        org.sakaiproject.api.user.profile.Profile.setImageUrl(profile.getImageUrl());
        org.sakaiproject.api.user.profile.Profile.setMobilephone(profile.getMobilephone());
        org.sakaiproject.api.user.profile.Profile.setNickname(profile.getNickname());
        org.sakaiproject.api.user.profile.Profile.setPersonalSummary(profile.getPersonalSummary());
        org.sakaiproject.api.user.profile.Profile.setPosition(profile.getPosition());
        org.sakaiproject.api.user.profile.Profile.setPublications(profile.getPublications());
        org.sakaiproject.api.user.profile.Profile.setRoom(profile.getRoom());
        org.sakaiproject.api.user.profile.Profile.setSchool(profile.getSchool());
        org.sakaiproject.api.user.profile.Profile.setStaffProfile(profile.getStaffProfile());
        org.sakaiproject.api.user.profile.Profile.setSubjects(profile.getSubjects());
        org.sakaiproject.api.user.profile.Profile.setUniversityProfileUrl(profile.getUniversityProfileUrl());
        org.sakaiproject.api.user.profile.Profile.setWorkphone(profile.getWorkphone());
        org.sakaiproject.api.user.profile.Profile.setLocked(profile.isLocked());
        org.sakaiproject.api.user.profile.Profile.setSocialInfo(profile.getSocialInfo());
        org.sakaiproject.api.user.profile.Profile.setStatus(profile.getStatus());
        org.sakaiproject.api.user.profile.Profile.setCompanyProfiles(profile.getCompanyProfiles());
        org.sakaiproject.api.user.profile.Profile.setProps(profile.getProps());
    }

    /**
     * parse user's events json
     * http://141.99.248.86:8089/direct/calendar/my.json
     * <p>
     * parse membership's events json
     * http://141.99.248.86:8089/direct/calendar/site/siteId.json
     *
     * @param event the object with the json data
     */
    public static void parseEventJson(Event event) {

        for (Event.Items item : event.getCalendarCollection()) {
            UserEvents userEvents = new UserEvents();
            userEvents.setCreator(item.getCreator());
            userEvents.setDuration(item.getDuration());
            userEvents.setEventId(item.getEventId());
            userEvents.setFirstTime(item.getFirstTime());
            userEvents.setSiteName(item.getSiteName());
            userEvents.setTitle(item.getTitle());
            userEvents.setType(item.getType());
            userEvents.setReference(item.getReference());

            userEvents.setEventDate();
            userEvents.setEventWholeDate();

            EventsCollection.getEventsList().add(userEvents);
        }
    }

    /**
     * parse user's event's info json
     * http://141.99.248.86:8089/direct/calendar/event/~owner/eventId.json
     * <p>
     * parse membership's event's info json
     * http://141.99.248.86:8089/direct/calendar/event/siteId/eventId.json
     *
     * @param userEventOwnerPojo the object with the json data
     * @param index              the index of the event on the List
     */
    public static void parseEventInfoJson(EventInfo userEventOwnerPojo, int index) {

        EventsCollection.getEventsList().get(index).setDescription(userEventOwnerPojo.getDescription());
        EventsCollection.getEventsList().get(index).setLastTime(userEventOwnerPojo.getLastTime());
        EventsCollection.getEventsList().get(index).setLocation(userEventOwnerPojo.getLocation());
        EventsCollection.getEventsList().get(index).setRecurrenceRule(userEventOwnerPojo.getRecurrenceRule());
        if (EventsCollection.getEventsList().get(index).getRecurrenceRule() != null)
            EventsCollection.getEventsList().get(index).getRecurrenceRule().setEndDate();
        EventsCollection.getEventsList().get(index).setAttachments(userEventOwnerPojo.getAttachments());

        EventsCollection.getEventsList().get(index).setTimeDuration();
    }

    /**
     * get the full name from the owner of the event
     * http://141.99.248.86:8089/direct/profile/user_id.json
     *
     * @param context
     * @param userEventOwner the object with the json data
     * @param index          the index of the event on the List
     */
    public static void getEventCreatorDisplayName(Context context, UserEventOwner userEventOwner, int index) {

        EventsCollection.getEventsList().get(index).setCreatorUserId(userEventOwner.getDisplayName());
        SharedPreferences preferences = context.getSharedPreferences("event_owners", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EventsCollection.getEventsList().get(index).getEventId(), userEventOwner.getDisplayName());
        editor.apply();
    }

    /**
     * get the data from the sites and projects
     * if the type is project it saves the data on the projects list, else on the sites list
     * http://141.99.248.86:8089/direct/membership.json
     *
     * @param membership the object with the json data
     */
    public static void parseSiteDataJson(Membership membership) {

        for (Membership.MembershipCollection collection : membership.getMembershipCollectionList()) {
            SiteData data = new SiteData();
            data.setId(collection.getId().replace(User.getUserId() + "::site:", ""));
            data.setMemberRole(collection.getMemberRole());
            data.setActive(collection.isActive());
            data.setProvided(collection.isProvided());
            data.setType(collection.getSiteType());

            if (collection.getSiteType() == null) {
                SiteData.getSites().add(data);
            } else {
                SiteData.getProjects().add(data);
            }
        }
    }

    /**
     * get the data from the selected site
     * http://141.99.248.86:8089/direct/site/site_id.json
     *
     * @param membershipData the object with the json data
     * @param index          the index of the site on the List
     * @param type           membership type
     */
    public static void getSiteData(MembershipData membershipData, int index, String type) {

        SiteData data;
        if (type == null)
            data = SiteData.getSites().get(index);
        else
            data = SiteData.getProjects().get(index);

        data.setContactEmail(membershipData.getContactEmail());
        data.setContactName(membershipData.getContactName());
        data.setCreatedTime(membershipData.getCreatedTime());
        data.setDescription(membershipData.getDescription());
        data.setShortDescription(membershipData.getShortDescription());
        data.setIconUrlFull(membershipData.getIconUrlFull());
        data.setInfoUrlFull(membershipData.getInfoUrlFull());
        data.setJoinerRole(membershipData.getJoinerRole());
        data.setLastModified(membershipData.getLastModified());
        data.setMaintainRole(membershipData.getMaintainRole());
        data.setModifiedTime(membershipData.getModifiedTime());
        data.setOwner(membershipData.getOwner());
        data.setProps(membershipData.getProps());
        data.setProviderGroupId(membershipData.getProviderGroupId());
        data.setSiteGroups(membershipData.getSiteGroups());
        data.setSiteOwner(membershipData.getSiteOwner());
        data.setSkin(membershipData.getSkin());
        data.setSoftlyDeletedDate(membershipData.getSoftlyDeletedDate());
        data.setTitle(membershipData.getTitle());
        data.setUserRoles(membershipData.getUserRoles());
        data.setActiveEdit(membershipData.isActiveEdit());
        data.setCustomPageOrdered(membershipData.isCustomPageOrdered());
        data.setEmpty(membershipData.isEmpty());
        data.setJoinable(membershipData.isJoinable());
        data.setPubView(membershipData.isPubView());
        data.setPublished(membershipData.isPublished());
        data.setSoftlyDeleted(membershipData.isSoftlyDeleted());
        data.setPages(membershipData.getSitePages());

        if (type == null)
            SiteData.getSites().set(index, data);
        else
            SiteData.getProjects().set(index, data);
    }

    /**
     * get My Workspace data
     * http://141.99.248.86:8089/direct/site/~site_id.json
     *
     * @param membershipData the object with the json data
     */
    public static void getSiteData(MembershipData membershipData) {

        SiteData data = new SiteData();

        data.setContactEmail(membershipData.getContactEmail());
        data.setContactName(membershipData.getContactName());
        data.setCreatedTime(membershipData.getCreatedTime());
        data.setDescription(membershipData.getDescription());
        data.setShortDescription(membershipData.getShortDescription());
        data.setIconUrlFull(membershipData.getIconUrlFull());
        data.setInfoUrlFull(membershipData.getInfoUrlFull());
        data.setJoinerRole(membershipData.getJoinerRole());
        data.setLastModified(membershipData.getLastModified());
        data.setMaintainRole(membershipData.getMaintainRole());
        data.setModifiedTime(membershipData.getModifiedTime());
        data.setOwner(membershipData.getOwner());
        data.setProps(membershipData.getProps());
        data.setProviderGroupId(membershipData.getProviderGroupId());
        data.setSiteGroups(membershipData.getSiteGroups());
        data.setSiteOwner(membershipData.getSiteOwner());
        data.setSkin(membershipData.getSkin());
        data.setSoftlyDeletedDate(membershipData.getSoftlyDeletedDate());
        data.setTitle(membershipData.getTitle());
        data.setUserRoles(membershipData.getUserRoles());
        data.setActiveEdit(membershipData.isActiveEdit());
        data.setCustomPageOrdered(membershipData.isCustomPageOrdered());
        data.setEmpty(membershipData.isEmpty());
        data.setJoinable(membershipData.isJoinable());
        data.setPubView(membershipData.isPubView());
        data.setPublished(membershipData.isPublished());
        data.setSoftlyDeleted(membershipData.isSoftlyDeleted());
        data.setPages(membershipData.getSitePages());

        SiteData.getSites().add(data);
    }

    /**
     * get the info from the tools for each site or project
     * http://141.99.248.86:8089/direct/site/site_id/pages.json
     *
     * @param pages list with the objects of the json data
     * @param index the index of the project on the List
     * @param type  "project" for project type, and "site" for site type
     */
    public static void getSitePageData(List<SitePage> pages, int index, String type) {
        for (int i = 0; i < pages.size(); i++) {
            SitePage page;
            if (type == null) {
                page = SiteData.getSites().get(index).getPages().get(i);
            } else {
                page = SiteData.getProjects().get(index).getPages().get(i);
            }

            page.setLayout(pages.get(i).getLayout());
            page.setToolPopupUrl(pages.get(i).getToolPopupUrl());
            page.setToolPopup(pages.get(i).isToolPopup());
            page.setSkin(pages.get(i).getSkin());
            page.setId(pages.get(i).getId());
            page.setPosition(pages.get(i).getPosition());
            page.setTitle(pages.get(i).getTitle());
            page.setTools(pages.get(i).getTools());

            if (type == null) {
                SiteData.getSites().get(index).getPages().set(i, page);
            } else {
                SiteData.getProjects().get(index).getPages().set(i, page);
            }
        }
    }

    /**
     * get the info from the tools for My Workspace
     * http://141.99.248.86:8089/direct/site/~site_id/pages.json
     *
     * @param pages list with the objects of the json data
     */
    public static void getSitePageData(List<SitePage> pages) {

        for (int i = 0; i < pages.size(); i++) {
            SitePage page = SiteData.getSites().get(0).getPages().get(i);

            page.setLayout(pages.get(i).getLayout());
            page.setToolPopupUrl(pages.get(i).getToolPopupUrl());
            page.setToolPopup(pages.get(i).isToolPopup());
            page.setSkin(pages.get(i).getSkin());
            page.setId(pages.get(i).getId());
            page.setPosition(pages.get(i).getPosition());
            page.setTitle(pages.get(i).getTitle());
            page.setTools(pages.get(i).getTools());

            SiteData.getSites().get(0).getPages().set(i, page);
        }
    }

    /**
     * get the permissions from the site
     * http://141.99.248.86:8089/direct/site/site_id/perms.json
     *
     * @param pagePermissions the object with the json data
     * @param index           the index of the project on the List
     * @param type            "project" for project type, and "site" for site type
     */
    public static void getSitePermissions(PagePermissions pagePermissions, int index, String type) {

        if (type == null) {
            SiteData.getSites().get(index).setAccess(pagePermissions.getData().getAccess());
            SiteData.getSites().get(index).setMaintain(pagePermissions.getData().getMaintain());
        } else {
            SiteData.getProjects().get(index).setAccess(pagePermissions.getData().getAccess());
            SiteData.getProjects().get(index).setMaintain(pagePermissions.getData().getMaintain());
        }
    }

    /**
     * get the permissions for My Workspace
     * http://141.99.248.86:8089/direct/site/~site_id/perms.json
     *
     * @param pagePermissions the object with the json data
     */
    public static void getSitePermissions(PagePermissions pagePermissions) {
        SiteData.getSites().get(0).setAccess(pagePermissions.getData().getAccess());
        SiteData.getSites().get(0).setMaintain(pagePermissions.getData().getMaintain());
    }

    /**
     * get the permissions for the user from the site (maintain or access)
     * http://141.99.248.86:8089/direct/site/site_id/userPerms.json
     *
     * @param pageUserPermissions the object with the json data
     * @param index               the index of the project on the List
     * @param type                "project" for project type, and "site" for site type
     */
    public static void getUserSitePermissions(PageUserPermissions pageUserPermissions, int index, String type) {

        if (type == null) {
            SiteData.getSites().get(index).setUserSitePermissions(pageUserPermissions.getData());
        } else {
            SiteData.getProjects().get(index).setUserSitePermissions(pageUserPermissions.getData());
        }
    }

    /**
     * get the permissions for the user for My Workspace (maintain or access)
     * http://141.99.248.86:8089/direct/site/site_id/userPerms.json
     *
     * @param pageUserPermissions the object with the json data
     */
    public static void getUserSitePermissions(PageUserPermissions pageUserPermissions) {
        SiteData.getSites().get(0).setUserSitePermissions(pageUserPermissions.getData());
    }

    /**
     * get assignment's site name
     * http://141.99.248.86:8089/direct/membership/site_id.json
     *
     * @param context
     * @param siteName   the name of the site
     * @param collection the assignment data
     */
    public static void getAssignmentSiteName(Context context, SiteName siteName, Assignment.AssignmentsCollection collection) {

        SharedPreferences preferences = context.getSharedPreferences("assignment_site_names", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(collection.getId(), siteName.getEntityTitle());
        editor.apply();
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

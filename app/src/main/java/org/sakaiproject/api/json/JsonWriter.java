package org.sakaiproject.api.json;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.sakaiproject.api.pojos.Time;
import org.sakaiproject.api.pojos.login.UserData;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.helpers.ActionsHelper;
import org.sakaiproject.sakai.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vasilis on 1/13/16.
 * this is the json writes for updates (PUT calls)
 */
public class JsonWriter {
    Context context;

    /**
     * the JsonWriter constructor
     *
     * @param context the context
     */
    public JsonWriter(Context context) {
        this.context = context;
    }


    public static String updateUserAccountJson(Context context, String name, String surname, String email, String pass) throws IOException {
        Gson gson = new Gson();
        if (ActionsHelper.createDirIfNotExists(context, User.getUserEid() + File.separator + "user")) {
            String userDataJson = ActionsHelper.readJsonFile(context, "fullUserDataJson", User.getUserEid() + File.separator + "user");

            UserData userData = gson.fromJson(userDataJson, UserData.class);
            userData.setFirstName(name);
            userData.setLastName(surname);
            userData.setEmail(email);

            if (pass != null)
                userData.setPassword(pass);

            return gson.toJson(userData);
        }
        return null;
    }


    public String updateUserProfileInfo() {
        JSONObject root = new JSONObject();
        try {
            root.put("academicProfileUrl", Profile.getAcademicProfileUrl());
            root.put("birthday", Profile.getBirthday());
            root.put("birthdayDisplay", Profile.getBirthdayDisplay());
            root.put("businessBiography", Profile.getBusinessBiography());
            // TODO: change the value with the list from the profile class
            root.put("companyProfiles", JSONObject.NULL);
            root.put("course", Profile.getCourse());

            if (Profile.getDateOfBirth() != null) {
                JSONObject dateOfBirth = new JSONObject();
                dateOfBirth.put("date", Profile.getDateOfBirth().getDate());
                dateOfBirth.put("day", Profile.getDateOfBirth().getDay());
                dateOfBirth.put("month", Profile.getDateOfBirth().getMonth());
                dateOfBirth.put("time", Profile.getDateOfBirth().getTime());
                dateOfBirth.put("timezoneOffset", Profile.getDateOfBirth().getTimezoneOffset());
                dateOfBirth.put("year", Profile.getDateOfBirth().getYear());
                root.put("dateOfBirth", dateOfBirth);
            }

            root.put("department", Profile.getDepartment());
            root.put("displayName", Profile.getDisplayName());
            root.put("email", User.getEmail());
            root.put("facsimile", Profile.getFacsimile());
            root.put("favouriteBooks", Profile.getFavouriteBooks());
            root.put("favouriteMovies", Profile.getFavouriteMovies());
            root.put("favouriteQuotes", Profile.getFavouriteQuotes());
            root.put("favouriteTvShows", Profile.getFavouriteTvShows());
            root.put("homepage", Profile.getHomepage());
            root.put("homephone", Profile.getHomephone());
            root.put("imageThumbUrl", Profile.getImageThumbUrl());
            root.put("imageUrl", Profile.getImageUrl());
            root.put("mobilephone", Profile.getMobilephone());
            root.put("nickname", Profile.getNickname());
            root.put("personalSummary", Profile.getPersonalSummary());
            root.put("position", Profile.getPosition());
            // TODO: change the value with the map from the profile class
            root.put("props", JSONObject.NULL);
            root.put("publications", Profile.getPublications());
            root.put("room", Profile.getRoom());
            root.put("school", Profile.getSchool());
            root.put("socialInfo", Profile.getSocialInfo());
            root.put("staffProfile", Profile.getStaffProfile());

//            if(Profile.getStatus() != null) {
//                JSONObject status = new JSONObject();
//                root.put("status", status);
//            }
            // TODO: has to change with the status object
            root.put("status", JSONObject.NULL);

            root.put("subjects", Profile.getSubjects());
            root.put("universityProfileUrl", Profile.getUniversityProfileUrl());
            root.put("userUuid", User.getUserId());
            root.put("workphone", Profile.getWorkphone());
            root.put("locked", Profile.isLocked());
            root.put("entityReference", "/profile/" + User.getUserEid());
            root.put("entityURL", context.getResources().getString(R.string.url) + "/profile/" + User.getUserEid());
            root.put("entityId", User.getUserEid());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root.toString();
    }
}

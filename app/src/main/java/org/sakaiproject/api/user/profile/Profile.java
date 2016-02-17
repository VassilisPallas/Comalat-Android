package org.sakaiproject.api.user.profile;

import org.sakaiproject.api.pojos.user.DateOfBirth;
import org.sakaiproject.api.pojos.user.ProfileStatus;
import org.sakaiproject.api.pojos.user.SocialNetworkingInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by vasilis on 10/26/15.
 * a JavaBeans convention class for the user Profile data
 */
public class Profile {
    private static Profile instance = null;

    private static String academicProfileUrl;
    private static String birthday;
    private static String birthdayDisplay;
    private static String businessBiography;
    private static List<String> companyProfiles;
    private static String course;
    private static DateOfBirth dateOfBirth;
    private static String department;
    private static String displayName;
    private static String facsimile; /* fax */
    private static String favouriteBooks;
    private static String favouriteMovies;
    private static String favouriteQuotes;
    private static String favouriteTvShows;
    private static String homepage;
    private static String homephone;
    private static String imageThumbUrl;
    private static String imageUrl;
    private static String mobilephone;
    private static String nickname;
    private static String personalSummary;
    private static String position;
    private static Map props;
    private static String publications;
    private static String room;
    private static String school;
    private static SocialNetworkingInfo socialInfo;
    private static String staffProfile;
    private static ProfileStatus status;
    private static String subjects;
    private static String universityProfileUrl;
    private static String workphone;
    private static boolean locked;

    private Profile() {
    }

    public static synchronized Profile getInstance() {
        if (instance == null)
            instance = new Profile();
        return instance;
    }

    public static synchronized void nullInstance() {
        if (instance != null)
            instance = null;
    }

    public static String getAcademicProfileUrl() {
        return academicProfileUrl;
    }

    public static String getBirthday() {
        return birthday;
    }

    public static String getBirthdayDisplay() {
        return birthdayDisplay;
    }

    public static String getBusinessBiography() {
        return businessBiography;
    }

    public static String getCourse() {
        return course;
    }

    public static DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public static String getDepartment() {
        return department;
    }

    public static String getFacsimile() {
        return facsimile;
    }

    public static String getFavouriteBooks() {
        return favouriteBooks;
    }

    public static String getFavouriteMovies() {
        return favouriteMovies;
    }

    public static String getFavouriteQuotes() {
        return favouriteQuotes;
    }

    public static String getFavouriteTvShows() {
        return favouriteTvShows;
    }

    public static String getHomepage() {
        return homepage;
    }

    public static String getHomephone() {
        return homephone;
    }

    public static String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public static String getImageUrl() {
        return imageUrl;
    }

    public static boolean isLocked() {
        return locked;
    }

    public static String getMobilephone() {
        return mobilephone;
    }

    public static String getNickname() {
        return nickname;
    }

    public static String getPersonalSummary() {
        return personalSummary;
    }

    public static String getPosition() {
        return position;
    }

    public static String getPublications() {
        return publications;
    }

    public static String getRoom() {
        return room;
    }

    public static String getSchool() {
        return school;
    }

    public static String getStaffProfile() {
        return staffProfile;
    }

    public static String getSubjects() {
        return subjects;
    }

    public static String getUniversityProfileUrl() {
        return universityProfileUrl;
    }

    public static String getWorkphone() {
        return workphone;
    }

    public static String getDisplayName() {
        return displayName;
    }

    public static SocialNetworkingInfo getSocialInfo() {
        return socialInfo;
    }

    public static ProfileStatus getStatus() {
        return status;
    }

    public static List<String> getCompanyProfiles() {
        return companyProfiles;
    }

    public static Map getProps() {
        return props;
    }

    public static void setProps(Map props) {
        Profile.props = props;
    }

    public static void setCompanyProfiles(List<String> companyProfiles) {
        Profile.companyProfiles = companyProfiles;
    }

    public static void setStatus(ProfileStatus status) {
        Profile.status = status;
    }

    public static void setSocialInfo(SocialNetworkingInfo socialInfo) {
        Profile.socialInfo = socialInfo;
    }

    public static void setDisplayName(String displayName) {
        Profile.displayName = displayName;
    }

    public static void setAcademicProfileUrl(String academicProfileUrl) {
        Profile.academicProfileUrl = academicProfileUrl;
    }

    public static void setBirthday(String birthday) {
        Profile.birthday = birthday;
    }

    public static void setBirthdayDisplay(String birthdayDisplay) {
        Profile.birthdayDisplay = birthdayDisplay;
    }

    public static void setBusinessBiography(String businessBiography) {
        Profile.businessBiography = businessBiography;
    }

    public static void setCourse(String course) {
        Profile.course = course;
    }

    public static void setDateOfBirth(DateOfBirth dateOfBirth) {
        Profile.dateOfBirth = dateOfBirth;
    }

    public static void setDepartment(String department) {
        Profile.department = department;
    }

    public static void setFacsimile(String facsimile) {
        Profile.facsimile = facsimile;
    }

    public static void setFavouriteBooks(String favouriteBooks) {
        Profile.favouriteBooks = favouriteBooks;
    }

    public static void setFavouriteMovies(String favouriteMovies) {
        Profile.favouriteMovies = favouriteMovies;
    }

    public static void setFavouriteQuotes(String favouriteQuotes) {
        Profile.favouriteQuotes = favouriteQuotes;
    }

    public static void setFavouriteTvShows(String favouriteTvShows) {
        Profile.favouriteTvShows = favouriteTvShows;
    }

    public static void setHomepage(String homepage) {
        Profile.homepage = homepage;
    }

    public static void setHomephone(String homephone) {
        Profile.homephone = homephone;
    }

    public static void setImageThumbUrl(String imageThumbUrl) {
        Profile.imageThumbUrl = imageThumbUrl;
    }

    public static void setImageUrl(String imageUrl) {
        Profile.imageUrl = imageUrl;
    }

    public static void setLocked(boolean locked) {
        Profile.locked = locked;
    }

    public static void setMobilephone(String mobilephone) {
        Profile.mobilephone = mobilephone;
    }

    public static void setNickname(String nickname) {
        Profile.nickname = nickname;
    }

    public static void setPersonalSummary(String personalSummary) {
        Profile.personalSummary = personalSummary;
    }

    public static void setPosition(String position) {
        Profile.position = position;
    }

    public static void setPublications(String publications) {
        Profile.publications = publications;
    }

    public static void setRoom(String room) {
        Profile.room = room;
    }

    public static void setSchool(String school) {
        Profile.school = school;
    }

    public static void setStaffProfile(String staffProfile) {
        Profile.staffProfile = staffProfile;
    }

    public static void setSubjects(String subjects) {
        Profile.subjects = subjects;
    }

    public static void setUniversityProfileUrl(String universityProfileUrl) {
        Profile.universityProfileUrl = universityProfileUrl;
    }

    public static void setWorkphone(String workphone) {
        Profile.workphone = workphone;
    }
}

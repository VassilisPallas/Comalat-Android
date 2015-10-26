package org.sakaiproject.api.user.data;

import java.util.Date;

/**
 * Created by vasilis on 10/26/15.
 */
public class Profile {
    private static Profile instance = null;

    private static String academicProfileUrl;
    private static String birthday;
    private static String birthdayDisplay;
    private static String businessBiography;
    private static String displayName;
    //private static List<String> companyProfiles;
    private static String course;
    private static Date dateOfBirth;
    private static String department;
    private static String facsimile; /* fax */
    private static String favouriteBooks;
    private static String favouriteMovies;
    private static String favouriteQuotes;
    private static String favouriteTvShows;
    private static String homepage;
    private static String homephone;
    private static String imageThumbUrl;
    private static String imageUrl;
    private static boolean locked;
    private static String mobilephone;
    private static String nickname;
    private static String personalSummary;
    private static String position;
    //private static Map props;
    private static String publications;
    private static String room;
    private static String school;
    //private static SocialNetworkingInfo socialInfo;
    private static String staffProfile;
    //private static ProfileStatus status;
    private static String subjects;
    private static String universityProfileUrl;
    private static String workphone;

    private Profile() {
    }

    public static synchronized Profile getInstance() {
        if (instance == null)
            instance = new Profile();
        return instance;
    }

    public static synchronized Profile nullInstance() {
        if (instance != null)
            instance = null;
        return instance;
    }

    public static synchronized String getAcademicProfileUrl() {
        return academicProfileUrl;
    }

    public static synchronized String getBirthday() {
        return birthday;
    }

    public static synchronized String getBirthdayDisplay() {
        return birthdayDisplay;
    }

    public static synchronized String getBusinessBiography() {
        return businessBiography;
    }

    public static synchronized String getCourse() {
        return course;
    }

    public static synchronized Date getDateOfBirth() {
        return dateOfBirth;
    }

    public static synchronized String getDepartment() {
        return department;
    }

    public static synchronized String getFacsimile() {
        return facsimile;
    }

    public static synchronized String getFavouriteBooks() {
        return favouriteBooks;
    }

    public static synchronized String getFavouriteMovies() {
        return favouriteMovies;
    }

    public static synchronized String getFavouriteQuotes() {
        return favouriteQuotes;
    }

    public static synchronized String getFavouriteTvShows() {
        return favouriteTvShows;
    }

    public static synchronized String getHomepage() {
        return homepage;
    }

    public static synchronized String getHomephone() {
        return homephone;
    }

    public static synchronized String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public static synchronized String getImageUrl() {
        return imageUrl;
    }

    public static synchronized boolean isLocked() {
        return locked;
    }

    public static synchronized String getMobilephone() {
        return mobilephone;
    }

    public static synchronized String getNickname() {
        return nickname;
    }

    public static synchronized String getPersonalSummary() {
        return personalSummary;
    }

    public static synchronized String getPosition() {
        return position;
    }

    public static synchronized String getPublications() {
        return publications;
    }

    public static synchronized String getRoom() {
        return room;
    }

    public static synchronized String getSchool() {
        return school;
    }

    public static synchronized String getStaffProfile() {
        return staffProfile;
    }

    public static synchronized String getSubjects() {
        return subjects;
    }

    public static synchronized String getUniversityProfileUrl() {
        return universityProfileUrl;
    }

    public static synchronized String getWorkphone() {
        return workphone;
    }

    public static synchronized String getDisplayName() {
        return displayName;
    }

    public static synchronized void setDisplayName(String displayName) {
        Profile.displayName = displayName;
    }

    public static synchronized void setAcademicProfileUrl(String academicProfileUrl) {
        Profile.academicProfileUrl = academicProfileUrl;
    }

    public static synchronized void setBirthday(String birthday) {
        Profile.birthday = birthday;
    }

    public static synchronized void setBirthdayDisplay(String birthdayDisplay) {
        Profile.birthdayDisplay = birthdayDisplay;
    }

    public static synchronized void setBusinessBiography(String businessBiography) {
        Profile.businessBiography = businessBiography;
    }

    public static synchronized void setCourse(String course) {
        Profile.course = course;
    }

    public static synchronized void setDateOfBirth(Date dateOfBirth) {
        Profile.dateOfBirth = dateOfBirth;
    }

    public static synchronized void setDepartment(String department) {
        Profile.department = department;
    }

    public static synchronized void setFacsimile(String facsimile) {
        Profile.facsimile = facsimile;
    }

    public static synchronized void setFavouriteBooks(String favouriteBooks) {
        Profile.favouriteBooks = favouriteBooks;
    }

    public static synchronized void setFavouriteMovies(String favouriteMovies) {
        Profile.favouriteMovies = favouriteMovies;
    }

    public static synchronized void setFavouriteQuotes(String favouriteQuotes) {
        Profile.favouriteQuotes = favouriteQuotes;
    }

    public static synchronized void setFavouriteTvShows(String favouriteTvShows) {
        Profile.favouriteTvShows = favouriteTvShows;
    }

    public static synchronized void setHomepage(String homepage) {
        Profile.homepage = homepage;
    }

    public static synchronized void setHomephone(String homephone) {
        Profile.homephone = homephone;
    }

    public static synchronized void setImageThumbUrl(String imageThumbUrl) {
        Profile.imageThumbUrl = imageThumbUrl;
    }

    public static synchronized void setImageUrl(String imageUrl) {
        Profile.imageUrl = imageUrl;
    }

    public static synchronized void setLocked(boolean locked) {
        Profile.locked = locked;
    }

    public static synchronized void setMobilephone(String mobilephone) {
        Profile.mobilephone = mobilephone;
    }

    public static synchronized void setNickname(String nickname) {
        Profile.nickname = nickname;
    }

    public static synchronized void setPersonalSummary(String personalSummary) {
        Profile.personalSummary = personalSummary;
    }

    public static synchronized void setPosition(String position) {
        Profile.position = position;
    }

    public static synchronized void setPublications(String publications) {
        Profile.publications = publications;
    }

    public static synchronized void setRoom(String room) {
        Profile.room = room;
    }

    public static synchronized void setSchool(String school) {
        Profile.school = school;
    }

    public static synchronized void setStaffProfile(String staffProfile) {
        Profile.staffProfile = staffProfile;
    }

    public static synchronized void setSubjects(String subjects) {
        Profile.subjects = subjects;
    }

    public static synchronized void setUniversityProfileUrl(String universityProfileUrl) {
        Profile.universityProfileUrl = universityProfileUrl;
    }

    public static synchronized void setWorkphone(String workphone) {
        Profile.workphone = workphone;
    }
}

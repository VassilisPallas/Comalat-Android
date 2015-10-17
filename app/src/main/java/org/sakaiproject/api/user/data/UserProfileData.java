package org.sakaiproject.api.user.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vasilis on 10/15/15.
 * This class stores the data from user's profile json
 * http://url/direct/profile/userEid.json
 */
@SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
public class UserProfileData implements Serializable {
    private String academicProfileUrl;
    private String birthday;
    private String birthdayDisplay;
    private String businessBiography;
    //private List<String> companyProfiles;
    private String course;
    private Date dateOfBirth;
    private String department;
    private String displayName;
    private String email;
    private String facsimile;
    private String favouriteBooks;
    private String favouriteMovies;
    private String favouriteQuotes;
    private String favouriteTvShows;
    private String homepage;
    private String homephone;
    private String imageThumbUrl;
    private String imageUrl;
    private boolean locked;
    private String mobilephone;
    private String nickname;
    private String personalSummary;
    private String position;
    //private Map props;
    private String publications;
    private String room;
    private String school;
    //private SocialNetworkingInfo socialInfo;
    private String staffProfile;
    //private ProfileStatus status;
    private String subjects;
    private String universityProfileUrl;
    private String userUuid;
    private String workphone;
    private String entityReference;
    private String entityURL;
    private String entityId;

    public String getAcademicProfileUrl() {
        return academicProfileUrl;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBirthdayDisplay() {
        return birthdayDisplay;
    }

    public String getBusinessBiography() {
        return businessBiography;
    }

    public String getCourse() {
        return course;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDepartment() {
        return department;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getFacsimile() {
        return facsimile;
    }

    public String getFavouriteBooks() {
        return favouriteBooks;
    }

    public String getFavouriteMovies() {
        return favouriteMovies;
    }

    public String getFavouriteQuotes() {
        return favouriteQuotes;
    }

    public String getFavouriteTvShows() {
        return favouriteTvShows;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getHomephone() {
        return homephone;
    }

    public String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isLocked() {
        return locked;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPersonalSummary() {
        return personalSummary;
    }

    public String getPosition() {
        return position;
    }

    public String getPublications() {
        return publications;
    }

    public String getRoom() {
        return room;
    }

    public String getSchool() {
        return school;
    }

    public String getStaffProfile() {
        return staffProfile;
    }

    public String getSubjects() {
        return subjects;
    }

    public String getUniversityProfileUrl() {
        return universityProfileUrl;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public String getWorkphone() {
        return workphone;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public String getEntityURL() {
        return entityURL;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setAcademicProfileUrl(String academicProfileUrl) {
        this.academicProfileUrl = academicProfileUrl;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setBirthdayDisplay(String birthdayDisplay) {
        this.birthdayDisplay = birthdayDisplay;
    }

    public void setBusinessBiography(String businessBiography) {
        this.businessBiography = businessBiography;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacsimile(String facsimile) {
        this.facsimile = facsimile;
    }

    public void setFavouriteBooks(String favouriteBooks) {
        this.favouriteBooks = favouriteBooks;
    }

    public void setFavouriteMovies(String favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }

    public void setFavouriteQuotes(String favouriteQuotes) {
        this.favouriteQuotes = favouriteQuotes;
    }

    public void setFavouriteTvShows(String favouriteTvShows) {
        this.favouriteTvShows = favouriteTvShows;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setHomephone(String homephone) {
        this.homephone = homephone;
    }

    public void setImageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPersonalSummary(String personalSummary) {
        this.personalSummary = personalSummary;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPublications(String publications) {
        this.publications = publications;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setStaffProfile(String staffProfile) {
        this.staffProfile = staffProfile;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public void setUniversityProfileUrl(String universityProfileUrl) {
        this.universityProfileUrl = universityProfileUrl;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public void setWorkphone(String workphone) {
        this.workphone = workphone;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public void setEntityURL(String entityURL) {
        this.entityURL = entityURL;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}

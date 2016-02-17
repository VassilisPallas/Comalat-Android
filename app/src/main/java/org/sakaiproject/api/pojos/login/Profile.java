package org.sakaiproject.api.pojos.login;

import org.sakaiproject.api.pojos.user.DateOfBirth;
import org.sakaiproject.api.pojos.user.ProfileStatus;
import org.sakaiproject.api.pojos.user.SocialNetworkingInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by vspallas on 09/02/16.
 */
public class Profile {
    private String academicProfileUrl;
    private String birthday;
    private String birthdayDisplay;
    private String businessBiography;
    private List<String> companyProfiles;
    private String course;
    private DateOfBirth dateOfBirth;
    private String department;
    private String displayName;
    private String facsimile; /* fax */
    private String favouriteBooks;
    private String favouriteMovies;
    private String favouriteQuotes;
    private String favouriteTvShows;
    private String homepage;
    private String homephone;
    private String imageThumbUrl;
    private String imageUrl;
    private String mobilephone;
    private String nickname;
    private String personalSummary;
    private String position;
    private Map props;
    private String publications;
    private String room;
    private String school;
    private SocialNetworkingInfo socialInfo;
    private String staffProfile;
    private ProfileStatus status;
    private String subjects;
    private String universityProfileUrl;
    private String workphone;
    private boolean locked;

    public String getAcademicProfileUrl() {
        return academicProfileUrl;
    }

    public void setAcademicProfileUrl(String academicProfileUrl) {
        this.academicProfileUrl = academicProfileUrl;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthdayDisplay() {
        return birthdayDisplay;
    }

    public void setBirthdayDisplay(String birthdayDisplay) {
        this.birthdayDisplay = birthdayDisplay;
    }

    public String getBusinessBiography() {
        return businessBiography;
    }

    public void setBusinessBiography(String businessBiography) {
        this.businessBiography = businessBiography;
    }

    public List<String> getCompanyProfiles() {
        return companyProfiles;
    }

    public void setCompanyProfiles(List<String> companyProfiles) {
        this.companyProfiles = companyProfiles;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(DateOfBirth dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFacsimile() {
        return facsimile;
    }

    public void setFacsimile(String facsimile) {
        this.facsimile = facsimile;
    }

    public String getFavouriteBooks() {
        return favouriteBooks;
    }

    public void setFavouriteBooks(String favouriteBooks) {
        this.favouriteBooks = favouriteBooks;
    }

    public String getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setFavouriteMovies(String favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }

    public String getFavouriteQuotes() {
        return favouriteQuotes;
    }

    public void setFavouriteQuotes(String favouriteQuotes) {
        this.favouriteQuotes = favouriteQuotes;
    }

    public String getFavouriteTvShows() {
        return favouriteTvShows;
    }

    public void setFavouriteTvShows(String favouriteTvShows) {
        this.favouriteTvShows = favouriteTvShows;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getHomephone() {
        return homephone;
    }

    public void setHomephone(String homephone) {
        this.homephone = homephone;
    }

    public String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public void setImageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPersonalSummary() {
        return personalSummary;
    }

    public void setPersonalSummary(String personalSummary) {
        this.personalSummary = personalSummary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Map getProps() {
        return props;
    }

    public void setProps(Map props) {
        this.props = props;
    }

    public String getPublications() {
        return publications;
    }

    public void setPublications(String publications) {
        this.publications = publications;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public SocialNetworkingInfo getSocialInfo() {
        return socialInfo;
    }

    public void setSocialInfo(SocialNetworkingInfo socialInfo) {
        this.socialInfo = socialInfo;
    }

    public String getStaffProfile() {
        return staffProfile;
    }

    public void setStaffProfile(String staffProfile) {
        this.staffProfile = staffProfile;
    }

    public ProfileStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileStatus status) {
        this.status = status;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getUniversityProfileUrl() {
        return universityProfileUrl;
    }

    public void setUniversityProfileUrl(String universityProfileUrl) {
        this.universityProfileUrl = universityProfileUrl;
    }

    public String getWorkphone() {
        return workphone;
    }

    public void setWorkphone(String workphone) {
        this.workphone = workphone;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}

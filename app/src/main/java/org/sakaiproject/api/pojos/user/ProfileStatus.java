package org.sakaiproject.api.pojos.user;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vasilis on 10/15/15.
 * This has created to get the data which are ProfileStatus type from the response jsons
 */
public class ProfileStatus implements Serializable {

    private String message;
    private Date dateAdded;
    private String dateFormatted; //not persisted, convenience holder
    //private int cleared; //maybe to hold value if the status has been cleared


    /**
     * Additional constructor to create a status object in one go
     */
    public ProfileStatus(String message, Date dateAdded, String dateFormatted) {
        this.message = message;
        this.dateAdded = dateAdded;
        this.dateFormatted = dateFormatted;
    }

    public String getMessage() {
        return message;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public String getDateFormatted() {
        return dateFormatted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }
}

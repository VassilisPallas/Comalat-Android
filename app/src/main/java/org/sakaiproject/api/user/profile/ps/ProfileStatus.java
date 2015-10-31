package org.sakaiproject.api.user.profile.ps;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vasilis on 10/15/15.
 */
public class ProfileStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userUuid;
    private String message;
    private Date dateAdded;
    private String dateFormatted; //not persisted, convenience holder
    //private int cleared; //maybe to hold value if the status has been cleared


    /**
     * Additional constructor to create a status object in one go
     */
    public ProfileStatus(String userUuid, String message, Date dateAdded) {
        this.userUuid = userUuid;
        this.message = message;
        this.dateAdded = dateAdded;
    }

}

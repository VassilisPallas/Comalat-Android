package org.sakaiproject.helpers;

import android.app.Activity;

/**
 * Created by vasilis on 1/12/16.
 */
public interface IUserAbout {

    void initialize(Activity activity);

    void fillValues();

    void checkVisibilities(Activity activity);

    void enableEdit();

    void saveEdit();

    void cancelEdit();
}

package org.sakaiproject.helpers.user_info_helpers;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by vasilis on 1/12/16.
 */
public interface IUserAbout {

    void initialize(AppCompatActivity activity);

    void fillValues();

    void checkVisibilities(AppCompatActivity activity);

    void enableEdit();

    void saveEdit();

    void cancelEdit();
}

package org.sakaiproject.helpers.user_info_helpers;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by vasilis on 1/12/16.
 */
public interface IUserAbout {

    void initialize(AppCompatActivity v);

    void fillValues();

    void checkVisibilities(AppCompatActivity v);
}

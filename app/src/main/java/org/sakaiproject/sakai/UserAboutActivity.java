package org.sakaiproject.sakai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.sakaiproject.api.user.profile.Profile;
import org.sakaiproject.helpers.user_info_helpers.BasicInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.ContactInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.PersonalInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.SocialNetworkInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.StaffInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.StudentInfoHelper;

public class UserAboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_about);

        setTitle(Profile.getDisplayName() + " - " + getString(R.string.about));

        new BasicInfoHelper(this);
        new ContactInfoHelper(this);
        new StaffInfoHelper(this);
        new StudentInfoHelper(this);
        new SocialNetworkInfoHelper(this);
        new PersonalInfoHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

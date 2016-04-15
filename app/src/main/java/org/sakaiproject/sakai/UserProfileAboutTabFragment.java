package org.sakaiproject.sakai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.sakaiproject.customviews.CustomSwipeRefreshLayout;
import org.sakaiproject.customviews.scrollview.AboutScrollView;
import org.sakaiproject.helpers.user_info_helpers.BasicInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.ContactInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.PersonalInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.SocialNetworkInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.StaffInfoHelper;
import org.sakaiproject.helpers.user_info_helpers.StudentInfoHelper;

/**
 * Created by vassilis on 4/15/16.
 */
public class UserProfileAboutTabFragment extends Fragment {

    public UserProfileAboutTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_user_about, container, false);
        initialize(v);
        return v;
    }

    private void initialize(View v) {
        new BasicInfoHelper(v);
        new ContactInfoHelper(v);
        new StaffInfoHelper(v);
        new StudentInfoHelper(v);
        new SocialNetworkInfoHelper(v);
        new PersonalInfoHelper(v);
    }
}

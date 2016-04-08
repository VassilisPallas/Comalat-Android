package org.sakaiproject.sakai;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by vspallas on 08/04/16.
 */
public class ChatSettings extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.chat_preference);
    }
}

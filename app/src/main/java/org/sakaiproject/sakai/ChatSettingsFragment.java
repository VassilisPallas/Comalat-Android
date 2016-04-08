package org.sakaiproject.sakai;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

public class ChatSettingsFragment extends Fragment {


    public ChatSettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_settings, container, false);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
//        boolean active = SP.getBoolean("chat_active", true);
//        boolean vibrate = SP.getBoolean("chat_vibrate", false);
//        boolean sound = SP.getBoolean("chat_sound", false);
//        boolean light = SP.getBoolean("notification_light", false);
//
//        Log.i("active", String.valueOf(active));
//        Log.i("vibrate", String.valueOf(vibrate));
//        Log.i("sound", String.valueOf(sound));
//        Log.i("light", String.valueOf(light));

        Map<String, ?> allEntries = SP.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }

        return v;
    }
}

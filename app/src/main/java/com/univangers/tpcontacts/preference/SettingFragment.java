package com.univangers.tpcontacts.preference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

import com.univangers.tpcontacts.R;

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_handler);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        onSharedPreferenceChanged(sharedPreferences,getString(R.string.second_pref));
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferenceScreen pref_screen = getPreferenceScreen();
        int count = pref_screen.getPreferenceCount();

        for(int i = 0;i<count;i++){
            Preference p = pref_screen.getPreference(i);
            if(p instanceof EditTextPreference){
                String input = sharedPreferences.getString(p.getKey(),"");
                System.out.println("My input : "+ input);

                try {
                    double value = Double.parseDouble(input);
                    if(value<0)
                        System.out.println(value + " is negative");
                    else
                        System.out.println(value + " is possitive");
                    p.setSummary(input);
                } catch (NumberFormatException e) {
                    System.out.println("String "+ input + "is not a number");
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }



    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


}

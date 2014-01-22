package net.szchmop.jbclient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Camille on 21/01/14.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_PREFS_SERVER_URL = "pref_key_server_url";
    public static final String KEY_PREFS_SERVER_PORT = "pref_key_server_port";
    public static final String KEY_PREFS_AUTH_LOGIN = "pref_key_auth_login";
    public static final String KEY_PREFS_AUTH_PASSWORD = "pref_key_auth_password";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        findPreference(KEY_PREFS_SERVER_URL).setSummary(sharedPreferences.getString(KEY_PREFS_SERVER_URL, ""));
        findPreference(KEY_PREFS_SERVER_PORT).setSummary(sharedPreferences.getString(KEY_PREFS_SERVER_PORT, ""));
        findPreference(KEY_PREFS_AUTH_LOGIN).setSummary(sharedPreferences.getString(KEY_PREFS_AUTH_LOGIN, ""));
        findPreference(KEY_PREFS_AUTH_PASSWORD).setSummary("***");
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREFS_SERVER_URL)
                || key.equals(KEY_PREFS_AUTH_LOGIN)
                || key.equals(KEY_PREFS_SERVER_PORT)) {
            findPreference(key).setSummary(sharedPreferences.getString(key, ""));
        }
    }
}

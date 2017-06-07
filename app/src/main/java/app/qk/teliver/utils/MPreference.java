
package app.qk.teliver.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


@SuppressLint("CommitPrefEdits")
public class MPreference {

    /**
     * The m preferences.
     */
    private SharedPreferences mPreferences;

    /**
     * The m editor.
     */
    private Editor mEditor;

    /**
     * Instantiates a new preference data.
     */
    public MPreference(Context context) {
        mPreferences = context.getSharedPreferences("dots.sdk",Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }


    /**
     * Store boolean in preference.
     *
     * @param key   the key
     * @param value the value
     */
    public void storeBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    /**
     * Gets the boolean from preference.
     *
     * @param key the key
     * @return the boolean from preference
     */
    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    /**
     * Store string in preference.
     *
     * @param key   the key
     * @param value the value
     */
    public void storeString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }


    /**
     * Clear all preference.
     */
    public void clearAllPreference() {
        mEditor.clear();
        mEditor.commit();
    }


    /**
     * Gets the string from preference.
     *
     * @param key the key
     * @return the string from preference
     */
    public String getString(String key) {
        return mPreferences.getString(key, null);
    }


}

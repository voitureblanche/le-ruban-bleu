package fr.free.gelmir.lerubanbleu;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 04/05/13
 * Time: 23:13
 * To change this template use File | Settings | File Templates.
 */
public class LeRubanBleuApplication extends Application {

    // Singleton
    private static final Object mLock = new Object();
    private static LeRubanBleuApplication mLeRubanBleuApplicationInstance;

    // Application preferences
    private static final String PREFERENCES = "fr.free.fr.gelmir.lerubanbanbleu.application.preferences";

    @Override
    public void onCreate() {
        Log.d("LeRubanBleuApplication", "Application onCreate");
        super.onCreate();

        // Singleton
        mLeRubanBleuApplicationInstance = this;

        // Set default preferences
        PreferenceManager.setDefaultValues(this, PREFERENCES, MODE_PRIVATE, R.xml.preferences, false);
        Log.d("LeRubanBleuApplication", "total number of episodes " + Integer.toString(getTotalNbEpisodes()));
        Log.d("LeRubanBleuApplication", "user latest episode " + Integer.toString(getUserLatestEpisode()));
        Locale userLanguage = getUserLanguage();
        Log.d("LeRubanBleuApplication", "user language " +  userLanguage.toString());

        // TODO Database creation?
    }

    // Singleton
    public synchronized static LeRubanBleuApplication getInstance() {
        synchronized (mLock) {
            if(mLeRubanBleuApplicationInstance == null) {
                mLeRubanBleuApplicationInstance = new LeRubanBleuApplication();
            }
        }
        return mLeRubanBleuApplicationInstance;
    }

    // Total number of episodes
    public void setTotalNbEpisodes(int totalNbEpisodes) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        preferences.edit().putString(getString(R.string.prefs_total_nb_episodes_key), Integer.toString(totalNbEpisodes)).commit();
    }
    public int getTotalNbEpisodes() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        return Integer.parseInt(preferences.getString(getString(R.string.prefs_total_nb_episodes_key), "0"));
    }

    // Last episode viewed by the user
    public void setUserLatestEpisode(int userLatestEpisode) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        preferences.edit().putString(getString(R.string.prefs_user_latest_episode_key), Integer.toString(userLatestEpisode)).commit();
    }
    public int getUserLatestEpisode() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        return Integer.parseInt(preferences.getString(getString(R.string.prefs_user_latest_episode_key), "0"));

    }

    // User language
    public void setUserLanguage(Locale userLanguage) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        preferences.edit().putString(getString(R.string.prefs_user_language_key), userLanguage.toString()).commit();
    }
    public Locale getUserLanguage() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        return getLocaleFromString(preferences.getString(getString(R.string.prefs_user_language_key), getString(R.string.prefs_user_language_default)));
    }

    // Zoom level
    public void setZoomLevel(int zoomLevel) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        preferences.edit().putString(getString(R.string.prefs_user_zoom_level_key), Integer.toString(zoomLevel)).commit();
    }
    public int getZoomLevel() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        return Integer.parseInt(preferences.getString(getString(R.string.prefs_user_zoom_level_key), "0"));
    }


    // Return Local from string
    private static Locale getLocaleFromString(String localeString)
    {
        if (localeString == null)
        {
            return null;
        }
        localeString = localeString.trim();
        if (localeString.toLowerCase().equals("default"))
        {
            return Locale.getDefault();
        }

        // Extract language
        int languageIndex = localeString.indexOf('_');
        String language = null;
        if (languageIndex == -1)
        {
            // No further "_" so is "{language}" only
            return new Locale(localeString, "");
        }
        else
        {
            language = localeString.substring(0, languageIndex);
        }

        // Extract country
        int countryIndex = localeString.indexOf('_', languageIndex + 1);
        String country = null;
        if (countryIndex == -1)
        {
            // No further "_" so is "{language}_{country}"
            country = localeString.substring(languageIndex+1);
            return new Locale(language, country);
        }
        else
        {
            // Assume all remaining is the variant so is "{language}_{country}_{variant}"
            country = localeString.substring(languageIndex+1, countryIndex);
            String variant = localeString.substring(countryIndex+1);
            return new Locale(language, country, variant);
        }
    }

}

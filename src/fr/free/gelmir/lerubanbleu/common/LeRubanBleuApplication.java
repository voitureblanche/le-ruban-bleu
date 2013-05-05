package fr.free.gelmir.lerubanbleu.common;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 04/05/13
 * Time: 23:13
 * To change this template use File | Settings | File Templates.
 */
public class LeRubanBleuApplication extends Application {

    public static final String PREF_LANGUAGE                = "PrefWelcomeScreen";
    public static final String PREF_CHECK_MPS_ENABLE		= "PrefCheckMpsEnable";
    public static final String PREF_NOTIFICATION_TYPE		= "PrefNotificationType";


    public int getTotalNb()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        //return Integer.parseInt(settings.getString(PREF_WELCOME_SCREEN, getString(R.string.pref_welcome_screen_default)));
        return 1;
    }

    public int getLatestUserEpisode()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return 1;
    }

    public int getLanguage()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return 1;
    }
}

package fr.free.gelmir.lerubanbleu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LeRubanBleuActivity extends Activity
{
    public static final String PREFS_NAME = "lerubanbleu";


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Restore user preferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);

    }

    @Override
    protected void onStop() {
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", mSilentMode);

        // Commit the edits!
        editor.commit();

    }
}

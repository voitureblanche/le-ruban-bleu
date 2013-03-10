package fr.free.gelmir.lerubanbleu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class LeRubanBleuActivity extends Activity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Bind Library

        // Restore user preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get last current page and display it

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Setup an editor on user preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Put current page
        editor.putLong("currentPage", 10);

        // Commit the edits!
        editor.commit();

    }
}

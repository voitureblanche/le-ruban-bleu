package fr.free.gelmir.lerubanbleu.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: de menditte
 * Date: 14/03/13
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public final class LibraryServiceHelper
{
    // Singleton
    //----------
    private static final Object mLock = new Object();
    private static LibraryServiceHelper mLibraryServiceHelperInstance;
    private static Context mApplicationContext;

    private HashMap mHashMap;


    // Intent
    public final static String GET_EPISODE_COMPLETE         = "fr.free.gelmir.service.LibraryServiceHelper.getEpisodeComplete";

    // Status
    public final static int STATUS_OK = 1;
    public final static int STATUS_KO = 2;

    // Extras
    public final static String EXTRA_EPISODE_POJO = "fr.free.gelmir.service.LibraryServiceHelper.extraEpisodePojo";
    public final static String EXTRA_STATUS       = "fr.free.gelmir.service.LibraryServiceHelper.extraStatus";


    // Constructor
    protected LibraryServiceHelper(Context context)
    {
        mApplicationContext = context.getApplicationContext();
    }

    public synchronized static LibraryServiceHelper getInstance(Context context)
    {
        synchronized (mLock) {
            if(mLibraryServiceHelperInstance == null){
                mLibraryServiceHelperInstance = new LibraryServiceHelper(context);
            }
        }

        return mLibraryServiceHelperInstance;
    }

    // Get an episode
    public void getEpisode(Context context, int episodeNb)
    {
        Log.d("LibraryServiceHelper", "Get episode " + Integer.toString(episodeNb));

        // Is the method pending?

        // Allocate result receiver
        ResultReceiver resultReceiver = new ResultReceiver(null){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Log.d("ResultReceiver", "onReceiveResult");
                handleResponse(resultCode, resultData);
            }
        };

        // Start service
        Intent intent = new Intent(context, LibraryService.class);
        intent.setAction(LibraryService.ACTION_GET_EPISODE);
        intent.putExtra(LibraryService.EXTRA_EPISODE_NB, episodeNb);
        intent.putExtra(LibraryService.EXTRA_RESULT_RECEIVER, resultReceiver);
        context.startService(intent);
    }

    // Cancel all actions
    public void cancelAllActions() {

    }

    // Handle the response
    private void handleResponse(int resultCode, Bundle resultData)
    {
        // Get original intent and action
        Intent intent = resultData.getParcelable(LibraryService.EXTRA_ORIGINAL_INTENT);
        String action = intent.getAction();

        if (action.equals(LibraryService.ACTION_GET_EPISODE)) {

            // Get intent extra
            int episodeId = intent.getIntExtra(LibraryService.EXTRA_EPISODE_NB, -1);
            Episode episode = resultData.getParcelable(LibraryService.EXTRA_EPISODE_POJO);

            // Broadcast intent
            Intent broadcastIntent = new Intent(GET_EPISODE_COMPLETE);
            broadcastIntent.putExtra(EXTRA_EPISODE_POJO, episode);
            switch (resultCode) {
                case 0:
                    broadcastIntent.putExtra(EXTRA_STATUS, STATUS_OK);
                    break;

                case -1:
                    broadcastIntent.putExtra(EXTRA_STATUS, STATUS_KO);
                    break;
            }
            mApplicationContext.sendBroadcast(broadcastIntent);
        }
    }
}
